/*********************************************************************************************
 *
 *
 * 'GamaPointType.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.types;

import java.util.*;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.shape.*;
import msi.gama.precompiler.*;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaPair;
import msi.gaml.operators.Cast;

/**
 * Written by drogoul Modified on 1 ao�t 2010
 *
 * @todo Description
 *
 */
@type(name = IKeyword.POINT,
id = IType.POINT,
wraps = { ILocation.class, GamaPoint.class },
kind = ISymbolKind.Variable.REGULAR,
concept = { IConcept.TYPE, IConcept.POINT })
public class GamaPointType extends GamaType<ILocation> {

	@Override
	public ILocation cast(final IScope scope, final Object obj, final Object param, final boolean copy)
		throws GamaRuntimeException {
		return staticCast(scope, obj, copy);
	}

	public static ILocation staticCast(final IScope scope, final Object obj, final boolean copy) {
		if ( obj instanceof ILocation ) { return (ILocation) obj; }
		if ( obj instanceof IShape ) { return ((IShape) obj).getLocation(); }
		if ( obj instanceof List ) {
			List l = (List) obj;
			if ( l.size() > 2 ) { return new GamaPoint(Cast.asFloat(scope, l.get(0)), Cast.asFloat(scope, l.get(1)),
				Cast.asFloat(scope, l.get(2))); }
			if ( l.size() > 1 ) { return new GamaPoint(Cast.asFloat(scope, l.get(0)), Cast.asFloat(scope, l.get(1))); }
			if ( l.size() > 0 ) { return staticCast(scope, l.get(0), copy); }
			return new GamaPoint(0, 0, 0);
		}
		if ( obj instanceof Map ) {
			Map m = (Map) obj;
			final double x = Cast.asFloat(scope, m.get("x"));
			final double y = Cast.asFloat(scope, m.get("y"));
			double z = Cast.asFloat(scope, m.get("z"));
			return new GamaPoint(x, y, z);
		}
		if ( obj instanceof GamaPair ) { return new GamaPoint(Cast.asFloat(scope, ((GamaPair) obj).first()),
			Cast.asFloat(scope, ((GamaPair) obj).last())); }
		if ( obj == null ) { return null; }
		final double dval = Cast.asFloat(scope, obj);
		return new GamaPoint(dval, dval, dval);
	}

	@Override
	public ILocation getDefault() {
		return null;
	}

	@Override
	public IType getContentType() {
		return Types.get(FLOAT);
	}

	@Override
	public IType getKeyType() {
		return Types.get(INT);
	}

	@Override
	public boolean canCastToConst() {
		return true;
	}

	@Override
	public boolean isDrawable() {
		return true;
	}
	//
	// @Override
	// public boolean hasContents() {
	// return true;
	// }

}
