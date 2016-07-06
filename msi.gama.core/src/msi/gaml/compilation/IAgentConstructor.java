/*********************************************************************************************
 *
 *
 * 'IAgentConstructor.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.compilation;

import java.util.*;
import msi.gama.metamodel.agent.*;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.runtime.exceptions.GamaRuntimeException;

/**
 * Written by drogoul Modified on 20 ao�t 2010
 *
 * @todo Description
 *
 */
public interface IAgentConstructor {

	public static class Minimal implements IAgentConstructor {

		/**
		 * Method createOneAgent()
		 * @see msi.gaml.compilation.IAgentConstructor#createOneAgent(msi.gama.metamodel.population.IPopulation)
		 */
		@Override
		public IAgent createOneAgent(final IPopulation manager) throws GamaRuntimeException {
			return new MinimalAgent(manager);
		}

	}

	public static class Gaml implements IAgentConstructor {

		/**
		 * Method createOneAgent()
		 * @see msi.gaml.compilation.IAgentConstructor#createOneAgent(msi.gama.metamodel.population.IPopulation)
		 */
		@Override
		public IAgent createOneAgent(final IPopulation manager) throws GamaRuntimeException {
			return new GamlAgent(manager);
		}

	}

	public static Map<Class<IAgent>, IAgentConstructor> CONSTRUCTORS = new HashMap() {

		{
			put(GamlAgent.class, new Gaml());
			put(MinimalAgent.class, new Minimal());
		}
	};

	public abstract IAgent createOneAgent(IPopulation manager) throws GamaRuntimeException;

}
