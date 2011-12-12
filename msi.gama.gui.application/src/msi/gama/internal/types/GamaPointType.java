/*
 * GAMA - V1.4 http://gama-platform.googlecode.com
 * 
 * (c) 2007-2011 UMI 209 UMMISCO IRD/UPMC & Partners (see below)
 * 
 * Developers :
 * 
 * - Alexis Drogoul, UMI 209 UMMISCO, IRD/UPMC (Kernel, Metamodel, GAML), 2007-2011
 * - Vo Duc An, UMI 209 UMMISCO, IRD/UPMC (SWT, multi-level architecture), 2008-2011
 * - Patrick Taillandier, UMR 6228 IDEES, CNRS/Univ. Rouen (Batch, GeoTools & JTS), 2009-2011
 * - Beno�t Gaudou, UMR 5505 IRIT, CNRS/Univ. Toulouse 1 (Documentation, Tests), 2010-2011
 * - Pierrick Koch, UMI 209 UMMISCO, IRD/UPMC (XText-based GAML), 2010-2011
 * - Romain Lavaud, UMI 209 UMMISCO, IRD/UPMC (RCP environment), 2010
 * - Francois Sempe, UMI 209 UMMISCO, IRD/UPMC (EMF model, Batch), 2007-2009
 * - Edouard Amouroux, UMI 209 UMMISCO, IRD/UPMC (C++ initial porting), 2007-2008
 * - Chu Thanh Quang, UMI 209 UMMISCO, IRD/UPMC (OpenMap integration), 2007-2008
 */
package msi.gama.internal.types;

import java.util.*;
import msi.gama.interfaces.*;
import msi.gama.kernel.exceptions.GamaRuntimeException;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.util.*;

/**
 * Written by drogoul Modified on 1 ao�t 2010
 * 
 * @todo Description
 * 
 */
@type(value = IType.POINT_STR, id = IType.POINT, wraps = { GamaPoint.class })
public class GamaPointType extends GamaType<GamaPoint> {

	@Override
	public GamaPoint cast(final IScope scope, final Object obj, final Object param)
		throws GamaRuntimeException {
		return staticCast(scope, obj, param);
	}

	public static GamaPoint staticCast(final IScope scope, final Object obj, final Object param) {
		if ( obj instanceof GamaPoint ) { return (GamaPoint) obj; }
		if ( obj instanceof IGeometry ) { return ((IGeometry) obj).getLocation(); }
		if ( obj instanceof List ) {
			List l = (List) obj;
			if ( l.size() > 1 ) { return new GamaPoint(Cast.asFloat(l.get(0)), Cast.asFloat(l
				.get(1))); }
			return null;
		}
		if ( obj instanceof Map ) {
			Map m = (Map) obj;
			final double x = Cast.asFloat(scope, m.get("x"));
			final double y = Cast.asFloat(scope, m.get("y"));
			// double z = TypeManager.asFloat(get("z"));
			return new GamaPoint(x, y);
		}
		if ( obj instanceof GamaPair ) { return new GamaPoint(Cast.asFloat(null,
			((GamaPair) obj).first()), Cast.asFloat(null, ((GamaPair) obj).last())); }
		if ( obj == null ) { return null; }
		final double dval = Cast.asFloat(scope, obj);
		return new GamaPoint(dval, dval);
	}

	@Override
	public GamaPoint getDefault() {
		return null;
	}

	@Override
	public IType defaultContentType() {
		return Types.get(FLOAT);
	}

}
