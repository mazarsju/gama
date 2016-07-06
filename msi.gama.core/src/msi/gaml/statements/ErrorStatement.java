/*********************************************************************************************
 * 
 * 
 * 'ErrorStatement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.statements;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.types.IType;

/**
 * Written by drogoul Modified on 6 févr. 2010
 * 
 * @todo Description
 * 
 */

@symbol(name = IKeyword.ERROR, kind = ISymbolKind.SINGLE_STATEMENT, with_sequence = false, concept = { IConcept.SYSTEM })
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT, ISymbolKind.LAYER })
@facets(value = { @facet(name = IKeyword.MESSAGE,
	type = IType.STRING,
	optional = false,
	doc = @doc("the message to display in the error.")) }, omissible = IKeyword.MESSAGE)
@doc(value = "The statement makes the agent output an error dialog (if the simulation contains a user interface). Otherwise displays the error in the console.",
	usages = { @usage(value = "Throwing an error",
		examples = { @example("error 'This is an error raised by ' + self;") }) })
public class ErrorStatement extends AbstractStatement {

	final IExpression message;

	public ErrorStatement(final IDescription desc) {
		super(desc);
		message = getFacet(IKeyword.MESSAGE);
	}

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		IAgent agent = scope.getAgentScope();
		String mes = null;
		if ( agent != null && !agent.dead() ) {
			mes = Cast.asString(scope, message.value(scope));
			throw GamaRuntimeException.error(mes, scope);
		}
		return mes;
	}
	//
	// @Override
	// public IType getType() {
	// return Types.STRING;
	// }

}
