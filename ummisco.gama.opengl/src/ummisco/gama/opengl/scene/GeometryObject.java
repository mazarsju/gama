/*********************************************************************************************
 *
 *
 * 'GeometryObject.java', in plugin 'msi.gama.jogl2', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gama.opengl.scene;

import com.vividsolutions.jts.geom.Geometry;

import msi.gama.metamodel.shape.IShape;
import msi.gama.util.GamaColor;
import msi.gaml.statements.draw.DrawingAttributes;
import msi.gaml.statements.draw.ShapeDrawingAttributes;
import ummisco.gama.webgl.SimpleGeometryObject;

public class GeometryObject extends AbstractObject {

	public final Geometry geometry;

	public GeometryObject(final Geometry geometry, final DrawingAttributes attributes, final LayerObject layer) {
		super(attributes, layer);
		this.geometry = geometry;
	}

	// Package protected as it is only used by the static layers
	GeometryObject(final IShape geometry, final GamaColor color, final IShape.Type type, final LayerObject layer) {
		this(geometry, color, true, type, layer);
	}

	GeometryObject(final IShape geometry, final GamaColor color, final boolean filled, final IShape.Type type,
			final LayerObject layer) {
		this(geometry.getInnerGeometry(), new ShapeDrawingAttributes(geometry, filled ? color : null, color), layer);
	}

	public IShape.Type getType() {
		if (!(attributes instanceof ShapeDrawingAttributes)) {
			return null;
		}
		return ((ShapeDrawingAttributes) attributes).type;
	}

	@Override
	public boolean isFilled() {
		return super.isFilled() && getType() != IShape.Type.GRIDLINE;
	}

	public SimpleGeometryObject toSimpleGeometryObject() {
		return new SimpleGeometryObject(geometry, getColor(), this.getBorder(), attributes.getDepth(),
				attributes.rotation, getLocation(), attributes.size, getType(), !isFilled(), attributes.getTextures());
	}

}
