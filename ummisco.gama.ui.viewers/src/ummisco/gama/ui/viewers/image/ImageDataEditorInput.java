package ummisco.gama.ui.viewers.image;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.*;

import ummisco.gama.ui.resources.GamaIcons;

/**
 * An editor input for directly displaying ImageData in the ImageViewer.
 */
public class ImageDataEditorInput implements IEditorInput, IAdaptable {

	private final ImageData imageData;

	private final String name;

	private final ImageDescriptor icon;

	private final String toolTipText;

	/**
	 * Constructor.
	 * 
	 * @param imageData
	 *            the image data (required).
	 * @param name
	 *            the editor display name (required).
	 * @param toolTipText
	 *            the editor tool tip text (optional).
	 * @param icon
	 *            the editor display icon (optional).
	 */
	public ImageDataEditorInput(final ImageData imageData, final String name, final String toolTipText,
		final ImageDescriptor icon) {
		this.imageData = imageData;
		this.name = name;
		this.toolTipText = toolTipText;
		this.icon = icon != null ? icon : GamaIcons.create("file.image2").descriptor(); //$NON-NLS-1$
	}

	/**
	 * Constructor. This will use the default icon and use the name of the tool
	 * tip text.
	 * 
	 * @param imageData
	 *            the image data (required).
	 * @param name
	 *            the editor display name (required).
	 */
	public ImageDataEditorInput(final ImageData imageData, final String name) {
		this(imageData, name, name, null);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
		if ( adapter == ImageData.class ) { return imageData; }

		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return icon;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// this editor state cannot be persisted
		return null;
	}

	@Override
	public String getToolTipText() {
		return toolTipText;
	}
}