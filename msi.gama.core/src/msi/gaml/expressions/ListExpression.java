/*********************************************************************************************
 *
 *
 * 'ListExpression.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.expressions;

import java.util.Arrays;
import java.util.List;

import msi.gama.precompiler.GamlProperties;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IList;
import msi.gaml.types.GamaType;
import msi.gaml.types.Types;

/**
 * ListValueExpr.
 *
 * @author drogoul 23 août 07
 */
public class ListExpression extends AbstractExpression {

	public static IExpression create(final List<? extends IExpression> elements) {
		final ListExpression u = new ListExpression(elements);

		// if (u.isConst() && GamaPreferences.CONSTANT_OPTIMIZATION.getValue())
		// {
		// final IExpression e =
		// GAML.getExpressionFactory().createConst(u.value(null), u.getType(),
		// u.serialize(false));
		// // System.out.println(" ==== Simplification of " + u.toGaml() + "
		// // into " + e.toGaml());
		// return e;
		// }
		return u;
	}

	final IExpression[] elements;
	private final Object[] values;
	// private boolean isConst;
	private boolean computed;

	ListExpression(final List<? extends IExpression> elements) {
		this.elements = elements.toArray(new IExpression[0]);
		final int n = this.elements.length;
		values = new Object[n];
		setName(elements.toString());
		type = Types.LIST.of(GamaType.findCommonType(this.elements, GamaType.TYPE));
		isConst();
	}

	public IExpression[] getElements() {
		return elements;
	}

	@Override
	public IExpression resolveAgainst(final IScope scope) {
		final ListExpression copy = new ListExpression(Arrays.asList(elements));
		for (int i = 0; i < elements.length; i++) {
			final IExpression exp = elements[i];
			if (exp != null) {
				copy.elements[i] = exp.resolveAgainst(scope);
			}
		}
		return copy;
	}

	@Override
	public IList value(final IScope scope) throws GamaRuntimeException {
		// if ( isConst && computed ) { return
		// GamaListFactory.createWithoutCasting(getType().getContentType(),
		// values); }
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				computed = false;
				return GamaListFactory.create();
			}
			values[i] = elements[i].value(scope);
		}
		computed = true;
		// Important NOT to return the reference to values (but a copy of it).
		return GamaListFactory.createWithoutCasting(getType().getContentType(), values);
	}

	@Override
	public String toString() {
		return Arrays.toString(elements);
	}

	@Override
	public boolean isConst() {
		return false;

		// for ( final IExpression e : elements ) {
		// // indicates a former problem in the compilation of the expression
		// if ( e == null ) { return false; }
		// if ( !e.isConst() ) { return false; }
		// }
		// isConst = true;
		// return true;
	}

	@Override
	public String serialize(final boolean includingBuiltIn) {
		final StringBuilder sb = new StringBuilder();
		surround(sb, '[', ']', elements);
		return sb.toString();
	}

	@Override
	public String getTitle() {
		return "literal list of type " + getType().getTitle();
	}

	@Override
	public String getDocumentation() {
		return "Constant " + isConst() + "<br>Contains elements of type " + type.getContentType().getTitle();
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return elements.length == 0;
	}

	/**
	 * Method collectPlugins()
	 * 
	 * @see msi.gaml.descriptions.IGamlDescription#collectPlugins(java.util.Set)
	 */
	@Override
	public void collectMetaInformation(final GamlProperties meta) {
		for (final IExpression e : elements) {
			if (e != null) {
				e.collectMetaInformation(meta);
			}
		}
	}

}
