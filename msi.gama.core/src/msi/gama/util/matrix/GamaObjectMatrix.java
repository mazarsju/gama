/*********************************************************************************************
 *
 *
 * 'GamaObjectMatrix.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.util.matrix;

import java.util.*;
import org.apache.commons.lang.ArrayUtils;
import com.google.common.collect.ImmutableList;
import msi.gama.common.util.RandomUtils;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.metamodel.topology.grid.GamaSpatialMatrix;
import msi.gama.runtime.*;
import msi.gama.runtime.GAMA.InScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gaml.operators.fastmaths.CmnFastMath;
import msi.gaml.types.*;

public class GamaObjectMatrix extends GamaMatrix<Object> {

	static public GamaObjectMatrix from(final int c, final int r, final IMatrix m) {
		if ( m instanceof GamaFloatMatrix ) { return new GamaObjectMatrix(c, r, ((GamaFloatMatrix) m).getMatrix()); }
		if ( m instanceof GamaObjectMatrix ) { return new GamaObjectMatrix(c, r, ((GamaObjectMatrix) m).getMatrix(),
			m.getType().getContentType()); }
		if ( m instanceof GamaIntMatrix ) { return new GamaObjectMatrix(c, r, ((GamaIntMatrix) m).matrix); }
		if ( m instanceof GamaSpatialMatrix ) { return new GamaObjectMatrix(c, r, ((GamaSpatialMatrix) m).getMatrix(),
			m.getType().getContentType()); }
		return null;
	}

	/** The matrix. */
	private Object[] matrix;

	public GamaObjectMatrix(final ILocation p, final IType contentsType) {
		this((int) p.getX(), (int) p.getY(), contentsType);
	}

	public GamaObjectMatrix(final int cols, final int rows, final IType contentsType) {
		super(cols, rows, contentsType);
		setMatrix(new Object[cols * rows]);
	}

	public GamaObjectMatrix(final int cols, final int rows, final double[] objects) {
		this(cols, rows, Types.FLOAT);
		int n = CmnFastMath.min(objects.length, rows * cols);
		for ( int i = 0; i < n; i++ ) {
			matrix[i] = objects[i];
		}
		// java.lang.System.arraycopy(objects, 0, getMatrix(), 0, FastMath.min(objects.length, rows * cols));
	}

	public GamaObjectMatrix(final int cols, final int rows, final int[] objects) {
		this(cols, rows, Types.INT);
		int n = CmnFastMath.min(objects.length, rows * cols);
		for ( int i = 0; i < n; i++ ) {
			matrix[i] = objects[i];
		}
	}

	public GamaObjectMatrix(final int cols, final int rows, final Object[] objects, final IType contentsType) {
		this(cols, rows, contentsType);
		java.lang.System.arraycopy(objects, 0, getMatrix(), 0, CmnFastMath.min(objects.length, rows * cols));
	}

	public GamaObjectMatrix(final IScope scope, final IList objects, final ILocation preferredSize,
		final IType contentsType) {
		super(scope, objects, preferredSize, contentsType);
		setMatrix(new Object[numRows * numCols]);
		boolean requiresCasting = GamaType.requiresCasting(contentsType, objects.getType().getContentType());
		if ( preferredSize != null ) {
			for ( int i = 0, stop = CmnFastMath.min(getMatrix().length, objects.size()); i < stop; i++ ) {
				getMatrix()[i] =
					requiresCasting ? contentsType.cast(scope, objects.get(i), null, false) : objects.get(i);
			}
		} else if ( isFlat(objects) ) {
			for ( int i = 0, stop = objects.size(); i < stop; i++ ) {
				getMatrix()[i] = contentsType.cast(scope, objects.get(i), null, false);
			}
		} else {
			for ( int i = 0; i < numRows; i++ ) {
				for ( int j = 0; j < numCols; j++ ) {
					set(scope, j, i, ((List) objects.get(j)).get(i));
				}
			}
		}
	}

	// public GamaObjectMatrix(final IScope scope, final Object[] mat) {
	// super(1, mat.length);
	// setMatrix(mat);
	// }

	@Override
	public void _clear() {
		Arrays.fill(getMatrix(), null);
	}

	@Override
	public boolean _contains(final IScope scope, final Object o) {
		for ( int i = 0; i < getMatrix().length; i++ ) {
			if ( getMatrix()[i].equals(o) ) { return true; }
		}
		return false;
	}

	@Override
	public Object _first(final IScope scope) {
		if ( getMatrix().length == 0 ) { return null; }
		return getMatrix()[0];
	}

	@Override
	public Object _last(final IScope scope) {
		if ( getMatrix().length == 0 ) { return null; }
		return getMatrix()[getMatrix().length - 1];
	}

	@Override
	public Integer _length(final IScope scope) {
		return getMatrix().length;
	}

	/**
	 * Take two matrices (with the same number of columns) and create a big matrix putting the second matrix on the
	 * right side of the first matrix
	 *
	 * @param two matrix to concatenate
	 * @return the matrix concatenated
	 */

	// @Override
	// @operator(value = IKeyword.APPEND_VERTICALLY, content_type = ITypeProvider.FIRST_CONTENT_TYPE, category={IOperatorCategory.MATRIX})
	public IMatrix _opAppendVertically(final IScope scope, final IMatrix b) {
		GamaObjectMatrix a = this;
		Object[] ma = a.getMatrix();
		Object[] mb = ((GamaObjectMatrix) b).getMatrix();
		Object[] mab = ArrayUtils.addAll(ma, mb);
		IType newContentsType = GamaType.findCommonType(getType().getContentType(), b.getType().getContentType());
		IMatrix fl = new GamaObjectMatrix(a.getCols(scope), a.getRows(scope) + b.getRows(scope), mab, newContentsType);

		// throw GamaRuntimeException.error("ATTENTION : Matrix additions not implemented. Returns nil for the moment");
		return fl;
	}

	/**
	 * Take two matrices (with the same number of rows) and create a big matrix putting the second matrix on the right
	 * side of the first matrix
	 *
	 * @param two matrix to concatenate
	 * @return the matrix concatenated
	 */

	// @Override
	// @operator(value = IKeyword.APPEND_HORYZONTALLY, content_type = ITypeProvider.FIRST_CONTENT_TYPE, category={IOperatorCategory.MATRIX})
	public GamaObjectMatrix _opAppendHorizontally(final IScope scope, final IMatrix b) {
		// GamaObjectMatrix a = this;
		GamaObjectMatrix aprime = _reverse(scope);
		// System.out.println("aprime = " + aprime);
		GamaObjectMatrix bprime = GamaObjectMatrix.from(b.getCols(scope), b.getRows(scope), b)._reverse(scope);
		// System.out.println("bprime = " + bprime);
		GamaObjectMatrix c = (GamaObjectMatrix) aprime.opAppendVertically(scope, bprime);
		// System.out.println("c = " + c);
		GamaObjectMatrix cprime = c._reverse(scope);
		// System.out.println("cprime = " + cprime);
		return cprime;
	}

	// @Override
	// public Double _max(final IScope scope) {
	// Double max = -Double.MAX_VALUE;
	// for ( int i = 0; i < matrix.length; i++ ) {
	// Object o = matrix[i];
	// if ( o instanceof Number && ((Number) o).doubleValue() > max ) {
	// max = Double.valueOf(((Number) o).doubleValue());
	// }
	// }
	// return max;
	// }
	//
	// @Override
	// public Double _min(final IScope scope) {
	// Double min = Double.MAX_VALUE;
	// for ( int i = 0; i < matrix.length; i++ ) {
	// Object o = matrix[i];
	// if ( o instanceof Number && ((Number) o).doubleValue() < min ) {
	// min = Double.valueOf(((Number) o).doubleValue());
	// }
	// }
	// return min;
	// }
	//
	// @Override
	// public Double _product(final IScope scope) {
	// Double result = 1.0;
	// for ( int i = 0, n = matrix.length; i < n; i++ ) {
	// Object d = matrix[i];
	// if ( d instanceof Number ) {
	// result *= ((Number) d).doubleValue();
	// }
	// }
	// return result;
	// }
	//
	// @Override
	// public Double _sum(final IScope scope) {
	// Double result = 0.0;
	// for ( int i = 0, n = matrix.length; i < n; i++ ) {
	// Object d = matrix[i];
	// if ( d instanceof Number ) {
	// result += ((Number) d).doubleValue();
	// }
	// }
	// return result;
	// }
	//
	@Override
	public boolean _isEmpty(final IScope scope) {
		for ( int i = 0; i < getMatrix().length; i++ ) {
			if ( getMatrix()[i] != null ) { return false; }
		}
		return true;
	}

	@Override
	protected IList _listValue(final IScope scope, final IType contentsType, final boolean cast) {
		return cast ? GamaListFactory.create(scope, contentsType, getMatrix())
			: GamaListFactory.createWithoutCasting(contentsType, getMatrix());
	}

	@Override
	protected IMatrix _matrixValue(final IScope scope, final ILocation preferredSize, final IType type,
		final boolean copy) {
		return GamaMatrixType.from(scope, this, type, preferredSize, copy);
	}

	@Override
	public GamaObjectMatrix _reverse(final IScope scope) throws GamaRuntimeException {
		final GamaObjectMatrix result = new GamaObjectMatrix(numRows, numCols, getType().getContentType());
		for ( int i = 0; i < numCols; i++ ) {
			for ( int j = 0; j < numRows; j++ ) {
				result.set(scope, j, i, get(scope, i, j));
			}
		}
		return result;
	}

	@Override
	public GamaObjectMatrix copy(final IScope scope, final ILocation size, final boolean copy) {
		if ( size == null ) {
			if ( copy ) {
				return new GamaObjectMatrix(numCols, numRows, Arrays.copyOf(matrix, matrix.length),
					getType().getContentType());
			} else {
				return this;
			}
		}
		return new GamaObjectMatrix((int) size.getX(), (int) size.getX(), Arrays.copyOf(matrix, matrix.length),
			getType().getContentType());
	}

	@Override
	public boolean equals(final Object m) {
		if ( this == m ) { return true; }
		if ( !(m instanceof GamaObjectMatrix) ) { return false; }
		final GamaObjectMatrix mat = (GamaObjectMatrix) m;
		return Arrays.equals(this.getMatrix(), mat.getMatrix());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public void _putAll(final IScope scope, final Object o) {
		fillWith(scope, getType().getContentType().cast(scope, o, null, false));
	}

	public void fillWith(final IScope scope, final Object o) {
		// We copy the element with which to fill the matrix if it is a (possibly) complex value
		// WARNING TODO WHY ???
		// if ( o instanceof IValue ) {
		// IValue v = (IValue) o;
		// for ( int i = 0; i < matrix.length; i++ ) {
		// matrix[i] = v.copy(scope);
		// }
		// } else {
		Arrays.fill(getMatrix(), o);
		// }
	}

	@Override
	public Object get(final IScope scope, final int col, final int row) {
		if ( col >= numCols || col < 0 || row >= numRows || row < 0 ) { return null; }
		return getMatrix()[row * numCols + col];
	}

	@Override
	public void set(final IScope scope, final int col, final int row, final Object obj) {
		if ( col >= numCols || col < 0 || row >= numRows || row < 0 ) { return; }
		getMatrix()[row * numCols + col] = GamaType.toType(scope, obj, getType().getContentType(), false);
	}

	@Override
	public Object remove(final IScope scope, final int col, final int row) {
		if ( col >= numCols || col < 0 || row >= numRows || row < 0 ) { return null; }
		final Object o = getMatrix()[row * numCols + col];
		getMatrix()[row * numCols + col] = null;
		return o;
	}

	@Override
	public boolean _removeFirst(final IScope scope, final Object o) {
		for ( int i = 0; i < getMatrix().length; i++ ) {
			if ( getMatrix()[i].equals(o) ) {
				getMatrix()[i] = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean _removeAll(final IScope scope, final IContainer<?, Object> list) throws GamaRuntimeException {
		boolean removed = false;
		for ( int i = 0; i < getMatrix().length; i++ ) {
			if ( list.contains(scope, getMatrix()[i]) ) { // VERIFY NULL SCOPE
				getMatrix()[i] = null;
				removed = true;
			}
		}
		// TODO Make a test to verify the return
		return removed;
	}

	@Override
	public void shuffleWith(final RandomUtils randomAgent) {
		setMatrix(randomAgent.shuffle(getMatrix()));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(numRows * numCols * 5);
		sb.append('[');
		GAMA.run(new InScope.Void() {

			@Override
			public void process(final IScope scope) {
				for ( int row = 0; row < numRows; row++ ) {
					for ( int col = 0; col < numCols; col++ ) {
						sb.append(get(scope, col, row));
						if ( col < numCols - 1 ) {
							sb.append(',');
						}
					}
					if ( row < numRows - 1 ) {
						sb.append(';');
					}
				}
			}
		});

		sb.append(']');
		return sb.toString();
	}

	/**
	 * Method iterator()
	 * @see msi.gama.util.matrix.GamaMatrix#iterator()
	 */
	@Override
	public java.lang.Iterable<Object> iterable(final IScope scope) {
		return ImmutableList.copyOf(getMatrix());
	}

	public Object[] getMatrix() {
		return matrix;
	}

	protected void setMatrix(final Object[] matrix) {
		this.matrix = matrix;
	}

	/**
	 * Method getNthElement()
	 * @see msi.gama.util.matrix.GamaMatrix#getNthElement(java.lang.Integer)
	 */
	@Override
	protected Object getNthElement(final Integer index) {
		if ( index == null ) { return null; }
		if ( index > getMatrix().length ) { return null; }
		return getMatrix()[index];
	}

}
