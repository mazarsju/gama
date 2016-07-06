/*********************************************************************************************
 *
 *
 * 'GamaGridFile.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.PrjFileReader;
import org.geotools.factory.Hints;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.metamodel.shape.IShape;
import msi.gama.precompiler.GamlAnnotations.file;
import msi.gama.precompiler.IConcept;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IList;
import msi.gaml.operators.Spatial;
import msi.gaml.types.GamaGeometryType;
import msi.gaml.types.IType;
import msi.gaml.types.Types;

@file(name = "grid", extensions = { "asc",
		"tif" }, buffer_type = IType.LIST, buffer_content = IType.GEOMETRY, buffer_index = IType.INT, concept = {
				IConcept.GRID, IConcept.ASC, IConcept.TIF, IConcept.FILE })
public class GamaGridFile extends GamaGisFile {

	private GamaGridReader reader;
	private GridCoverage2D coverage;
	public int nbBands;

	@Override
	public IList<String> getAttributes(final IScope scope) {
		// No attributes
		return GamaListFactory.create();
	}

	private GamaGridReader createReader(final IScope scope, final boolean fillBuffer) {
		if (reader == null) {
			final File gridFile = getFile();
			gridFile.setReadable(true);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(gridFile);
			} catch (final FileNotFoundException e) {
				// Should not happen;
			}
			try {
				reader = new GamaGridReader(scope, fis, fillBuffer);
			} catch (final GamaRuntimeException e) {
				if (isTiff()) {
					final GamaRuntimeException ex = GamaRuntimeException.error(
							"The format of " + getFile().getName() + " is not correct. Error: " + e.getMessage(),
							scope);
					ex.addContext("for file " + getFile().getPath());
					throw ex;

				}
				// A problem appeared, likely related to the wrong format of the
				// file (see Issue 412)
				GAMA.reportError(scope,
						GamaRuntimeException.warning(
								"The format of " + getFile().getName() + " is incorrect. Attempting to read it anyway.",
								scope),
						false);
				final StringBuilder text = new StringBuilder();
				final String NL = System.getProperty("line.separator");
				Scanner scanner = null;

				try {
					scanner = new Scanner(getFile());
					final int cpt = 0;
					while (scanner.hasNextLine()) {
						final String line = scanner.nextLine();
						if (cpt < 10) {
							if (line.contains("dx")) {
								text.append(line.replace("dx", "cellsize") + NL);
							} else if (line.contains("dy")) {
								continue;
							} else {
								text.append(line + NL);
							}
						} else {
							text.append(line + NL);
						}
					}
				} catch (final FileNotFoundException e2) {
					final GamaRuntimeException ex = GamaRuntimeException.error(
							"The format of " + getFile().getName() + " is not correct. Error: " + e2.getMessage(),
							scope);
					ex.addContext("for file " + getFile().getPath());
					throw ex;
				} finally {
					if (scanner != null) {
						scanner.close();
					}
				}

				text.append(NL);
				// fis = new StringBufferInputStream(text.toString());
				reader = new GamaGridReader(scope, new StringBufferInputStream(text.toString()), fillBuffer);
			}
		}
		return reader;
	}

	class GamaGridReader {

		int numRows, numCols;
		IShape geom;

		GamaGridReader(final IScope scope, final InputStream fis, final boolean fillBuffer)
				throws GamaRuntimeException {
			setBuffer(GamaListFactory.<IShape> create(Types.GEOMETRY));
			AbstractGridCoverage2DReader store = null;
			try {
				if (fillBuffer) {
					scope.getGui().getStatus().beginSubStatus("Reading file " + getName());
				}
				// Necessary to compute it here, because it needs to be passed
				// to the Hints
				final CoordinateReferenceSystem crs = getExistingCRS(scope);
				if (isTiff()) {
					if (crs == null) {
						store = new GeoTiffReader(getFile(), new Hints(Hints.USE_JAI_IMAGEREAD, true));
					} else {
						store = new GeoTiffReader(getFile(), new Hints(Hints.USE_JAI_IMAGEREAD, true,
								Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs));
					}
				} else {
					if (crs == null) {
						store = new ArcGridReader(fis, new Hints(Hints.USE_JAI_IMAGEREAD, true));
					} else {
						store = new ArcGridReader(fis, new Hints(Hints.USE_JAI_IMAGEREAD, true,
								Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs));
					}
				}
				final GeneralEnvelope genv = store.getOriginalEnvelope();
				numRows = store.getOriginalGridRange().getHigh(1) + 1;
				numCols = store.getOriginalGridRange().getHigh(0) + 1;
				final Envelope env = new Envelope(genv.getMinimum(0), genv.getMaximum(0), genv.getMinimum(1),
						genv.getMaximum(1));
				computeProjection(scope, env);
				final Envelope envP = gis.getProjectedEnvelope();
				final double cellHeight = envP.getHeight() / numRows;
				final double cellWidth = envP.getWidth() / numCols;
				final IList<IShape> shapes = GamaListFactory.create(Types.GEOMETRY);
				final double originX = envP.getMinX();
				final double originY = envP.getMinY();
				final double maxY =envP.getMaxY();
				final double maxX = envP.getMaxX();
				shapes.add(new GamaPoint(originX, originY));
				shapes.add(new GamaPoint(maxX, originY));
				shapes.add(new GamaPoint(maxX, maxY));
				shapes.add(new GamaPoint(originX, maxY));
				shapes.add(shapes.get(0));
				geom = GamaGeometryType.buildPolygon(shapes);
				if (!fillBuffer) {
					return;
				}

				final GamaPoint p = new GamaPoint(0, 0);
				coverage = store.read(null);
				final double cmx = cellWidth / 2;
				final double cmy = cellHeight / 2;
				boolean doubleValues = false;
				boolean floatValues = false;
				boolean intValues = false;
				boolean longValues = false;
				boolean byteValues = false;
				final double cellHeightP = genv.getSpan(1) / numRows;
				final double cellWidthP = genv.getSpan(0) / numCols;
				final double originXP = genv.getMinimum(0);
				final double maxYP = genv.getMaximum(1);
				final double cmxP = cellWidthP / 2;
				final double cmyP = cellHeightP / 2;
			
				for (int i = 0, n = numRows * numCols; i < n; i++) {
					scope.getGui().getStatus().setSubStatusCompletion(i / (double) n);
					final int yy = i / numCols;
					final int xx = i - yy * numCols;
					p.x = originX + xx * cellWidth + cmx;
					p.y = maxY - (yy * cellHeight + cmy);
					GamaShape rect = (GamaShape) GamaGeometryType.buildRectangle(cellWidth, cellHeight, p);
					final Object vals = coverage
							.evaluate(new DirectPosition2D(originXP + xx * cellWidthP + cmxP, maxYP - (yy * cellHeightP + cmyP)));
					if (i == 0) {
						doubleValues = vals instanceof double[];
						intValues = vals instanceof int[];
						byteValues = vals instanceof byte[];
						longValues = vals instanceof long[];
						floatValues = vals instanceof float[];
					}
					if (gis == null) {
						rect = new GamaShape(rect.getInnerGeometry());
					} else {
						rect = new GamaShape(gis.transform(rect.getInnerGeometry()));
					}
					rect.getOrCreateAttributes();
					if (doubleValues) {
						final double[] vd = (double[]) vals;
						if (i == 0)
							nbBands = vd.length;
						rect.getAttributes().put("grid_value", vd[0]);
						rect.getAttributes().put("bands", GamaListFactory.create(scope, Types.FLOAT, vd));
					} else if (intValues) {
						final int[] vi = (int[]) vals;
						if (i == 0)
							nbBands = vi.length;
						final double v = Double.valueOf(vi[0]);
						rect.getAttributes().put("grid_value", v);
						rect.getAttributes().put("bands", GamaListFactory.create(scope, Types.FLOAT, vi));
					} else if (longValues) {
						final long[] vi = (long[]) vals;
						if (i == 0)
							nbBands = vi.length;
						final double v = Double.valueOf(vi[0]);
						rect.getAttributes().put("grid_value", v);
						rect.getAttributes().put("bands", GamaListFactory.create(scope, Types.FLOAT, vi));
					} else if (floatValues) {
						final float[] vi = (float[]) vals;
						if (i == 0)
							nbBands = vi.length;
						final double v = Double.valueOf(vi[0]);
						rect.getAttributes().put("grid_value", v);
						rect.getAttributes().put("bands", GamaListFactory.create(scope, Types.FLOAT, vi));
					} else if (byteValues) {
						final byte[] bv = (byte[]) vals;
						if (i == 0)
							nbBands = bv.length;
						if (bv.length == 1) {
							final double v = Double.valueOf(((byte[]) vals)[0]);
							rect.getAttributes().put("grid_value", v);
						} else if (bv.length == 3) {
							final int red = bv[0] < 0 ? 256 + bv[0] : bv[0];
							final int green = bv[0] < 0 ? 256 + bv[1] : bv[1];
							final int blue = bv[0] < 0 ? 256 + bv[2] : bv[2];
							rect.getAttributes().put("grid_value", (red + green + blue) / 3.0);
						}
						rect.getAttributes().put("bands", GamaListFactory.create(scope, Types.FLOAT, bv));
					}
					((IList) getBuffer()).add(rect);
				}
			} catch (final Exception e) {
				final GamaRuntimeException ex = GamaRuntimeException.error(
						"The format of " + getFile().getName() + " is not correct. Error: " + e.getMessage(), scope);
				ex.addContext("for file " + getFile().getPath());
				throw ex;
			} finally {
				if (store != null) {
					store.dispose();
				}
				scope.getGui().getStatus().endSubStatus("Opening file " + getName());
			}
		}

	}

	public GamaGridFile(final IScope scope, final String pathName) throws GamaRuntimeException {
		super(scope, pathName, (Integer) null);
	}

	public GamaGridFile(final IScope scope, final String pathName, final Integer code) throws GamaRuntimeException {
		super(scope, pathName, code);
	}

	public GamaGridFile(final IScope scope, final String pathName, final String code) throws GamaRuntimeException {
		super(scope, pathName, code);
	}

	@Override
	public Envelope computeEnvelope(final IScope scope) {
		fillBuffer(scope);
		return gis.getProjectedEnvelope();
	}

	public Envelope computeEnvelopeWithoutBuffer(final IScope scope) {
		if (gis == null) {
			final GamaGridReader rd = createReader(scope, false);
			if (rd == null) {
				return null;
			}
		}
		return gis.getProjectedEnvelope();
	}

	@Override
	protected void fillBuffer(final IScope scope) throws GamaRuntimeException {
		if (getBuffer() != null) {
			return;
		}
		createReader(scope, true);
	}

	@Override
	protected void flushBuffer() throws GamaRuntimeException {
		// TODO at least, save as ASCII grid (plain text)

	}

	public int getNbRows(final IScope scope) {
		return createReader(scope, true).numRows;
	}

	public int getNbCols(final IScope scope) {
		return createReader(scope, true).numCols;
	}

	@Override
	public String getExtension() {
		final String path = getFile().getPath().toLowerCase();
		final int mid = path.lastIndexOf(".");
		if (mid == -1) {
			return "";
		}
		return path.substring(mid + 1, path.length());
	}

	public boolean isTiff() {
		return getExtension().equals("tif");
	}

	@Override
	public IShape getGeometry(final IScope scope) {
		return createReader(scope, true).geom;
	}

	@Override
	protected CoordinateReferenceSystem getOwnCRS() {
		final File source = getFile();
		// check to see if there is a projection file
		// getting name for the prj file
		final String sourceAsString;
		sourceAsString = source.getAbsolutePath();
		final int index = sourceAsString.lastIndexOf(".");
		final StringBuffer prjFileName;
		if (index == -1) {
			prjFileName = new StringBuffer(sourceAsString);
		} else {
			prjFileName = new StringBuffer(sourceAsString.substring(0, index));
		}
		prjFileName.append(".prj");

		// does it exist?
		final File prjFile = new File(prjFileName.toString());
		FileInputStream fip = null;
		if (prjFile.exists()) {
			// it exists then we have to read it
			PrjFileReader projReader = null;
			try {
				fip = new FileInputStream(prjFile);
				final FileChannel channel = fip.getChannel();
				projReader = new PrjFileReader(channel);
				return projReader.getCoordinateReferenceSystem();
			} catch (final FileNotFoundException e) {
				// warn about the error but proceed, it is not fatal
				// we have at least the default crs to use
				return null;
			} catch (final IOException e) {
				// warn about the error but proceed, it is not fatal
				// we have at least the default crs to use
				return null;
			} catch (final FactoryException e) {
				// warn about the error but proceed, it is not fatal
				// we have at least the default crs to use
				return null;
			} finally {
				if (projReader != null) {
					try {
						projReader.close();
					} catch (final IOException e) {
						// warn about the error but proceed, it is not fatal
						// we have at least the default crs to use
						return null;
					}
					if (fip != null) {
						try {
							fip.close();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else if (isTiff()) {
			try {
				GeoTiffReader store = new GeoTiffReader(getFile(), new Hints(Hints.USE_JAI_IMAGEREAD, true));
				return store.getCoordinateReferenceSystem();
			} catch (DataSourceException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	// public static RenderedImage getImage(final String pathName) {
	// return GAMA.run(new InScope<RenderedImage>() {
	//
	// @Override
	// public RenderedImage run(final IScope scope) {
	// GamaGridFile file = new GamaGridFile(scope, pathName);
	// file.createReader(scope, true);
	// return file.coverage.getRenderedImage();
	// }
	// });
	// }

	@Override
	public void invalidateContents() {
		super.invalidateContents();
		reader = null;
		if (coverage != null) {
			coverage.dispose(true);
		}
		coverage = null;
	}

}
