/*********************************************************************************************
 *
 *
 * 'LayerBox.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.outputs.layers;

import msi.gama.metamodel.shape.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.expressions.*;
import msi.gaml.operators.Cast;
import msi.gaml.operators.fastmaths.FastMath;
import msi.gaml.types.IType;

/**
 * Written by drogoul Modified on 16 nov. 2010
 *
 * @todo Description
 *
 */
public class LayerBox implements IDisplayLayerBox {

	IExpression transparency = new ConstantExpression(0d);
	IExpression loc = new ConstantExpression(new GamaPoint(0, 0, 0));
	IExpression size = new ConstantExpression(new GamaPoint(1, 1, 1));
	IExpression refresh = new ConstantExpression(true);
	IExpression trace = new ConstantExpression(0);
	IExpression fading = new ConstantExpression(false);
	IExpression selectable = new ConstantExpression(true);

	Double currentTransparency = 0d;
	ILocation currentPosition;
	ILocation currentSize;
	Boolean currentRefresh;
	Boolean currentFading;
	Integer currentTrace;
	Boolean currentSelectable;

	ILocation constantPosition = null;
	ILocation constantSize = null;
	Double constantTransparency = null;
	Boolean constantRefresh = null;
	Boolean constantFading = null;
	Integer constantTrace = null;
	Boolean constantSelectable = null;

	boolean constantBoundingBox = false;

	public LayerBox(final IExpression transp, final IExpression pos, final IExpression ext, final IExpression refr,
		final IExpression tr, final IExpression fd, final IExpression sl) throws GamaRuntimeException {
		IScope scope = null; // GAMA.obtainNewScope();
		setTransparency(scope, transp == null ? transparency : transp);
		setPosition(scope, pos == null ? loc : pos);
		setSize(scope, ext == null ? size : ext);
		setRefresh(scope, refr == null ? refresh : refr);
		setTrace(scope, tr == null ? trace : tr);
		setFading(scope, fd == null ? fading : fd);
		setSelectable(scope, sl == null ? selectable : sl);
		setConstantBoundingBox(loc.isConst() && size.isConst());
	}

	@Override
	public void setConstantBoundingBox(final boolean b) {
		constantBoundingBox = b;
		if ( b ) {
			constantPosition = currentPosition;
			constantSize = currentSize;
		}
	}

	@Override
	public void compute(final IScope scope) throws GamaRuntimeException {
		try {
			currentTransparency = constantTransparency == null
				? 1d - FastMath.min(FastMath.max(Cast.asFloat(scope, transparency.value(scope)), 0d), 1d)
				: constantTransparency;
			currentSelectable =
				constantSelectable == null ? Cast.asBool(scope, selectable.value(scope)) : constantSelectable;
			if ( !constantBoundingBox ) {
				currentPosition = constantPosition == null ? Cast.asPoint(scope, loc.value(scope)) : constantPosition;
				currentSize = constantSize == null ? Cast.asPoint(scope, size.value(scope)) : constantSize;
			}
			currentRefresh = constantRefresh == null ? Cast.asBool(scope, refresh.value(scope)) : constantRefresh;
			currentTrace =
				constantTrace == null ? trace.getType().id() == IType.BOOL && Cast.asBool(scope, trace.value(scope))
					? Integer.MAX_VALUE : Cast.asInt(scope, trace.value(scope)) : constantTrace;
			currentFading = constantFading == null ? Cast.asBool(scope, fading.value(scope)) : constantFading;

		} catch (Exception e) {
			throw GamaRuntimeException.create(e, scope);
		}
	}

	@Override
	public void setTransparency(final IScope scope, final IExpression t) throws GamaRuntimeException {
		if ( t != null ) {
			constantTransparency = null;
			transparency = t;
			if ( t.isConst() ) {
				setTransparency(Cast.asFloat(scope, t.value(scope)));
			}
		}
	}

	@Override
	public void setPosition(final IScope scope, final IExpression p) throws GamaRuntimeException {
		if ( p != null ) {
			constantPosition = null;
			constantBoundingBox = false;
			loc = p;
			if ( p.isConst() ) {
				setPosition(Cast.asPoint(scope, loc.value(scope)));
			}
		}
	}

