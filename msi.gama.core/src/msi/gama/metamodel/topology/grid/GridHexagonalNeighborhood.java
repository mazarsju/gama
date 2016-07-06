/**
 *
 */
package msi.gama.metamodel.topology.grid;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;

public class GridHexagonalNeighborhood extends GridNeighborhood {



	/**
	 * @param gamaSpatialMatrix
	 */
	GridHexagonalNeighborhood(final GamaSpatialMatrix matrix) {
		super(matrix);
	}

	final int getIndexAt(int x, int y, final int xSize, final int ySize, final boolean isTorus) {
		if ( x < 0 || y < 0 || x > xSize - 1 || y > ySize - 1 ) {
			if ( !isTorus ) { return -1; }
			if ( x < 0 ) {
				x = xSize - 1;
			}
			if ( y < 0 ) {
				y = ySize - 1;
			}
			if ( x > xSize - 1 ) {
				x = 0;
			}
			if ( y > ySize - 1 ) {
				y = 0;
			}
		}
		return y * xSize + x;
	}

	public TIntHashSet getNeighborsAtRadius(final int placeIndex, final int radius, final int xSize,
		final int ySize, final boolean isTorus) {
		final TIntHashSet currentNeigh = new TIntHashSet();
		currentNeigh.add(placeIndex);
		for ( int i = 0; i < radius; i++ ) {
			final TIntHashSet newNeigh = new TIntHashSet();
			TIntIterator it = currentNeigh.iterator();
			while (it.hasNext()) {
				newNeigh.addAll(getNeighborsAtRadius1(it.next(), xSize, ySize, isTorus));
			}
			currentNeigh.addAll(newNeigh);
		}
		currentNeigh.remove(placeIndex);
		return currentNeigh;

	}

	public TIntHashSet getNeighborsAtRadius1(final int placeIndex, final int xSize, final int ySize,
		final boolean isTorus) {
		final int y = placeIndex / xSize;
		final int x = placeIndex - y * xSize;
		final TIntHashSet neigh = new TIntHashSet();
		int id = getIndexAt(x, y - 1, xSize, ySize, isTorus);
		if ( id != -1 ) {
			neigh.add(id);
		}
		id = getIndexAt(x, y + 1, xSize, ySize, isTorus);
		if ( id != -1 ) {
			neigh.add(id);
		}
		id = getIndexAt(x - 1, y, xSize, ySize, isTorus);
		if ( id != -1 ) {
			neigh.add(id);
		}
		id = getIndexAt(x + 1, y, xSize, ySize, isTorus);
		if ( id != -1 ) {
			neigh.add(id);
		}
		if ( x % 2 == 0 ) {
			id = getIndexAt(x + 1, y - 1, xSize, ySize, isTorus);
			if ( id != -1 ) {
				neigh.add(id);
			}
			id = getIndexAt(x - 1, y - 1, xSize, ySize, isTorus);
			if ( id != -1 ) {
				neigh.add(id);
			}
		} else {
			id = getIndexAt(x + 1, y + 1, xSize, ySize, isTorus);
			if ( id != -1 ) {
				neigh.add(id);
			}
			id = getIndexAt(x - 1, y + 1, xSize, ySize, isTorus);
			if ( id != -1 ) {
				neigh.add(id);
			}
		}
		return neigh;
	}

	@Override
	protected TIntHashSet getNeighborsAtRadius(final int placeIndex, final int radius) {
		final TIntHashSet neigh2 = new TIntHashSet();
		final TIntHashSet neigh = getNeighborsAtRadius(placeIndex, radius, matrix.numCols, matrix.numRows, matrix.isTorus);
		TIntIterator it = neigh.iterator();
		while (it.hasNext()) {
			int id = it.next();
			if ( matrix.matrix[id] != null ) {
				neigh2.add(id);
			}
		}
		return neigh2;
	}

	@Override
	public boolean isVN() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see msi.gama.metamodel.topology.grid.INeighborhood#clear()
	 */
	@Override
	public void clear() {}

}