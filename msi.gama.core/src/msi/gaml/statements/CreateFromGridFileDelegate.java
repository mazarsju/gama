/**
 * Created by drogoul, 27 mai 2015
 *
 */
package msi.gaml.statements;

import java.util.*;
import msi.gama.common.interfaces.*;
import msi.gama.metamodel.shape.IShape;
import msi.gama.runtime.IScope;
import msi.gama.util.file.GamaGridFile;
import msi.gaml.operators.fastmaths.CmnFastMath;
import msi.gaml.types.*;

/**
 * Class CreateFromDatabaseDelegate.
 *
 * @author drogoul
 * @since 27 mai 2015
 *
 */
public class CreateFromGridFileDelegate implements ICreateDelegate {

	/**
	 * Method acceptSource()
	 *
	 * @see msi.gama.common.interfaces.ICreateDelegate#acceptSource(java.lang.Object)
	 */
	@Override
	public boolean acceptSource(final Object source) {

		return source instanceof GamaGridFile;
	}

	/**
	 * Method createFrom() Method used to read initial values and attributes
	 * from a GRID file.
	 *
	 * @author Alexis Drogoul
	 * @since 04-09-2012
	 * @see msi.gama.common.interfaces.ICreateDelegate#createFrom(msi.gama.runtime.IScope, java.util.List, int, java.lang.Object)
	 */
	@Override
	public boolean createFrom(final IScope scope, final List<Map> inits, final Integer max, final Object input,
		final Arguments init, final CreateStatement statement) {
		final GamaGridFile file = (GamaGridFile) input;
		final int num = max == null ? file.length(scope) : CmnFastMath.min(file.length(scope), max);
		for ( int i = 0; i < num; i++ ) {
			final IShape g = file.get(scope, i);
			final Map map = g.getOrCreateAttributes();
			// The shape is added to the initial values
			map.put(IKeyword.SHAPE, g);
			// GIS attributes are mixed with the attributes of agents
			statement.fillWithUserInit(scope, map);
			inits.add(map);
		}
		return true;
	}

	/**
	 * Method fromFacetType()
	 * @see msi.gama.common.interfaces.ICreateDelegate#fromFacetType()
	 */
	@Override
	public IType fromFacetType() {
		return Types.FILE;
	}

}
