/*********************************************************************************************
 *
 *
 * 'NavigatorLabelProvider.java', in plugin 'msi.gama.application', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gama.ui.navigator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import msi.gama.runtime.GAMA;
import msi.gaml.compilation.GamaBundleLoader;
import ummisco.gama.ui.metadata.FileMetaDataProvider;
import ummisco.gama.ui.resources.GamaFonts;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.resources.IGamaColors;
import ummisco.gama.ui.resources.IGamaIcons;

public class NavigatorLabelProvider extends CellLabelProvider implements ILabelProvider, IColorProvider, IFontProvider {

	// TODO BUILD A LIST FROM THE FILES LOADED IN GAMAFILE
	// private static final Set<String> HANDLED = new
	// HashSet(Arrays.asList("shp", "gaml", "jpg", "jpeg", "png", "bmp",
	// "html", "htm", "gif", "csv", "ico", "asc", "pgm", "svg"));

	@Override
	public String getText(final Object element) {
		if (element instanceof VirtualContent) {
			return ((VirtualContent) element).getName();
		}
		return null;
	}

	public static boolean isResource(final IResource r) {
		if (r instanceof IFile) {
			return !GAMA.getGui().getMetaDataProvider().isGAML((IFile) r);
		}
		if (r instanceof IContainer) {
			try {
				for (final IResource m : ((IContainer) r).members()) {
					if (!isResource(m)) {
						return false;
					}
				}
			} catch (final CoreException e) {
			}
			return true;
		}
		return false;
	}

	@Override
	public Image getImage(final Object element) {
		if (element instanceof WrappedFile) {
			return WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider()
					.getImage(((WrappedFile) element).getFile());
		}
		if (element instanceof VirtualContent) {
			return ((VirtualContent) element).getImage();
		}
		if (element instanceof IProject) {
			return IGamaIcons.FOLDER_PROJECT.image();
		}
		if (element instanceof IFolder) {
			if (isResource((IFolder) element)) {
				return IGamaIcons.FOLDER_RESOURCES.image();
			} else {
				return IGamaIcons.FOLDER_MODEL.image();
			}
		}
		if (element instanceof IFile) {
			final IFile f = (IFile) element;
			final String s = f.getFileExtension();
			if (isHandled(s)) {
				if (FileMetaDataProvider.isShapeFileSupport(f)) {
					return GamaIcons.create("file.shapesupport2").image();
				} else {
					return null;
				}
			} else {
				return GamaIcons.create("file.text2").image();
			}
		}
		return null;
	}

	/**
	 * @param s
	 * @return
	 */
	private boolean isHandled(final String s) {
		return GamaBundleLoader.HANDLED_FILE_EXTENSIONS.contains(s);
	}

	@Override
	public void addListener(final ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
	}

	/**
	 * Method getFont()
	 * 
	 * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
	 */
	@Override
	public Font getFont(final Object element) {
		if (element instanceof VirtualContent) {
			return ((VirtualContent) element).getFont();
		}
		if (element instanceof IProject) {
			return GamaFonts.getNavigHeaderFont();
		}
		if (element instanceof IFolder && isResource((IFolder) element)) {
			return GamaFonts.getResourceFont();
		}
		if (element instanceof IFile) {
			return GamaFonts.getNavigFileFont();
		}
		return GamaFonts.getNavigFolderFont();
	}

	/**
	 * Method getForeground()
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	@Override
	public Color getForeground(final Object element) {
		if (element instanceof VirtualContent) {
			return ((VirtualContent) element).getColor();
		}
		if (!(element instanceof IFile)) {
			return IGamaColors.GRAY_LABEL.color();
		}
		return null;
	}

	/**
	 * Method getBackground()
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	@Override
	public Color getBackground(final Object element) {
		return null;
	}

	/**
	 * Method update()
	 * 
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(final ViewerCell cell) {
	}

}
