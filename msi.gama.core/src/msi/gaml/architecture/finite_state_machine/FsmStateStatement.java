/*********************************************************************************************
 *
 *
 * 'FsmStateStatement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.architecture.finite_state_machine;

import java.util.*;
import gnu.trove.map.hash.THashMap;
import msi.gama.common.interfaces.*;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.architecture.IArchitecture;
import msi.gaml.architecture.finite_state_machine.FsmStateStatement.StateValidator;
import msi.gaml.compilation.*;
import msi.gaml.descriptions.*;
import msi.gaml.expressions.*;
import msi.gaml.operators.Cast;
import msi.gaml.statements.*;
import msi.gaml.types.IType;

/**
 * The Class FsmStateStatement.
 *
 * @author drogoul
 */

@symbol(name = FsmStateStatement.STATE, kind = ISymbolKind.BEHAVIOR, with_sequence = true, unique_name = true)
@inside(symbols = IKeyword.FSM, kinds = { ISymbolKind.SPECIES, ISymbolKind.EXPERIMENT, ISymbolKind.MODEL })
@facets(
	value = {
		@facet(name = FsmStateStatement.INITIAL,
			type = IType.BOOL,
			optional = true,
			doc = @doc("specifies whether the state is the initial one (default value = false)") ),
		@facet(name = FsmStateStatement.FINAL,
			type = IType.BOOL,
			optional = true,
			doc = @doc("specifies whether the state is a final one (i.e. there is no transition from this state to another state) (default value= false)") ),
		@facet(name = IKeyword.NAME, type = IType.ID, optional = false, doc = @doc("the identifier of the state") ) },
	omissible = IKeyword.NAME)
@validator(StateValidator.class)
@doc(
	value = "A state, like a reflex, can contains several statements that can be executed at each time step by the agent.",
	usages = { @usage(value = "Here is an exemple integrating 2 states and the statements in the FSM architecture:",
		examples = { @example(value = "	state s_init initial: true {", isExecutable = false),
			@example(value = "		enter { write \"Enter in\" + state; }", isExecutable = false),
			@example(value = "			write \"Enter in\" + state;", isExecutable = false),
			@example(value = "		}", isExecutable = false), @example(value = "", isExecutable = false),
			@example(value = "		write state;", isExecutable = false), @example(value = "", isExecutable = false),
			@example(value = "		transition to: s1 when: (cycle > 2) {", isExecutable = false),
			@example(value = "			write \"transition s_init -> s1\";", isExecutable = false),
			@example(value = "		}", isExecutable = false), @example(value = "", isExecutable = false),
			@example(value = "		exit {", isExecutable = false),
			@example(value = "			write \"EXIT from \"+state;", isExecutable = false),
			@example(value = "		}", isExecutable = false), @example(value = "	}", isExecutable = false),
			@example(value = "	state s1 {", isExecutable = false), @example(value = "", isExecutable = false),
			@example(value = "	enter {write 'Enter in '+state;}", isExecutable = false),
			@example(value = "", isExecutable = false), @example(value = "	write state;", isExecutable = false),
			@example(value = "", isExecutable = false),
			@example(value = "	exit {write 'EXIT from '+state;}", isExecutable = false),
			@example(value = "}", isExecutable = false) }) },
	see = { FsmStateStatement.ENTER, FsmStateStatement.EXIT, FsmTransitionStatement.TRANSITION })
public class FsmStateStatement extends AbstractStatementSequence {

	static List<String> AllowedArchitectures = Arrays.asList(IKeyword.USER_CONTROLLED, IKeyword.USER_FIRST,
		IKeyword.USER_INIT, IKeyword.USER_LAST, IKeyword.USER_ONLY);

	public static class StateValidator implements IDescriptionValidator {

