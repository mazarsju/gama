/*********************************************************************************************
 *
 *
 * 'AWTDisplayGraphics.java', in plugin 'msi.gama.application', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/

package msi.gama.outputs.display;

import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_COLOR_RENDERING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_COLOR_RENDER_SPEED;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_RENDER_SPEED;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.vividsolutions.jts.awt.PointTransformation;
import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Lineal;
import com.vividsolutions.jts.geom.Puntal;

import msi.gama.common.interfaces.IDisplaySurface;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.metamodel.shape.IShape;
import msi.gama.outputs.layers.OverlayLayer;
import msi.gama.runtime.IScope;
import msi.gama.util.GamaColor;
import msi.gama.util.GamaPair;
import msi.gama.util.file.GamaFile;
import msi.gama.util.file.GamaGeometryFile;
import msi.gama.util.file.GamaImageFile;
import msi.gaml.operators.Cast;
import msi.gaml.operators.Maths;
import msi.gaml.operators.fastmaths.FastMath;
import msi.gaml.statements.draw.FieldDrawingAttributes;
import msi.gaml.statements.draw.FileDrawingAttributes;
import msi.gaml.statements.draw.ShapeDrawingAttributes;
import msi.gaml.statements.draw.TextDrawingAttributes;

/**
 *
 * Simplifies the drawing of circles, rectangles, and so forth. Rectangles are
 * generally faster to draw than circles. The Displays should take care of
 * layouts while objects that wish to be drawn as a shape need only call the
 * appropriate method.
 * <p>
 *
 * 29/04/2013: Deep revision to simplify the interface due to the changes in
 * draw/aspects
 *
 * @author Nick Collier, Alexis Drogoul, Patrick Taillandier
 * @version $Revision: 1.13 $ $Date: 2010-03-19 07:12:24 $
 */

public class AWTDisplayGraphics extends AbstractDisplayGraphics implements PointTransformation {

	private Graphics2D currentRenderer, overlayRenderer, normalRenderer;
	private final ShapeWriter sw = new ShapeWriter(this);
	private static final Font defaultFont = new Font("Helvetica", Font.BOLD, 12);

