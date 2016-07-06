/*********************************************************************************************
 *
 *
 * 'IExperimentCreator.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.common.interfaces;

import msi.gama.kernel.experiment.IExperimentAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.precompiler.GamlProperties;
import msi.gaml.descriptions.IGamlDescription;

public interface IExperimentAgentCreator {

	public static class ExperimentAgentDescription implements IExperimentAgentCreator, IGamlDescription {

		private final IExperimentAgentCreator original;
		private final String name, plugin;

		public ExperimentAgentDescription(final IExperimentAgentCreator original, final String name, final String plugin) {
			this.original = original;
			this.name = name;
			this.plugin = plugin;
		}

		/**
		 * Method create()
		 * @see msi.gama.common.interfaces.IExperimentAgentCreator#create(java.lang.Object[])
		 */
		@Override
		public IExperimentAgent create(final IPopulation pop) {
			return original.create(pop);
		}

		/**
		 * Method getName()
		 * @see msi.gama.common.interfaces.INamed#getName()
		 */
		@Override
		public String getName() {
			return name;
		}

		/**
		 * Method setName()
		 * @see msi.gama.common.interfaces.INamed#setName(java.lang.String)
		 */
		@Override
		public void setName(final String newName) {}

		/**
		 * Method serialize()
		 * @see msi.gama.common.interfaces.IGamlable#serialize(boolean)
		 */
		@Override
		public String serialize(final boolean includingBuiltIn) {
			return getName();
		}

		/**
		 * Method getTitle()
		 * @see msi.gaml.descriptions.IGamlDescription#getTitle()
		 */
		@Override
		public String getTitle() {
			return "Experiment Agent supported by " + getName() + " technology";
		}

		/**
		 * Method getDocumentation()
		 * @see msi.gaml.descriptions.IGamlDescription#getDocumentation()
		 */
		@Override
		public String getDocumentation() {
			return "";
		}

		/**
		 * Method getDefiningPlugin()
		 * @see msi.gaml.descriptions.IGamlDescription#getDefiningPlugin()
		 */
		@Override
		public String getDefiningPlugin() {
			return plugin;
		}

		/**
		 * Method collectPlugins()
		 * @see msi.gaml.descriptions.IGamlDescription#collectPlugins(java.util.Set)
		 */
		@Override
		public void collectMetaInformation(final GamlProperties meta) {
			meta.put(GamlProperties.PLUGINS, plugin);
		}
	}

	IExperimentAgent create(IPopulation pop);

}