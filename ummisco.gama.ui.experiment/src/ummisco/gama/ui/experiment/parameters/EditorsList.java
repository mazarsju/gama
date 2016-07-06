/*********************************************************************************************
 *
 *
 * 'EditorsList.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gama.ui.experiment.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import msi.gama.common.interfaces.ItemList;
import msi.gama.kernel.experiment.IParameter;
import msi.gama.metamodel.agent.IAgent;
import ummisco.gama.ui.interfaces.IParameterEditor;

public abstract class EditorsList<T> implements ItemList<T> {

	/* Map to associate a category to each parameter */
	protected final Map<T, Map<String, IParameterEditor>> categories = new LinkedHashMap();

	@Override
	public List<T> getItems() {
		return new ArrayList(categories.keySet());
	}

	@Override
	public abstract String getItemDisplayName(final T obj, final String previousName);

	public abstract void add(final Collection<? extends IParameter> params, final IAgent agent);

	public Map<T, Map<String, IParameterEditor>> getCategories() {
		return categories;
	}

	public void revertToDefaultValue() {
		for (final Map<String, IParameterEditor> editors : categories.values()) {
			for (final IParameterEditor ed : editors.values()) {
				ed.revertToDefaultValue();
			}
		}
	}

	@Override
	public void removeItem(final T name) {
		categories.remove(name);
	}

	@Override
	public void pauseItem(final T name) {
	}

	@Override
	public abstract void updateItemValues();

	@Override
	public void resumeItem(final T obj) {
	}

	@Override
	public void focusItem(final T obj) {
	}

	@Override
	public void makeItemVisible(final T obj, final boolean b) {
	}

	@Override
	public void makeItemSelectable(final T obj, final boolean b) {
	}

}
