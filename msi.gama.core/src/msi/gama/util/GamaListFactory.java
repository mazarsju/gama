/**
 * Created by drogoul, 30 janv. 2015
 *
 */
package msi.gama.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.lang.ArrayUtils;

import com.google.common.collect.Iterables;

import msi.gama.runtime.IScope;
import msi.gaml.expressions.IExpression;
import msi.gaml.types.GamaType;
import msi.gaml.types.IType;
import msi.gaml.types.Types;

/**
 * Class GamaListFactory. The factory for creating lists from various other
 * objects. All the methods that accept the scope as a parameter will observe
 * the contract of GAML containers, which is that the objects contained in the
 * list will be casted to the content type of the list. To avoid unecessary
 * castings, some methods (without the scope parameter) will simply copy the
 * objects.
 *
 * @author drogoul
 * @since 30 janv. 2015
 *
 */
public class GamaListFactory {

	private static final int DEFAULT_SIZE = 10;

	/**
	 * Create a GamaList from an array of objects, but does not attempt casting
	 * its values.
	 *
	 * @param contentType
	 * @param collection
	 * @warning ***WARNING*** This operation can end up putting values of the
	 *          wrong type into the list
	 * @return
	 */

	public static <T> IList<T> createWithoutCasting(final IType contentType, final T... objects) {
		final IList<T> list = create(contentType, objects.length);
		list.addAll(Arrays.asList(objects));
		return list;
	}

	public static IList<Integer> createWithoutCasting(final IType contentType, final int[] objects) {
		final IList<Integer> list = create(contentType, objects.length);
		list.addAll(Arrays.asList(ArrayUtils.toObject(objects)));
		return list;
	}

	public static IList<Double> createWithoutCasting(final IType contentType, final double[] objects) {
		final IList<Double> list = create(contentType, objects.length);
		list.addAll(Arrays.asList(ArrayUtils.toObject(objects)));
		return list;
	}

	/**
	 * Create a GamaList from an iterable, but does not attempt casting its
	 * values.
	 *
	 * @param contentType
	 * @param collection
	 * @warning ***WARNING*** This operation can end up putting values of the
	 *          wrong type into the list
	 * @return
	 */

	public static <T> IList<T> createWithoutCasting(final IType contentType, final Iterable<T> objects) {
		final IList<T> list = create(contentType);
		Iterables.addAll(list, objects);
		return list;
	}

	private static void castAndAdd(final IScope scope, final IList list, final Object o) {
		list.addValue(scope, o);
	}

	public static IList create(final IScope scope, final IType contentType, final IContainer container) {
		if (container == null) {
			return create(contentType);
		}
		if (GamaType.requiresCasting(contentType, container.getType().getContentType())) {
			return create(scope, contentType, container.iterable(scope));
		} else {
			return createWithoutCasting(contentType, container.iterable(scope));
		}
	}

	public static <T> IList<T> create(final IScope scope, final IType contentType, final IList<T> container) {
		if (container == null) {
			return create(contentType);
		}
		if (GamaType.requiresCasting(contentType, container.getType().getContentType())) {
			return create(scope, contentType, (Collection) container);
		} else {
			return createWithoutCasting(contentType, container);
		}
	}

	public static <T> IList<T> create(final IScope scope, final IType contentType, final Iterable<T> iterable) {
		final IList<T> list = create(contentType);
		for (final Object o : iterable) {
			castAndAdd(scope, list, o);
		}
		return list;
	}

	public static <T> IList<T> create(final IScope scope, final IType contentType, final Iterator<T> iterator) {
		final IList<T> list = create(contentType);
		if (iterator != null) {
			while (iterator.hasNext()) {
				castAndAdd(scope, list, iterator.next());
			}
		}
		return list;
	}

	public static <T> IList<T> create(final IScope scope, final IType contentType, final Enumeration<T> iterator) {
		final IList<T> list = create(contentType);
		if (iterator != null) {
			while (iterator.hasMoreElements()) {
				castAndAdd(scope, list, iterator.nextElement());
			}
		}
		return list;
	}

	public static <T> IList<T> create(final IScope scope, final IType contentType, final T... objects) {
		final IList<T> list = create(contentType, objects == null ? 0 : objects.length);
		if (objects != null) {
			for (final Object o : objects) {
				castAndAdd(scope, list, o);
			}
		}
		return list;
	}

	public static IList create(final IScope scope, final IType contentType, final byte[] ints) {
		final IList list = create(contentType, ints == null ? 0 : ints.length);
		if (ints != null) {
			for (final int o : ints) {
				castAndAdd(scope, list, Integer.valueOf(o));
			}
		}
		return list;
	}

	public static IList create(final IScope scope, final IType contentType, final int[] ints) {
		final IList list = create(contentType, ints == null ? 0 : ints.length);
		if (ints != null) {
			for (final int o : ints) {
				castAndAdd(scope, list, Integer.valueOf(o));
			}
		}
		return list;
	}

	public static IList create(final IScope scope, final IType contentType, final long[] ints) {
		final IList list = create(contentType, ints == null ? 0 : ints.length);
		if (ints != null) {
			for (final long o : ints) {
				castAndAdd(scope, list, Long.valueOf(o).intValue());
			}
		}
		return list;
	}

	public static IList create(final IScope scope, final IType contentType, final float[] doubles) {
		final IList list = create(contentType, doubles == null ? 0 : doubles.length);
		if (doubles != null) {
			for (final float o : doubles) {
				castAndAdd(scope, list, Double.valueOf(o));
			}
		}
		return list;
	}

	public static IList create(final IScope scope, final IExpression fillExpr, final Integer size) {
		if (fillExpr == null) {
			return create(Types.NO_TYPE, size);
		}
		final Object[] contents = new Object[size];
		final IType contentType = fillExpr.getType();
		// 10/01/14. Cannot use Arrays.fill() everywhere: see Issue 778.
		if (fillExpr.isConst()) {
			Arrays.fill(contents, fillExpr.value(scope));
		} else {
			for (int i = 0; i < contents.length; i++) {
				contents[i] = fillExpr.value(scope);
			}
		}
		return create(scope, contentType, contents);
	}

	public static IList create(final IScope scope, final IType contentType, final double[] doubles) {
		final IList list = create(contentType, doubles == null ? 0 : doubles.length);
		if (doubles != null) {
			for (final double o : doubles) {
				castAndAdd(scope, list, Double.valueOf(o));
			}
		}
		return list;
	}

	public static <T> IList<T> create(final IType contentType, final int size) {
		return new GamaList<T>(size, contentType);
	}

	public static <T> IList<T> create(final IType contentType) {
		return create(contentType, DEFAULT_SIZE);
	}

	public static <T> IList<T> create(final Class<T> clazz) {
		return create(Types.get(clazz));
	}

	public static IList create() {
		return create(Types.NO_TYPE);
	}

}