		/**
		 * Method validate()
		 * @see msi.gaml.compilation.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription description) {
			// Verify that the state is inside a species with fsm control
			SpeciesDescription species = description.getSpeciesContext();
			String keyword = description.getKeyword();
			IArchitecture control = species.getControl();
			// String control = species.getControlName();
			if ( !(control instanceof FsmArchitecture) ) {
				if ( keyword.equals(STATE) ) {
					description.error("A state can only be defined in an fsm-controlled or user-controlled species",
						IGamlIssue.WRONG_CONTEXT);
					return;
				} else if ( control.getClass() == FsmArchitecture.class ) {
					description.error("A " + description.getKeyword() +
						" can only be defined in a user-controlled species (one of" + AllowedArchitectures + ")",
						IGamlIssue.WRONG_CONTEXT);
					return;
				}
			}
			if ( !Assert.nameIsValid(description) ) { return; }
			Facets ff = description.getFacets();
			IExpression expr = ff.getExpr(INITIAL);
			if ( IExpressionFactory.TRUE_EXPR.equals(expr) ) {
				assertNoOther(description, INITIAL);
			} else {
				expr = ff.getExpr(FINAL);
				if ( IExpressionFactory.TRUE_EXPR.equals(expr) ) {
					assertNoOther(description, FINAL);
				} else {
					assertAtLeastOne(description, INITIAL);
				}
			}
		}

		private void assertNoOther(final IDescription desc, final String facet) {
			final IDescription sd = desc.getEnclosingDescription();
			if ( !(sd instanceof SpeciesDescription) ) { return; }
			for ( final IDescription child : ((SpeciesDescription) sd).getBehaviors() ) {
				if ( child.equals(desc) || !child.getKeyword().equals(STATE) ) {
					continue;
				}
				IExpression expr = child.getFacets().getExpr(facet);
				if ( IExpressionFactory.TRUE_EXPR.equals(expr) ) {
					final String error = "Only one " + facet + " state is allowed.";
					child.error(error, IGamlIssue.DUPLICATE_DEFINITION, facet, TRUE);
				}
			}
		}

		private void assertAtLeastOne(final IDescription desc, final String facet) {
			final IDescription sd = desc.getEnclosingDescription();
			if ( !(sd instanceof SpeciesDescription) ) { return; }
			for ( final IDescription child : ((SpeciesDescription) sd).getBehaviors() ) {
				String s = child.getKeyword();
				if ( s.equals(STATE) || s.equals(USER_PANEL) ) {
					final IExpression expr = child.getFacets().getExpr(facet);
					if ( expr == null ) {
						continue;
					}
					if ( IExpressionFactory.TRUE_EXPR.equals(expr) ) { return; }
				}
			}
			final String error = "No " + facet + " state defined";
			sd.error(error, IGamlIssue.MISSING_DEFINITION, sd.getUnderlyingElement(null), desc.getKeyword(), facet,
				TRUE);
		}
	}

	public static final String STATE_MEMORY = "state_memory";

	public static final String INITIAL = "initial";
	public static final String FINAL = "final";
	protected static final String STATE = "state";
	public static final String ENTER = "enter";
	public static final String EXIT = "exit";
	private FsmEnterStatement enterActions = null;
	private FsmExitStatement exitActions = null;
	List<FsmTransitionStatement> transitions = new ArrayList();
	private int transitionsSize;
	boolean isInitial;
	boolean isFinal;

	public FsmStateStatement(final IDescription desc) {
		super(desc);
		setName(getLiteral(IKeyword.NAME)); // A VOIR
		isInitial = Cast.asBool(null, getLiteral(FsmStateStatement.INITIAL));
		isFinal = Cast.asBool(null, getLiteral(FsmStateStatement.FINAL));
	}

	@Override
	public void setChildren(final List<? extends ISymbol> children) {
		for ( final ISymbol c : children ) {
			if ( c instanceof FsmEnterStatement ) {
				enterActions = (FsmEnterStatement) c;
			} else if ( c instanceof FsmExitStatement ) {
				exitActions = (FsmExitStatement) c;
			} else if ( c instanceof FsmTransitionStatement ) {
				transitions.add((FsmTransitionStatement) c);
			}
		}
		children.remove(enterActions);
		children.remove(exitActions);
		children.removeAll(transitions);
		transitionsSize = transitions.size();
		super.setChildren(children);
	}

	protected boolean beginExecution(final IScope scope) throws GamaRuntimeException {
		final IAgent agent = scope.getAgentScope();
		if ( scope.interrupted() ) { return false; }
		final Boolean enter = (Boolean) agent.getAttribute(ENTER);
		Map<String, Object> memory = (Map) agent.getAttribute(STATE_MEMORY);
		if ( enter || memory == null ) {
			memory = new THashMap();
			agent.setAttribute(STATE_MEMORY, memory);
		} else {
			for ( final Map.Entry<String, Object> entry : memory.entrySet() ) {
				scope.addVarWithValue(entry.getKey(), entry.getValue());
			}
		}
		if ( enter ) {
			if ( enterActions != null ) {
				enterActions.executeOn(scope);
			}
			if ( agent.dead() ) { return false; }
			agent.setAttribute(ENTER, false);
		}
		return true;
	}

	protected Object bodyExecution(final IScope scope) throws GamaRuntimeException {
		return super.privateExecuteIn(scope);
	}

	protected String evaluateTransitions(final IScope scope) throws GamaRuntimeException {
		final IAgent agent = scope.getAgentScope();
		for ( int i = 0; i < transitionsSize; i++ ) {
			final FsmTransitionStatement transition = transitions.get(i);

			if ( transition.evaluatesTrueOn(scope) ) {
				final String futureState = transition.getName();
				haltOn(scope);
				transition.executeOn(scope);
				scope.setAgentVarValue(agent, STATE, futureState);
				return futureState;
			}
		}
		if ( !agent.dead() ) {
			scope.saveAllVarValuesIn((Map) agent.getAttribute(STATE_MEMORY));
		}
		return name;

	}

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		if ( !beginExecution(scope) ) { return null; }
		bodyExecution(scope);
		return evaluateTransitions(scope);
	}

	public void haltOn(final IScope scope) throws GamaRuntimeException {
		if ( exitActions != null ) {
			exitActions.executeOn(scope);
		}
	}

	public FsmExitStatement getExitStatement() {
		return exitActions;
	}

	public boolean hasExitActions() {
		return exitActions != null;
	}

	public boolean isInitial() {
		return isInitial;
	}

	public boolean isFinal() {
		return isFinal;
	}

}
