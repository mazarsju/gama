/*********************************************************************************************
 *
 *
 * 'GamaShapeFile.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.util.file;

import java.io.*;
import java.util.*;
import org.kabeja.dxf.*;
import org.kabeja.parser.*;
import com.vividsolutions.jts.geom.Envelope;
import msi.gama.metamodel.shape.*;
import msi.gama.precompiler.GamlAnnotations.file;
import msi.gama.precompiler.IConcept;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gaml.operators.*;
import msi.gaml.types.*;

/**
 * Written by drogoul
 * Modified on 13 nov. 2011
 *
 * @todo Description
 *
 */
@file(name = "dxf",
extensions = { "dxf" },
buffer_type = IType.LIST,
buffer_content = IType.GEOMETRY,
buffer_index = IType.INT,
concept = { IConcept.DXF, IConcept.FILE } )
public class GamaDXFFile extends GamaGeometryFile {

	GamaPoint size;
	Double unit;
	double x_t ;
	double y_t ;

	public GamaDXFFile(final IScope scope, final String pathName) throws GamaRuntimeException {
		super(scope, pathName);
	}

	public GamaDXFFile(final IScope scope, final String pathName, final Double unit) throws GamaRuntimeException {
		super(scope, pathName);
		this.unit = unit;
	}

	public GamaDXFFile(final IScope scope, final String pathName, final Double unit,final GamaPoint size) throws GamaRuntimeException {
		super(scope, pathName);
		this.unit = unit;
		this.size = size;
	}


	public GamaDXFFile(final IScope scope, final String pathName, final GamaPoint size) throws GamaRuntimeException {
		super(scope, pathName);
		this.size = size;
	}

	@Override
	protected IShape buildGeometry(final IScope scope) {
		return GamaGeometryType.geometriesToGeometry(scope, getBuffer());
	}

	@Override
	public IList<String> getAttributes(final IScope scope) {
		// TODO are there attributes ?
		return GamaListFactory.create();
	}




	public IShape createPolyline(final IScope scope, final IList pts) {
		if (pts.isEmpty()) {
			return null;
		}
		IShape shape =  GamaGeometryType.buildPolyline(pts);
		if (shape != null) {
			if ( size != null ) {
				return Spatial.Transformations.scaled_to(scope, shape, size);
			}
			return shape;
		}
		return null;
	}

	public IShape createPolygone(final IScope scope, final IList pts) {
		if (pts.isEmpty()) {
			return null;
		}
		IShape shape =  GamaGeometryType.buildPolygon(pts);
		if (shape != null) {
			if ( size != null ) {
				return Spatial.Transformations.scaled_to(scope, shape, size);
			}
			return shape;
		}
		return null;
	}

	public IShape createCircle(final IScope scope, final GamaPoint location, final double radius) {
		IShape shape =  GamaGeometryType.buildCircle(radius, location);
		if (shape != null) {
			if ( size != null ) {
				return Spatial.Transformations.scaled_to(scope, shape, size);
			}
			return shape;
		}
		return null;
	}


	public IShape manageObj(final IScope scope, final DXFSolid obj) {
		if (obj == null) {
			return null;
		}
		IList list = GamaListFactory.create(Types.POINT);
		list.add(new GamaPoint(obj.getPoint1().getX() * (unit == null ? 1 : unit)- x_t,obj.getPoint1().getY() * (unit == null ? 1 : unit) - y_t,obj.getPoint1().getZ()* (unit == null ? 1 : unit)));
		list.add(new GamaPoint(obj.getPoint2().getX() * (unit == null ? 1 : unit)- x_t,obj.getPoint2().getY() * (unit == null ? 1 : unit) - y_t,obj.getPoint2().getZ()* (unit == null ? 1 : unit)));
		list.add(new GamaPoint(obj.getPoint3().getX() * (unit == null ? 1 : unit)- x_t,obj.getPoint3().getY() * (unit == null ? 1 : unit) - y_t,obj.getPoint3().getZ()* (unit == null ? 1 : unit)));
		list.add(new GamaPoint(obj.getPoint4().getX() * (unit == null ? 1 : unit)- x_t,obj.getPoint4().getY() * (unit == null ? 1 : unit) - y_t,obj.getPoint4().getZ()* (unit == null ? 1 : unit)));

		IShape shape = createPolygone(scope,list);

		return shape;
	}

