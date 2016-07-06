/*********************************************************************************************
 *
 *
 * 'ContainerVariable.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.variables;

import msi.gama.common.interfaces.*;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.util.GAML;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.*;
import msi.gaml.statements.Facets;
import msi.gaml.types.IType;
import msi.gaml.variables.ContainerVariable.ContainerVarValidator;

@facets(
	value = {
		@facet(name = IKeyword.NAME,
			type = IType.NEW_VAR_ID,
			optional = false,
			doc = @doc("The name of the attribute")),
		@facet(name = IKeyword.TYPE, type = IType.TYPE_ID, optional = true),
		@facet(name = IKeyword.INIT,
			// AD 02/16 TODO Allow to declare ITypeProvider.OWNER_TYPE here
			type = IType.NONE,
			optional = true,
			doc = @doc("The initial value of the attribute")),
		@facet(name = IKeyword.VALUE,
			type = IType.NONE,
			optional = true,
			doc = @doc(value = "", deprecated = "Use 'update' instead")),
		@facet(name = IKeyword.UPDATE,
			// AD 02/16 TODO Allow to declare ITypeProvider.OWNER_TYPE here
			type = IType.NONE,
			optional = true,
			doc = @doc("An expression that will be evaluated each cycle to compute a new value for the attribute")),
		@facet(name = IKeyword.FUNCTION,
			// AD 02/16 TODO Allow to declare ITypeProvider.OWNER_TYPE here
			type = IType.NONE,
			optional = true,
			doc = @doc("Used to specify an expression that will be evaluated each time the attribute is accessed. This facet is incompatible with both 'init:' and 'update:'")),
		@facet(name = IKeyword.CONST,
			type = IType.BOOL,
			optional = true,
			doc = @doc("Indicates whether this attribute can be subsequently modified or not")),
		@facet(name = IKeyword.CATEGORY,
			type = IType.LABEL,
			optional = true,
			doc = @doc("Soon to be deprecated. Declare the parameter in an experiment instead")),
		@facet(name = IKeyword.PARAMETER,
			type = IType.LABEL,
			optional = true,
			doc = @doc("Soon to be deprecated. Declare the parameter in an experiment instead")),
		@facet(name = IKeyword.SIZE,
			type = { IType.INT, IType.POINT },
			optional = true,
			doc = @doc(value = "",
				deprecated = "Use the operator matrix_with(size, fill_with) or list_with(size, fill_with) instead")),
		@facet(name = IKeyword.OF, type = IType.TYPE_ID, optional = true),
		@facet(name = IKeyword.INDEX, type = IType.TYPE_ID, optional = true),
		@facet(name = IKeyword.FILL_WITH,
			type = IType.NONE,
			optional = true,
			doc = @doc(value = "",
				deprecated = "Use the operator matrix_with(size, fill_with) or list_with(size, fill_with) instead")) },
	omissible = IKeyword.NAME)
@symbol(kind = ISymbolKind.Variable.CONTAINER, with_sequence = false,
concept = { IConcept.CONTAINER })
@inside(kinds = { ISymbolKind.SPECIES, ISymbolKind.EXPERIMENT, ISymbolKind.MODEL })
@doc("Allows to declare an attribute of a species or an experiment")
@validator(ContainerVarValidator.class)
public class ContainerVariable extends Variable {

	public static class ContainerVarValidator extends VarValidator {

		/**
		 * Method validate()
		 * @see msi.gaml.compilation.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription vd) {
			Facets ff = vd.getFacets();
			// Replaces the size: and fill_with: facets with an operator depending on the type of the container
			if ( ff.containsKey(SIZE) ) {
				IExpression size = ff.getExpr(SIZE);
				IExpression fill = ff.getExpr(FILL_WITH);
				if ( fill == null ) {
					fill = IExpressionFactory.NIL_EXPR;
				}
				IType type = vd.getType();
				switch (type.id()) {
					case IType.LIST:
						if ( size.getType().id() != IType.INT ) {
							vd.error("Facet 'size:' must be of type int", IGamlIssue.WRONG_TYPE, SIZE, "int");
							return;
						}
						IExpression init =
							GAML.getExpressionFactory().createOperator("list_with", vd, null, size, fill);
						ff.put(INIT, init);
						break;
					case IType.MATRIX:
						if ( size.getType().id() != IType.POINT ) {
							vd.error("Facet 'size:' must be of type point", IGamlIssue.WRONG_TYPE, SIZE, "point");
							return;
						}

						init = GAML.getExpressionFactory().createOperator("matrix_with", vd, null, size, fill);
						ff.put(INIT, init);
						break;
					default:
						vd.error("Facet 'size:' can only be used for lists and matrices", IGamlIssue.UNKNOWN_FACET,
							SIZE);
						return;
				}
			} else if ( ff.containsKey(FILL_WITH) ) {
				vd.error("Facet 'size:' missing. A container cannot be filled if no size is provided",
					IGamlIssue.MISSING_FACET, FILL_WITH);
				return;
			}
			super.validate(vd);
		}
	}

	public ContainerVariable(final IDescription sd) {
		super(sd);
	}

}
