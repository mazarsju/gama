/*********************************************************************************************
 *
 *
 * 'EventLayerStatement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.outputs.layers;

import java.util.Collection;
import java.util.List;

import msi.gama.common.interfaces.IGamlIssue;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.outputs.layers.EventLayerStatement.EventLayerValidator;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.IDescriptionValidator;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.StatementDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.types.IType;

/**
 * Written by Marilleau Modified on 16 novembre 2012
 * 
 * @todo Description
 *
 */
@symbol(name = IKeyword.EVENT, kind = ISymbolKind.LAYER, with_sequence = true, concept = { IConcept.GUI })
@inside(symbols = { IKeyword.DISPLAY })
@facets(value = {
		@facet(name = "unused", type = IType.ID, values = { "mouse_up", "mouse_down", "mouse_move", "mouse_enter",
				"mouse_exit" }, optional = true, doc = @doc(value = "an unused facet that serves only for the purpose of declaring the string values"), internal = true),
		@facet(name = IKeyword.NAME, type = IType.ID,
				// values = { "mouse_up", "mouse_down", "mouse_drag" },
				optional = false, doc = @doc("the type of event captured: can be  \"mouse_up\", \"mouse_down\", \"mouse_move\", \"mouse_exit\", \"mouse_enter\" or a character (not yet functional)")),
		@facet(name = IKeyword.ACTION, type = IType.STRING, optional = false, doc = @doc("the identifier of the action to be executed. It has to be an action written in the global block. This action needs to be defined in 'global' and will receive two possible arguments: the location of the mouse in the environment and the agents under the mouse. For instance:`action myAction (point location, list selected_agents)`")),
		@facet(name = EventLayerStatement.defaultPointArg, type = IType.STRING, optional = true, internal = true),
		@facet(name = EventLayerStatement.defaultListArg, type = IType.STRING, optional = true, internal = true) }, omissible = IKeyword.NAME)
@validator(EventLayerValidator.class)
@doc(value = "`" + IKeyword.EVENT
		+ "` allows to interact with the simulation by capturing mouse or key events and doing an action. This action needs to be defined in 'global' and will receive two possible arguments: the location of the mouse in the environment and the agents under the mouse. The names of these arguments need not to be fixed: instead, the first argument of type 'point' will receive the location of the mouse, while the first argument whose type is compatible with 'container<agent>' will receive the list of agents selected.", usages = {
				@usage(value = "The general syntax is:", examples = {
						@example(value = "event [event_type] action: myAction;", isExecutable = false) }),
				@usage(value = "For instance:", examples = { @example(value = "global {", isExecutable = false),
						@example(value = "   // ... ", isExecutable = false),
						@example(value = "   action myAction (point location, list<agent> selected_agents) {", isExecutable = false),
						@example(value = "      // location: contains le location of the click in the environment", isExecutable = false),
						@example(value = "      // selected_agents: contains agents clicked by the event", isExecutable = false),
						@example(value = "      ", isExecutable = false),
						@example(value = "      // code written by modelers", isExecutable = false),
						@example(value = "   }", isExecutable = false), @example(value = "}", isExecutable = false),
						@example(value = "", isExecutable = false),
						@example(value = "experiment Simple type:gui {", isExecutable = false),
						@example(value = "   display my_display {", isExecutable = false),
						@example(value = "      event mouse_up action: myAction;", isExecutable = false),
						@example(value = "   }", isExecutable = false),
						@example(value = "}", isExecutable = false) }) }, see = { IKeyword.DISPLAY, IKeyword.AGENTS,
								IKeyword.CHART, "graphics", IKeyword.GRID_POPULATION, IKeyword.IMAGE, IKeyword.OVERLAY,
								IKeyword.POPULATION, })
public class EventLayerStatement extends AbstractLayerStatement {

	public static class EventLayerValidator implements IDescriptionValidator {

		@Override
		public void validate(final IDescription description) {
			final String actionName = description.getFacets().getLabel(ACTION);
			StatementDescription sd = description.getModelDescription().getAction(actionName);
			if (sd == null) {
				// display
				IDescription superDesc = description.getEnclosingDescription();
				// output or permanent
				superDesc = superDesc.getEnclosingDescription();
				if (superDesc.getKeyword() == IKeyword.PERMANENT) {
					// we look into experiment
					sd = superDesc.getEnclosingDescription().getAction(actionName);
				}
			}
			if (sd == null) {
				description.error("Action '" + actionName + "' is not defined in 'global'", IGamlIssue.UNKNOWN_ACTION,
						ACTION);
				return;
			}
			String pointArg = null, listArg = null;
			final Collection<StatementDescription> args = sd.getArgs();
			for (final StatementDescription d : args) {
				if (d.getName().equals(defaultPointArg) || pointArg == null && d.getType().id() == IType.POINT) {
					pointArg = d.getName();
				}
				if (d.getName().equals(defaultListArg) || listArg == null && d.getType().id() == IType.LIST) {
					listArg = d.getName();
				}
			}
			if (pointArg == null) {
				description.info(
						"Action '" + actionName + "' does not accept '" + defaultPointArg
								+ "' or any argument of type point. The location of the mouse will not be passed to it.",
						IGamlIssue.MISSING_ARGUMENT, ACTION);
			} else if (!pointArg.equals(defaultListArg)) {
				description.info("The location of the mouse will be passed to the parameter '" + pointArg
						+ "' of action '" + actionName + "'", IGamlIssue.GENERAL, ACTION);
			}
			if (listArg == null && !description.getName().equals(MOUSE_EXITED)
					&& !description.getName().equals(MOUSE_ENTERED) && !description.getName().equals(MOUSE_MOVED)) {
				description.info(
						"Action '" + actionName + "' does not accept '" + defaultListArg
								+ "' or any argument of type list<agent>. The agents selected will not be passed to it.",
						IGamlIssue.MISSING_ARGUMENT, ACTION);
			} else if (listArg != null && !listArg.equals(defaultListArg)) {
				description.info("The list of selected agents will be passed to the parameter '" + listArg
						+ "' of action '" + actionName + "'", IGamlIssue.GENERAL, ACTION);
			}
			final List<String> argNames = sd.getArgNames();
			if (pointArg != null && listArg != null) {
				if (argNames.size() > 2) {
					description.error("Actions called by this event layer can not define any argument in addition to '"
							+ pointArg + "' and '" + listArg + "'", IGamlIssue.DIFFERENT_ARGUMENTS, ACTION);
					return;
				}
			}
			// The facets are modified
			if (pointArg != null) {
				description.getFacets().putAsLabel(defaultPointArg, pointArg);
			}
			if (listArg != null) {
				description.getFacets().putAsLabel(defaultListArg, listArg);
			}
		}
	}

	public static final String defaultPointArg = "mouse_location";
	public static final String defaultListArg = "selected_agents";

	public EventLayerStatement(final IDescription desc) throws GamaRuntimeException {
		super(/* context, */desc);
	}

	@Override
	public boolean _init(final IScope scope) throws GamaRuntimeException {
		final IExpression eventType = getFacet(IKeyword.NAME);
		final IExpression actionName = getFacet(IKeyword.ACTION);
		return true;
	}

	@Override
	public short getType() {
		return EVENT;
	}

	@Override
	public String toString() {
		// StringBuffer sb = new StringBuffer();
		return "Event layer: " + this.getFacet(IKeyword.NAME).literalValue();
	}

	/**
	 * Method _step()
	 * 
	 * @see msi.gama.outputs.layers.AbstractLayerStatement#_step(msi.gama.runtime.IScope)
	 */
	@Override
	protected boolean _step(final IScope scope) {
		return true;
	}
}