	public IShape manageObj(final IScope scope, final DXFCircle obj) {
		if (obj == null) {
			return null;
		}
		GamaPoint pt = new GamaPoint(obj.getCenterPoint().getX() * (unit == null ? 1 : unit) - x_t, obj.getCenterPoint().getY() * (unit == null ? 1 : unit) - y_t, obj.getCenterPoint().getZ()* (unit == null ? 1 : unit));
		return createCircle(scope,pt, obj.getRadius()* (unit == null ? 1 : unit));
	}

	public IShape manageObj(final IScope scope, final DXFLine obj) {
		if (obj == null) {
			return null;
		}
		IList list = GamaListFactory.create(Types.POINT);
		list.add(new GamaPoint(obj.getStartPoint().getX()* (unit == null ? 1 : unit) - x_t,obj.getStartPoint().getY() * (unit == null ? 1 : unit)- y_t,obj.getStartPoint().getZ()* (unit == null ? 1 : unit)));
		list.add(new GamaPoint(obj.getEndPoint().getX() * (unit == null ? 1 : unit)- x_t,obj.getEndPoint().getY() * (unit == null ? 1 : unit)- y_t,obj.getEndPoint().getZ()* (unit == null ? 1 : unit)));
		return createPolyline(scope,list);
	}
	public IShape manageObj(final IScope scope, final DXFArc obj) {
		if (obj == null) {
			return null;
		}
		IList list = GamaListFactory.create(Types.POINT);
		list.add(new GamaPoint(obj.getStartPoint().getX() * (unit == null ? 1 : unit)- x_t,obj.getStartPoint().getY()* (unit == null ? 1 : unit)- y_t,obj.getStartPoint().getZ()* (unit == null ? 1 : unit)));
		list.add(new GamaPoint(obj.getEndPoint().getX()* (unit == null ? 1 : unit) - x_t,obj.getEndPoint().getY() * (unit == null ? 1 : unit)- y_t,obj.getEndPoint().getZ()* (unit == null ? 1 : unit)));
		return createPolyline(scope,list);
	}
	public IShape manageObj(final IScope scope, final DXFPolyline obj) {
		if (obj == null) {
			return null;
		}
		IList list = GamaListFactory.create(Types.POINT);
		Iterator it = obj.getVertexIterator();
		while(it.hasNext()) {
			DXFVertex vertex = (DXFVertex)it.next();
			list.add(new GamaPoint(vertex.getX() * (unit == null ? 1 : unit)- x_t, vertex.getY() * (unit == null ? 1 : unit)- y_t, vertex.getZ()* (unit == null ? 1 : unit)));
		}
		list = Containers.remove_duplicates(scope, list);
		GamaPoint pt = (GamaPoint) list.get(list.size()-1);
		if(pt.getX() == 0 && pt.getY() == 0 && pt.getZ() == 0)  {
			list.remove(pt);

		}
		if (list.size() < 2) {
			return null;
		}
		return createPolyline(scope,list);
	}

	public IShape defineGeom(final IScope scope, final Object obj){

		if (obj != null) {

			if (obj instanceof DXFArc) {return manageObj(scope, (DXFArc) obj); }
			if (obj instanceof DXFLine) {return manageObj(scope, (DXFLine) obj); }
			if (obj instanceof DXFPolyline) {return manageObj(scope, (DXFPolyline) obj); }
			if (obj instanceof DXFSolid) {return manageObj(scope, (DXFSolid) obj);}
			if (obj instanceof DXFCircle) {return manageObj(scope, (DXFCircle) obj); }

		}
		return null;
	}

