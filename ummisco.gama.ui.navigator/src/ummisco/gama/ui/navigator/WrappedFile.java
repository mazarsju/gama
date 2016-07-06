/**
 * Created by drogoul, 5 févr. 2015
 *
 */
package ummisco.gama.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import ummisco.gama.ui.resources.GamaColors;
import ummisco.gama.ui.resources.GamaFonts;
import ummisco.gama.ui.utils.WorkbenchHelper;

/**
 * Class WrappedFile.
 *
 * @author drogoul
 * @since 5 févr. 2015
 *
 */
public class WrappedFile extends VirtualContent implements IAdaptable {

	final IFile file;

	/**
	 * @param root
	 * @param name
	 */
	public WrappedFile(final VirtualContent root, final IFile wrapped) {
		super(root, wrapped.getName());
		file = wrapped;
	}

	@Override
	public boolean canBeDecorated() {
		return true;
	}

	/**
	 * Method hasChildren()
	 * 
	 * @see ummisco.gama.ui.navigator.VirtualContent#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Font getFont() {
		return GamaFonts.getNavigLinkFont(); // by default
	}

	/**
	 * Method getNavigatorChildren()
	 * 
	 * @see ummisco.gama.ui.navigator.VirtualContent#getNavigatorChildren()
	 */
	@Override
	public Object[] getNavigatorChildren() {
		return EMPTY;
	}

	/**
	 * Method getImage()
	 * 
	 * @see ummisco.gama.ui.navigator.VirtualContent#getImage()
	 */
	@Override
	public Image getImage() {
		// should be handled by the label provider
		return null;
	}

	/**
	 * Method getColor()
	 * 
	 * @see ummisco.gama.ui.navigator.VirtualContent#getColor()
	 */
	@Override
	public Color getColor() {
		return GamaColors.system(SWT.COLOR_BLACK);
	}

	/**
	 * Method isParentOf()
	 * 
	 * @see ummisco.gama.ui.navigator.VirtualContent#isParentOf(java.lang.Object)
	 */
	// @Override
	// public boolean isParentOf(final Object element) {
	// return false;
	// }

	@Override
	public boolean handleDoubleClick() {
		final IEditorInput editorInput = new FileEditorInput(file);
		final IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		if (desc == null) {
			return false;
		}
		final IWorkbenchPage page = WorkbenchHelper.getPage();
		try {
			page.openEditor(editorInput, desc.getId());
		} catch (final PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public IFile getFile() {
		return file;
	}

	/**
	 * Method getAdapter()
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(final Class adapter) {
		return adapter == IFile.class ? file : null;
	}

}
