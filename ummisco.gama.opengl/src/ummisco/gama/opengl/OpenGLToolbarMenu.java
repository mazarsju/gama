/*********************************************************************************************
 *
 *
 * 'OpenGLItem.java', in plugin 'msi.gama.application', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gama.opengl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import msi.gama.common.interfaces.IDisplaySurface.OpenGL;
import ummisco.gama.ui.resources.IGamaIcons;
import ummisco.gama.ui.views.toolbar.GamaToolbar2;

/**
 * The class FocusItem.
 *
 * @author drogoul
 * @since 19 janv. 2012
 *
 */
public class OpenGLToolbarMenu {

	private Menu menu;

	public void fillMenu(final Menu menu, final SWTLayeredDisplayView view) {

		new MenuItem(menu, SWT.SEPARATOR);

		final MenuItem camera = new MenuItem(menu, SWT.PUSH);
		camera.setImage(IGamaIcons.DISPLAY_TOOLBAR_CAMERA.image());
		final boolean arcBall = view.getDisplaySurface().getData().isArcBallCamera();
		camera.setText(arcBall ? "Use FreeFly camera" : "Use ArcBall camera");
		camera.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				view.getDisplaySurface().runAndUpdate(new Runnable() {

					@Override
					public void run() {
						final boolean old = view.getDisplaySurface().getData().isArcBallCamera();
						((OpenGL) view.getDisplaySurface()).getData().setArcBallCamera(!old);
					}
				});
			}
		});
		new MenuItem(menu, SWT.SEPARATOR);

		final MenuItem rotation = new MenuItem(menu, SWT.CHECK);
		final boolean rotated = view.getDisplaySurface().getData().isRotationOn();
		rotation.setSelection(rotated);
		rotation.setText("Rotate scene");
		rotation.setImage(IGamaIcons.DISPLAY_TOOLBAR_ROTATE.image());
		rotation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				view.getDisplaySurface().runAndUpdate(new Runnable() {

					@Override
					public void run() {
						final boolean rotated = view.getDisplaySurface().getData().isRotationOn();
						view.getDisplaySurface().getData().setRotation(!rotated);
					}
				});
			}
		});
		final MenuItem split = new MenuItem(menu, SWT.CHECK);
		split.setImage(IGamaIcons.DISPLAY_TOOLBAR_SPLIT.image());
		final boolean splitted = view.getDisplaySurface().getData().isLayerSplitted();
		split.setSelection(splitted);
		split.setText("Split layers");
		split.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				view.getDisplaySurface().runAndUpdate(new Runnable() {

					@Override
					public void run() {
						final boolean splitted = view.getDisplaySurface().getData().isLayerSplitted();
						((OpenGL) view.getDisplaySurface()).getData().setLayerSplitted(!splitted);
					}
				});
			}
		});
		final MenuItem triangle = new MenuItem(menu, SWT.CHECK);
		final boolean triangulated = view.getDisplaySurface().getData().isTriangulation();
		triangle.setText("Triangulate scene");
		triangle.setSelection(triangulated);
		triangle.setImage(IGamaIcons.DISPLAY_TOOLBAR_TRIANGULATE.image());
		triangle.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				view.getDisplaySurface().runAndUpdate(new Runnable() {

					@Override
					public void run() {
						final boolean triangulated = view.getDisplaySurface().getData().isTriangulation();
						view.getDisplaySurface().getData().setTriangulation(!triangulated);
					}
				});
			}
		});

	}

	/**
	 * @param tb
	 * @param view
	 */
	public void createItem(final GamaToolbar2 tb, final SWTLayeredDisplayView view) {

		tb.menu("display.presentation2", "Presentation", "OpenGL options", new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent trigger) {
				final boolean asMenu = trigger.detail == SWT.ARROW;
				if (!asMenu) {
					return;
				}
				final ToolItem target = (ToolItem) trigger.widget;
				final ToolBar toolBar = target.getParent();
				if (menu != null) {
					menu.dispose();
				}
				menu = new Menu(toolBar.getShell(), SWT.POP_UP);
				fillMenu(menu, view);
				final Point point = toolBar.toDisplay(new Point(trigger.x, trigger.y));
				menu.setLocation(point.x, point.y);
				menu.setVisible(true);

			}

		}, SWT.LEFT);

	}
}