	protected void fillBuffer(final IScope scope, final DXFDocument doc ) {
		IList<IShape> geoms = GamaListFactory.create(Types.GEOMETRY);
		double xmax = (doc.getBounds().getMaximumX() - doc.getBounds().getMinimumX()) * (unit == null ? 1 : unit);
		double ymax =  (doc.getBounds().getMaximumY() - doc.getBounds().getMinimumY()) * (unit == null ? 1 : unit);

		IShape env = GamaGeometryType.buildPolygon(GamaListFactory.createWithoutCasting(Types.POINT, new GamaPoint(0,0),new GamaPoint(xmax,0),new GamaPoint(xmax,ymax),new GamaPoint(0,ymax),new GamaPoint(0,0)));
		Iterator it =    doc.getDXFLayerIterator();
		List<IShape> entities = new ArrayList<IShape>();
		while (it.hasNext()) {
			DXFLayer layer = (DXFLayer) it.next();
			Iterator ittype =  layer.getDXFEntityTypeIterator();
			while (ittype.hasNext()) {
				String entityType = (String)ittype.next();
				List<DXFEntity> entity_list = layer.getDXFEntities(entityType);
				for (DXFEntity obj : entity_list) {
					IShape g = defineGeom(scope,obj);
					if (g != null && g.intersects(env)) {
						if (entities.contains(g)) {
							continue;
						}
						entities.add(g);

						g.setAttribute("layer", obj.getLayerName());
						g.setAttribute("id", obj.getID());
						g.setAttribute("scale_factor", obj.getLinetypeScaleFactor());
						g.setAttribute("thickness", obj.getThickness());
						g.setAttribute("is_visible", obj.isVisibile());
						g.setAttribute("is_omit", obj.isOmitLineType());
						g.setAttribute("color_index",obj.getColor());

						if (obj.getColorRGB() != null) {
							g.setAttribute("color", new GamaColor(obj.getColorRGB()[0], obj.getColorRGB()[1], obj.getColorRGB()[2],255));
						}
						if (obj.getLineType() != null) {
							g.setAttribute("line_type", obj.getLineType());
						}

						geoms.add(g);
					}
				}

			}
		}

		Iterator itbl =  doc.getDXFBlockIterator();
		while (itbl.hasNext()) {
			DXFBlock block = (DXFBlock) itbl.next();
			Iterator itent =  block.getDXFEntitiesIterator();
			while (itent.hasNext()) {
				DXFEntity obj = (DXFEntity)itent.next();
				IShape g = defineGeom(scope,obj);
				if (g != null && g.intersects(env)) {
					if (entities.contains(g)) {
						continue;
					}
					entities.add(g);

					g.setAttribute("layer", obj.getLayerName());
					g.setAttribute("id", obj.getID());
					g.setAttribute("scale_factor", obj.getLinetypeScaleFactor());
					g.setAttribute("thickness", obj.getThickness());
					g.setAttribute("is_visible", obj.isVisibile());
					g.setAttribute("is_omit", obj.isOmitLineType());

					g.setAttribute("color_index",obj.getColor());

					if (obj.getColorRGB() != null) {
						g.setAttribute("color", new GamaColor(obj.getColorRGB()[0], obj.getColorRGB()[1], obj.getColorRGB()[2],255));
					}
					if (obj.getLineType() != null) {
						g.setAttribute("line_type", obj.getLineType());
					}


					geoms.add(g);
				}
			}
		}

		setBuffer(geoms);
	}
	@Override
	protected void fillBuffer(final IScope scope) throws GamaRuntimeException {
		if (getBuffer() != null) {
			return ;
		}
		Parser parser = ParserBuilder.createDefaultParser();
		try {
			final InputStream in = new FileInputStream(getFile());
			parser.parse(in, DXFParser.DEFAULT_ENCODING);

			//get the document and the layer
			DXFDocument doc = parser.getDocument();
			x_t = doc.getBounds().getMinimumX() * (unit == null ? 1 : unit);
			y_t = doc.getBounds().getMinimumY() * (unit == null ? 1 : unit);

			fillBuffer(scope, doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void flushBuffer() throws GamaRuntimeException {}


	@Override
	public Envelope computeEnvelope(final IScope scope) {
		Parser parser = ParserBuilder.createDefaultParser();
		try {
			final InputStream in = new FileInputStream(getFile());

			//parse
			parser.parse(in, DXFParser.DEFAULT_ENCODING);

			//get the documnet and the layer
			DXFDocument doc = parser.getDocument();
			Envelope env = new Envelope(0, (doc.getBounds().getMaximumX() - doc.getBounds().getMinimumX()) * (unit == null ? 1 : unit), 0, (doc.getBounds().getMaximumY() - doc.getBounds().getMinimumY()) * (unit == null ? 1 : unit));

			return env;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}
}
