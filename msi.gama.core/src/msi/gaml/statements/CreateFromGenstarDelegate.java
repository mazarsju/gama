/**
 * Created by drogoul, 27 mai 2015
 *
 */
package msi.gaml.statements;

import java.util.*;
import msi.gama.common.interfaces.*;
import msi.gama.runtime.IScope;
import msi.gama.util.IList;
import msi.gaml.operators.fastmaths.CmnFastMath;
import msi.gaml.types.*;

/**
 * Class CreateFromDatabaseDelegate.
 *
 * @author drogoul
 * @since 27 mai 2015
 *
 */
public class CreateFromGenstarDelegate implements ICreateDelegate {

	/**
	 * Method acceptSource()
	 *
	 * @see msi.gama.common.interfaces.ICreateDelegate#acceptSource(java.lang.Object)
	 */
	@Override
	public boolean acceptSource(final Object source) {
		return source instanceof List && ((List) source).get(0) instanceof String &&
			((List) source).get(0).equals(IKeyword.GENSTAR_POPULATION);
	}

	/**
	 * Method createFrom() Method used to read initial values and attributes
	 * from a CSV values descring a synthetic population
	 *
	 * @author Vo Duc An
	 * @since 04-09-2012
	 * @see msi.gama.common.interfaces.ICreateDelegate#createFrom(msi.gama.runtime.IScope, java.util.List, int, java.lang.Object)
	 */
	@Override
	public boolean createFrom(final IScope scope, final List<Map> inits, final Integer max, final Object source,
		final Arguments init, final CreateStatement statement) {
		final IList<Map> syntheticPopulation = (IList<Map>) source;
		final int num = max == null ? syntheticPopulation.length(scope) - 1
			: CmnFastMath.min(syntheticPopulation.length(scope) - 1, max);
		// the first element of syntheticPopulation a string (i.e.,
		// "genstar_population")
		for ( int i = 1; i < num; i++ ) {
			final Map genstarInit = syntheticPopulation.get(i);
			statement.fillWithUserInit(scope, genstarInit);
			// mix genstar's init attributes with user's init
			inits.add(genstarInit);
		}
		return true;
	}

	/**
	 * Method fromFacetType()
	 * @see msi.gama.common.interfaces.ICreateDelegate#fromFacetType()
	 */
	@Override
	public IType fromFacetType() {
		return Types.LIST;
	}

}