	@Override
	public void setSize(final IScope scope, final IExpression e) throws GamaRuntimeException {
		if ( e != null ) {
			constantSize = null;
			constantBoundingBox = false;
			size = e;
			if ( e.isConst() ) {
				setSize(Cast.asPoint(scope, size.value(scope)));
			}
		}
	}

	@Override
	public void setRefresh(final IScope scope, final IExpression r) throws GamaRuntimeException {
		if ( r != null ) {
			constantRefresh = null;
			refresh = r;
			if ( r.isConst() ) {
				setRefresh(Cast.asBool(scope, r.value(scope)));
			}
		}

	}

	@Override
	public void setTransparency(final double f) {
		currentTransparency = constantTransparency = 1d - FastMath.min(FastMath.max(f, 0d), 1d);
	}

	@Override
	public void setSize(final ILocation p) {
		setSize(p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void setSize(final double width, final double height, final double depth) {
		currentSize = constantSize = new GamaPoint(width, height, depth);
		if ( constantPosition != null ) {
			constantBoundingBox = true;
		}
	}

	@Override
	public void setPosition(final ILocation p) {
		setPosition(p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void setPosition(final double x, final double y, final double z) {
		currentPosition = constantPosition = new GamaPoint(x, y, z);
		if ( constantSize != null ) {
			constantBoundingBox = true;
		}
	}

	@Override
	public void setRefresh(final Boolean r) {
		currentRefresh = constantRefresh = r;

	}

	@Override
	public final Double getTransparency() {
		return currentTransparency;
	}

	@Override
	public ILocation getPosition() {
		return currentPosition;
	}

	@Override
	public ILocation getSize() {
		return currentSize;
	}

	@Override
	public Boolean getRefresh() {
		return currentRefresh;
	}

	/**
	 * Method setTrace()
	 * @see msi.gama.outputs.layers.IDisplayLayerBox#setTrace(msi.gama.runtime.IScope, msi.gaml.expressions.IExpression)
	 */
	@Override
	public void setTrace(final IScope scope, final IExpression r) {
		if ( r != null ) {
			constantTrace = null;
			trace = r;
			if ( r.isConst() ) {
				if ( r.getType().id() == IType.BOOL ) {
					boolean val = Cast.asBool(scope, trace.value(scope));
					setTrace(val ? Integer.MAX_VALUE : 0);
				} else {
					setTrace(Cast.asInt(scope, r.value(scope)));
				}
			}
		}
	}

	/**
	 * @param asBool
	 */
	private void setTrace(final Integer t) {
		currentTrace = constantTrace = t;
	}

	/**
	 * Method setFading()
	 * @see msi.gama.outputs.layers.IDisplayLayerBox#setFading(msi.gama.runtime.IScope, msi.gaml.expressions.IExpression)
	 */
	@Override
	public void setFading(final IScope scope, final IExpression r) {
		if ( r != null ) {
			constantFading = null;
			fading = r;
			if ( r.isConst() ) {
				setFading(Cast.asBool(scope, r.value(scope)));
			}
		}
	}

	@Override
	public void setSelectable(final IScope scope, final IExpression r) {
		if ( r != null ) {
			constantSelectable = null;
			fading = r;
			if ( r.isConst() ) {
				setSelectable(Cast.asBool(scope, r.value(scope)));
			}
		}
	}

	/**
	 * @param asBool
	 */
	private void setFading(final Boolean b) {
		currentFading = constantFading = b;
	}

	@Override
	public void setSelectable(final Boolean b) {
		currentSelectable = constantSelectable = b;
	}

	/**
	 * Method getTrace()
	 * @see msi.gama.outputs.layers.IDisplayLayerBox#getTrace()
	 */
	@Override
	public Integer getTrace() {
		return currentTrace;
	}

	/**
	 * Method getFading()
	 * @see msi.gama.outputs.layers.IDisplayLayerBox#getFading()
	 */
	@Override
	public Boolean getFading() {
		return currentFading;
	}

	@Override
	public Boolean isSelectable() {
		return currentSelectable;
	}

}
