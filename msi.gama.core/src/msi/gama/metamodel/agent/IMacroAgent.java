/*********************************************************************************************
 *
 *
 * 'IMacroAgent.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.metamodel.agent;

import java.util.Map;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gaml.species.ISpecies;
import msi.gaml.types.IType;

@vars({
	@var(name = IKeyword.MEMBERS,
		type = IType.LIST,
		of = IType.AGENT,
		doc = {
			@doc("Returns the list of agents for the population(s) of which the receiver agent is a direct host") }),
	@var(name = IKeyword.AGENTS,
		type = IType.LIST,
		of = IType.AGENT,
		doc = {
			@doc("Returns the list of agents for the population(s) of which the receiver agent is a direct or undirect host") }) })
public interface IMacroAgent extends IAgent {

	/**
	 * Verifies if this agent can capture other agent as the specified micro-species.
	 *
	 * An agent A can capture another agent B as newSpecies if the following conditions are correct:
	 * 1. other is not this agent;
	 * 2. other is not "world" agent;
	 * 3. newSpecies is a (direct) micro-species of A's species;
	 * 4. newSpecies is a direct sub-species of B's species.
	 *
	 * @param other
	 * @return
	 * 		true if this agent can capture other agent
	 *         false otherwise
	 */
	public abstract boolean canCapture(IAgent other, ISpecies newSpecies);

	public abstract IAgent captureMicroAgent(IScope scope, final ISpecies microSpecies, final IAgent microAgent)
		throws GamaRuntimeException;

	/**
	 * Captures some agents as micro-agents with the specified micro-species as their new species.
	 *
	 * @param microSpecies the species that the captured agents will become, this must be a
	 *            micro-species of this agent's species.
	 * @param microAgents
	 * @return
	 * @throws GamaRuntimeException
	 */
	public abstract IList<IAgent> captureMicroAgents(IScope scope, final ISpecies microSpecies,
		final IList<IAgent> microAgents) throws GamaRuntimeException;

	/**
	 * Returns all the agents which consider this agent as direct host.
	 *
	 * @return
	 */
	@getter(IKeyword.MEMBERS)
	public abstract IContainer<?, IAgent> getMembers(IScope scope);

	/**
	 * Returns the population of the specified (direct) micro-species.
	 *
	 * @param microSpecies
	 * @return
	 */
	public abstract IPopulation getMicroPopulation(ISpecies microSpecies);

	/**
	 * Returns the population of the specified (direct) micro-species.
	 *
	 * @param microSpeciesName
	 * @return
	 */
	public abstract IPopulation getMicroPopulation(String microSpeciesName);

	/**
	 * Returns a list of populations of (direct) micro-species.
	 *
	 * @return
	 */
	public abstract IPopulation[] getMicroPopulations();

	/**
	 * Verifies if this agent contains micro-agents or not.
	 *
	 * @return true if this agent contains micro-agent(s)
	 *         false otherwise
	 */
	public abstract boolean hasMembers();

	public abstract void initializeMicroPopulation(IScope scope, String name);

	/**
	 * Initialize Populations to manage micro-agents.
	 */
	// public abstract void initializeMicroPopulations(IScope scope);

	/**
	 * Migrates some micro-agents from one micro-species to another micro-species of this agent's
	 * species.
	 *
	 * @param microAgent
	 * @param newMicroSpecies
	 * @return
	 */
	public abstract IList<IAgent> migrateMicroAgents(IScope scope, final IList<IAgent> microAgents,
		final ISpecies newMicroSpecies);

	/**
	 * Migrates some micro-agents from one micro-species to another micro-species of this agent's
	 * species.
	 *
	 * @param microAgent
	 * @param newMicroSpecies
	 * @return
	 */
	public abstract IList<IAgent> migrateMicroAgents(IScope scope, final ISpecies oldMicroSpecies,
		final ISpecies newMicroSpecies);

	/**
	 * Releases some micro-agents of this agent.
	 *
	 * @param microAgents
	 * @return
	 * @throws GamaRuntimeException
	 */
	public abstract IList<IAgent> releaseMicroAgents(IScope scope, final IList<IAgent> microAgents)
		throws GamaRuntimeException;

	@setter(IKeyword.MEMBERS)
	public abstract void setMembers(IList<IAgent> members);

	@setter(IKeyword.AGENTS)
	public abstract void setAgents(IList<IAgent> agents);

	/**
	 * Returns all the agents which consider this agent as direct or in-direct host.
	 *
	 * @return
	 */
	@getter(IKeyword.AGENTS)
	public abstract IList<IAgent> getAgents(IScope scope);
	

	/**
	 * Returns the number of  agents which consider this agent as direct host.
	 *
	 * @return
	 */
	public abstract int getNbAgents();
	
	public abstract void addSubAgents(int nb);
	
	public abstract void removeAgent();
	/**
	 * @return
	 */
	// public abstract boolean mustScheduleMembers();

	// hqnghi manipulate micro-models
	public abstract void addExternMicroPopulation(final String expName, final IPopulation pop);

	public abstract IPopulation getExternMicroPopulationFor(final String expName);

	public abstract Map<String, IPopulation> getExternMicroPopulations();
	// end-hqnghi

}