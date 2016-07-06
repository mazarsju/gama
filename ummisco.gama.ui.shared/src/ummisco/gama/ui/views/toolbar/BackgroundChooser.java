/**
 * Created by drogoul, 9 févr. 2015
 *
 */
package ummisco.gama.ui.views.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import msi.gama.util.GamaColor;
import ummisco.gama.ui.menus.GamaColorMenu;
import ummisco.gama.ui.menus.GamaColorMenu.IColorRunnable;
import ummisco.gama.ui.resources.GamaColors;
import ummisco.gama.ui.resources.GamaColors.GamaUIColor;
import ummisco.gama.ui.resources.GamaIcons;

/**
 * Class FontSizer.
 *
 * @author drogoul
 * @since 9 févr. 2015
 *
 */
public class BackgroundChooser {

	final IToolbarDecoratedView.Colorizable view;
	final String[] labels;
	final GamaUIColor[] colors;

	public BackgroundChooser(final IToolbarDecoratedView.Colorizable view) {
		// We add a control listener to the toolbar in order to install the
		// gesture once the control to resize have been created.
		this.view = view;
		labels = view.getColorLabels();
		colors = new GamaUIColor[labels.length];
		for (int i = 0; i < labels.length; i++) {
			colors[i] = view.getColor(i);
		}
	}

	/**
	 * @param tb
	 */
	public void install(final GamaToolbar2 tb) {
		for (int i = 0; i < labels.length; i++) {
			final int index = i;
			final ToolItem item = tb.button(null, labels[index], labels[index], null, SWT.RIGHT);
			item.setImage(GamaIcons.createTempColorIcon(colors[index]));
			item.addSelectionListener(new SelectionAdapter() {

				SelectionListener listener = new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final MenuItem i = (MenuItem) e.widget;
						final String color = i.getText().replace("#", "");
						final GamaColor c = GamaColor.colors.get(color);
						if (c == null) {
							return;
						}
						changeColor(c.red(), c.green(), c.blue());
					}

				};

				void changeColor(final int r, final int g, final int b) {
					colors[index] = GamaColors.get(r, g, b);
					// Image temp = item.getImage();
					item.setImage(GamaIcons.createTempColorIcon(colors[index]));
					// temp.dispose();
					view.setColor(index, colors[index]);
				}

				@Override
				public void widgetSelected(final SelectionEvent e) {

					GamaColorMenu.getInstance().open(item.getParent(), e, listener, new IColorRunnable() {

						@Override
						public void run(final int r, final int g, final int b) {
							changeColor(r, g, b);
						}
					});

				}
			});

		}
	}

}
