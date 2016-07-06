/*********************************************************************************************
 * 
 *
 * 'ReflexStatement.java', in plugin 'msi.gama.core', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.architecture.reflex;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.IDescriptionValidator.ValidNameValidator;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.statements.AbstractStatementSequence;
import msi.gaml.types.IType;

@symbol(name = { IKeyword.REFLEX, IKeyword.INIT }, kind = ISymbolKind.BEHAVIOR, with_sequence = true, unique_name = true, concept = { IConcept.BEHAVIOR, IConcept.SCHEDULER })
@inside(kinds = { ISymbolKind.SPECIES, ISymbolKind.EXPERIMENT, ISymbolKind.MODEL })
@facets(value = { @facet(name = IKeyword.WHEN, type = IType.BOOL, optional = true, doc = @doc("an expression that evaluates a boolean, the condition to fulfill in order to execute the statements embedded in the reflex.")),
	@facet(name = IKeyword.NAME, type = IType.ID, optional = true, doc = @doc("the identifier of the reflex")) }, omissible = IKeyword.NAME)
@validator(ValidNameValidator.class)
@doc(value="A reflex is a sequence of statements that can be executed, at each time step, by the agent. If no facet when: is defined, it will be executed every time step. If there is a when: facet, it is executed only if the boolean expression evaluates to true.", usages = {
	@usage(value="Example:", examples = {
		@example(value="reflex my_reflex when: flip (0.5){ 		//Only executed when flip returns true", isExecutable=false),
		@example(value="    write \"Executing the unconditional reflex\";", isExecutable=false),
		@example(value="}", isExecutable=false)})})
public class ReflexStatement extends AbstractStatementSequence {

	private final IExpression when;

	public ReflexStatement(final IDescription desc) {
		super(desc);
		when = getFacet(IKeyword.WHEN);
		if ( hasFacet(IKeyword.NAME) ) {
			setName(getLiteral(IKeyword.NAME));
		}
	}

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		return when == null || Cast.asBool(scope, when.value(scope)) ? super.privateExecuteIn(scope) : null;
	}

}
