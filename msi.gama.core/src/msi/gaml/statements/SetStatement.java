/*********************************************************************************************
 * 
 * 
 * 'SetStatement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.statements;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.serializer;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GAML;
import msi.gaml.compilation.IDescriptionValidator;
import msi.gaml.descriptions.*;
import msi.gaml.expressions.*;
import msi.gaml.operators.Cast;
import msi.gaml.statements.SetStatement.AssignmentSerializer;
import msi.gaml.statements.SetStatement.AssignmentValidator;
import msi.gaml.types.IType;

/**
 * Written by drogoul Modified on 6 févr. 2010
 * 
 * @todo Description
 * 
 */

@facets(value = { /* @facet(name = IKeyword.VAR, type = IType.NONE, optional = true), */
	@facet(name = IKeyword.NAME, type = IType.NONE, optional = false,
		doc = @doc("the name of an existing variable or attribute to be modified")),
	@facet(name = IKeyword.VALUE, type = { IType.NONE }, optional = false,
		doc = @doc("the value to affect to the variable or attribute")) }, omissible = IKeyword.NAME)
@symbol(name = { IKeyword.SET }, kind = ISymbolKind.SINGLE_STATEMENT,
concept = { IConcept.ATTRIBUTE }, with_sequence = false)
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT }, symbols = IKeyword.CHART)
@validator(AssignmentValidator.class)
@doc(value = "Allows to assign a value to the variable or attribute specified")
@serializer(AssignmentSerializer.class)
public class SetStatement extends AbstractStatement {

	public static class AssignmentSerializer extends SymbolSerializer {

		@Override
		protected void serialize(final SymbolDescription desc, final StringBuilder sb, final boolean includingBuiltIn) {
			Facets f = desc.getFacets();
			sb.append(f.get(NAME).serialize(includingBuiltIn)).append(" <- ")
				.append(f.get(VALUE).serialize(includingBuiltIn)).append(";");
		}

	}

	public static class AssignmentValidator implements IDescriptionValidator {

		/**
		 * Method validate()
		 * @see msi.gaml.compilation.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription cd) {
			final IExpressionDescription receiver = cd.getFacets().get(NAME);
			// String name = cd.getName();
			final IExpression expr = receiver.getExpression();
			if ( !(expr instanceof IVarExpression) ) {
				cd.error("The expression " + cd.getFacets().getLabel(NAME) + " is not a reference to a variable ", NAME);
				return;
			}
			final IExpressionDescription assigned = cd.getFacets().get(VALUE);
			if ( assigned != null ) {
				Assert.typesAreCompatibleForAssignment(cd, Cast.toGaml(expr), expr.getType(), /* expr.getContentType(), */
					assigned);
			}

			// AD 19/1/13: test of the constants
			if ( ((IVarExpression) expr).isNotModifiable() ) {
				cd.error("The variable " + expr.serialize(false) +
					" is a constant or a function and cannot be assigned a value.", IKeyword.NAME);
			}

		}
	}

	protected final IVarExpression varExpr;
	protected final IExpression value;

	public SetStatement(final IDescription desc) {
		super(desc);
		varExpr = (IVarExpression) getFacet(IKeyword.NAME);
		setName(IKeyword.SET + getVarName());
		IExpression expr = getFacet(IKeyword.VALUE);
		if ( expr == null ) {
			value = GAML.getExpressionFactory().createConst(varExpr.getType().getDefault(), varExpr.getType());
		} else {
			value = expr;
		}

	}

	@Override
	protected Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		final Object val = value.value(scope);
		varExpr.setVal(scope, val, false);
		return val;
	}

	public String getVarName() {
		return varExpr.literalValue();
	}

}
