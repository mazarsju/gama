/**
 * Created by drogoul, 9 févr. 2015
 *
 */
package ummisco.gama.ui.views.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

import ummisco.gama.ui.resources.IGamaIcons;

/**
 * Class ZoomController.
 *
 * @author drogoul
 * @since 9 févr. 2015
 *
 */
public class ZoomController {

	// Fix for Issue #1291
	private final boolean includingScrolling;
	private final IToolbarDecoratedView.Zoomable view;
	private final GestureListener gl = new GestureListener() {

		@Override
		public void gesture(final GestureEvent ge) {
			if (ge.detail == SWT.GESTURE_MAGNIFY) {
				if (ge.magnification > 1.0) {
					view.zoomIn();
				} else if (ge.magnification < 1.0) {
					view.zoomOut();
				}
			}

		}
	};

	private final MouseListener ml = new MouseAdapter() {

		@Override
		public void mouseDoubleClick(final MouseEvent e) {
			if (e.button == 1)
				view.zoomFit();
		}
	};

	private final MouseWheelListener mw = new MouseWheelListener() {

		@Override
		public void mouseScrolled(final MouseEvent e) {
			if (e.count < 0) {
				view.zoomOut();
			} else {
				view.zoomIn();
			}
		}
	};

	/**
	 * @param view
	 */
	public ZoomController(final IToolbarDecoratedView.Zoomable view) {
		this.view = view;
		this.includingScrolling = view.zoomWhenScrolling();
	}

	/**
	 * @param tb
	 */
	public void install(final GamaToolbar2 tb) {

		tb.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(final ControlEvent e) {
				final Control[] controls = view.getZoomableControls();
				for (final Control c : controls) {
					if (c != null) {
						c.addGestureListener(gl);
						c.addMouseListener(ml);
						if (includingScrolling) {
							c.addMouseWheelListener(mw);
						}
						// once installed the listener removes itself from the
						// toolbar
						tb.removeControlListener(this);
					}
				}
			}

		});
		tb.button(IGamaIcons.DISPLAY_TOOLBAR_ZOOMIN.getCode(), "Zoom in", "Zoom in", new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				view.zoomIn();
			}
		}, SWT.RIGHT);
		tb.button(IGamaIcons.DISPLAY_TOOLBAR_ZOOMFIT.getCode(), "Zoom fit", "Zoom to fit view", new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				view.zoomFit();
			}

		}, SWT.RIGHT);

		tb.button(IGamaIcons.DISPLAY_TOOLBAR_ZOOMOUT.getCode(), "Zoom out", "Zoom out", new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				view.zoomOut();
			}
		}, SWT.RIGHT);

	}

}
