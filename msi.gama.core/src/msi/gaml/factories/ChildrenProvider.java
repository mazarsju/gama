/*********************************************************************************************
 * 
 *
 * 'ChildrenProvider.java', in plugin 'msi.gama.core', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import msi.gaml.descriptions.IDescription;

public class ChildrenProvider {

	private final List<IDescription> children;
	public static final ChildrenProvider NONE = new ChildrenProvider(null);
	public static final ChildrenProvider FUTURE = new ChildrenProvider(new ArrayList()) {
		@Override
		public boolean hasChildren() {
			return true;
		}
	};

	public ChildrenProvider(final List descs) {
		children = descs;
	}

	public List<IDescription> getChildren() {
		return children == null ? Collections.EMPTY_LIST : children;
	}

	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

}