	static {

		QUALITY_RENDERING.put(KEY_RENDERING, VALUE_RENDER_QUALITY);
		QUALITY_RENDERING.put(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
		QUALITY_RENDERING.put(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
		QUALITY_RENDERING.put(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
		QUALITY_RENDERING.put(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

		MEDIUM_RENDERING.put(KEY_RENDERING, VALUE_RENDER_QUALITY);
		MEDIUM_RENDERING.put(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_SPEED);
		MEDIUM_RENDERING.put(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
		MEDIUM_RENDERING.put(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
		MEDIUM_RENDERING.put(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

		SPEED_RENDERING.put(KEY_RENDERING, VALUE_RENDER_SPEED);
		SPEED_RENDERING.put(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_SPEED);
		SPEED_RENDERING.put(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_SPEED);
		SPEED_RENDERING.put(KEY_INTERPOLATION, VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		SPEED_RENDERING.put(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

	}

	public AWTDisplayGraphics(final IDisplaySurface surface, final Graphics2D g2) {
		super(surface);
		setGraphics2D(g2);

	}

	@Override
	public void dispose() {
		super.dispose();
		if (currentRenderer != null)
			currentRenderer.dispose();
		if (normalRenderer != null)
			normalRenderer.dispose();
		if (overlayRenderer != null)
			overlayRenderer.dispose();
	}

	@Override
	public boolean beginDrawingLayers() {
		currentRenderer.setRenderingHints(data.isAntialias() ? QUALITY_RENDERING : SPEED_RENDERING);
		return true;
	}

	@Override
	public void setOpacity(final double alpha) {
		super.setOpacity(alpha);
		currentRenderer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
	}

	/**
	 * Implements PointTransformation.transform
	 * 
	 * @see com.vividsolutions.jts.awt.PointTransformation#transform(com.vividsolutions.jts.geom.Coordinate,
	 *      java.awt.geom.Point2D)
	 */
	@Override
	public void transform(final Coordinate c, final Point2D p) {
		p.setLocation(xFromModelUnitsToPixels(c.x), yFromModelUnitsToPixels(c.y));
	}

	@Override
	public Rectangle2D drawField(final double[] fieldValues, final FieldDrawingAttributes attributes) {
		if (attributes.textures == null) {
			return null;
		}
		final Object image = attributes.textures.get(0);
		if (image instanceof GamaFile) {
			return drawFile((GamaFile) image, attributes);
		}
		if (image instanceof BufferedImage) {
			return drawImage((BufferedImage) image, attributes);
		}
		return null;
	}

	@Override
	public Rectangle2D drawFile(final GamaFile file, final FileDrawingAttributes attributes) {
		final IScope scope = surface.getDisplayScope();
		if (file instanceof GamaImageFile) {
			return drawImage(((GamaImageFile) file).getImage(scope), attributes);
		}
		if (!(file instanceof GamaGeometryFile)) {
			return null;
		}
		IShape shape = Cast.asGeometry(scope, file);
		if (shape == null) {
			return null;
		}
		final GamaPair<Double, GamaPoint> rot = attributes.rotation;
		final Double rotation = rot == null ? null : rot.key;
		// System.out.println("Old centroid " +
		// shape.getInnerGeometry().getCentroid());
		// shape = shape.translatedTo(scope, attributes.location);
		// System.out.println("Centroid after translation" +
		// shape.getInnerGeometry().getCentroid());
		shape = new GamaShape(shape, null, rotation, attributes.location, attributes.size, true);
		// shape = new GamaShape(shape, null, rotation, attributes.location);
		// System.out.println("New centroid " +
		// shape.getInnerGeometry().getCentroid());
		final GamaColor c = attributes.color;
		return drawShape(shape, new ShapeDrawingAttributes(new GamaPoint((Coordinate) shape.getLocation()), c, c));
	}

	@Override
	public Rectangle2D drawImage(final BufferedImage img, final FileDrawingAttributes attributes) {
		final AffineTransform saved = currentRenderer.getTransform();
		double curX, curY;
		if (attributes.location == null) {
			curX = getXOffsetInPixels();
			curY = getYOffsetInPixels();
		} else {
			curX = xFromModelUnitsToPixels(attributes.location.getX());
			curY = yFromModelUnitsToPixels(attributes.location.getY());
		}
		double curWidth, curHeight;
		if (attributes.size == null) {
			curWidth = getLayerWidth();
			curHeight = getLayerHeight();
		} else {
			curWidth = wFromModelUnitsToPixels(attributes.size.getX());
			curHeight = hFromModelUnitsToPixels(attributes.size.getY());
		}
		if (attributes.rotation != null && attributes.rotation.key != null) {
			currentRenderer.rotate(Maths.toRad * attributes.rotation.key, curX + curWidth / 2d, curY + curHeight / 2d);
		}
		currentRenderer.drawImage(img, (int) FastMath.round(curX), (int) FastMath.round(curY), (int) curWidth,
				(int) curHeight, null);
		if (attributes.border != null) {
			drawGridLine(img, attributes.border);
		}
		currentRenderer.setTransform(saved);
		rect.setRect(curX, curY, curWidth, curHeight);
		if (highlight) {
			highlightRectangleInPixels(rect);
		}
		return rect.getBounds2D();
	}

	/**
	 * Method drawString.
	 * 
	 * @param string
	 *            String
	 * @param stringColor
	 *            Color
	 * @param angle
	 *            Integer
	 * @param z
	 *            float (has no effect in 2D)
	 */

	@Override
	public Rectangle2D drawString(final String string, final TextDrawingAttributes attributes) {
		// Multiline: Issue #780
		if (string.contains("\n")) {
			final Rectangle2D result = new Rectangle2D.Double();
			for (final String s : string.split("\n")) {
				final Rectangle2D r = drawString(s, attributes);
				attributes.location.setY(attributes.location.getY() + r.getHeight());
				result.add(r);
			}
			return result;
		}
		currentRenderer.setColor(highlight ? data.getHighlightColor() : attributes.color);
		double curX, curY;
		// final double curZ;
		if (attributes.location == null) {
			curX = getXOffsetInPixels();
			curY = getYOffsetInPixels();
			// curZ = 0;
		} else {
			curX = xFromModelUnitsToPixels(attributes.location.getX());
			curY = yFromModelUnitsToPixels(attributes.location.getY());
		}
		currentRenderer.setFont(attributes.font);
		final AffineTransform saved = currentRenderer.getTransform();
		if (attributes.rotation != null && attributes.rotation.key != null) {
			final Rectangle2D r = currentRenderer.getFontMetrics().getStringBounds(string, currentRenderer);
			currentRenderer.rotate(Maths.toRad * attributes.rotation.key, curX + r.getWidth() / 2,
					curY + r.getHeight() / 2);
		}
		currentRenderer.drawString(string, (int) curX, (int) curY);
		currentRenderer.setTransform(saved);
		final Rectangle2D result = currentRenderer.getFontMetrics().getStringBounds(string, currentRenderer);
		result.setFrame(curX, curY, result.getWidth(), result.getHeight());
		return result;
	}

	@Override
	public Rectangle2D drawShape(final IShape geometry, final ShapeDrawingAttributes attributes) {
		if (geometry == null) {
			return null;
		}
		if (highlight) {
			attributes.color = GamaColor.getInt(data.getHighlightColor().getRGB());
			if (attributes.border != null)
				attributes.border = attributes.color;
		}
		final Geometry geom = geometry.getInnerGeometry();
		final Shape s = sw.toShape(geom);
		try {
			final Rectangle2D r = s.getBounds2D();
			currentRenderer.setColor(attributes.color);
			if (geom instanceof Lineal || geom instanceof Puntal ? false : !attributes.empty) {
				currentRenderer.fill(s);
				if (attributes.border != null) {
					currentRenderer.setColor(attributes.border);
				}
			}
			if (attributes.border != null) {
				currentRenderer.draw(s);
			}
			return r;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void fillBackground(final Color bgColor, final double opacity) {
		setOpacity(opacity);
		currentRenderer.setColor(bgColor);
		currentRenderer.fillRect(0, 0, (int) surface.getDisplayWidth(), (int) surface.getDisplayHeight());
	}

	public void drawGridLine(final BufferedImage image, final Color lineColor) {

		// The image contains the dimensions of the grid.
		final double stepx = (double) getLayerWidth() / (double) image.getWidth();
		final double stepy = (double) getLayerHeight() / (double) image.getHeight();
		if (stepx < 2 || stepy < 2) {
			return;
		}
		final Line2D line = new Line2D.Double();
		currentRenderer.setColor(lineColor);
		for (double step = 0.0, end = getLayerWidth(); step < end + 1; step += stepx) {
			line.setLine(getXOffsetInPixels() + step, getYOffsetInPixels(), getXOffsetInPixels() + step,
					getYOffsetInPixels() + getLayerHeight());
			currentRenderer.draw(line);
		}
		line.setLine(getXOffsetInPixels() + getLayerWidth() - 1, getYOffsetInPixels(),
				getXOffsetInPixels() + getLayerWidth() - 1, getYOffsetInPixels() + getLayerHeight() - 1);
		currentRenderer.draw(line);

		for (double step = 0.0, end = getLayerHeight(); step < end + 1; step += stepy) {
			line.setLine(getXOffsetInPixels(), getYOffsetInPixels() + step, getXOffsetInPixels() + getLayerWidth(),
					getYOffsetInPixels() + step);
			currentRenderer.draw(line);
		}
		line.setLine(getXOffsetInPixels(), getYOffsetInPixels() + getLayerHeight() - 1,
				getXOffsetInPixels() + getLayerWidth() - 1, getYOffsetInPixels() + getLayerHeight() - 1);
		currentRenderer.draw(line);

	}

	private void highlightRectangleInPixels(final Rectangle2D r) {
		if (r == null) {
			return;
		}
		final Stroke oldStroke = currentRenderer.getStroke();
		currentRenderer.setStroke(new BasicStroke(5));
		final Color old = currentRenderer.getColor();
		currentRenderer.setColor(data.getHighlightColor());
		currentRenderer.draw(r);
		currentRenderer.setStroke(oldStroke);
		currentRenderer.setColor(old);
	}

	/**
	 * Method is2D()
	 * 
	 * @see msi.gama.common.interfaces.IGraphics#is2D()
	 */
	@Override
	public boolean is2D() {
		return true;
	}

	@Override
	public void beginOverlay(final OverlayLayer layer) {
		currentRenderer = overlayRenderer;
		currentRenderer.setColor(layer.getBackground());
		if (layer.isRounded()) {
			currentRenderer.fillRoundRect((int) getXOffsetInPixels(), (int) getYOffsetInPixels(), getLayerWidth(),
					getLayerHeight(), 10, 10);
		} else {
			currentRenderer.fillRect((int) getXOffsetInPixels(), (int) getYOffsetInPixels(), getLayerWidth(),
					getLayerHeight());
		}
		if (layer.getBorder() != null) {
			currentRenderer.setColor(layer.getBorder());
			if (layer.isRounded()) {
				currentRenderer.drawRoundRect((int) getXOffsetInPixels(), (int) getYOffsetInPixels(), getLayerWidth(),
						getLayerHeight(), 10, 10);
			} else {
				currentRenderer.drawRect((int) getXOffsetInPixels(), (int) getYOffsetInPixels(), getLayerWidth(),
						getLayerHeight());
			}
		}
	}

	@Override
	public void endOverlay() {
		currentRenderer = normalRenderer;
	}

	public void setGraphics2D(final Graphics2D g) {
		normalRenderer = g;
		currentRenderer = g;
		if (g != null) {
			g.setFont(defaultFont);
		}
	}

	public void setUntranslatedGraphics2D(final Graphics2D g) {
		overlayRenderer = g;
	}

	@Override
	public boolean cannotDraw() {
		return false;
	}

	@Override
	public ILocation getCameraPos() {
		return GamaPoint.NULL_POINT;
	}

	@Override
	public ILocation getCameraTarget() {
		return GamaPoint.NULL_POINT;
	}

	@Override
	public ILocation getCameraOrientation() {
		return GamaPoint.NULL_POINT;
	}

}
