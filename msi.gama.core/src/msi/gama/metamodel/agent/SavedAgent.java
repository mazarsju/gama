package msi.gama.metamodel.agent;

import java.util.*;
import gnu.trove.map.hash.THashMap;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.species.ISpecies;

/**
 * A helper class to save agent and restore/recreate agent as a member of a population.
 */
public class SavedAgent {

	/** Variables which are not saved during the capture and release process. */
	static final List<String> UNSAVABLE_VARIABLES = Arrays.asList(IKeyword.PEERS, IKeyword.AGENTS, IKeyword.HOST,
		IKeyword.TOPOLOGY, IKeyword.MEMBERS, "populations");

	int index;
	Map<String, Object> variables;
	Map<String, List<SavedAgent>> innerPopulations;

	public SavedAgent(final IScope scope, final IAgent agent) throws GamaRuntimeException {
		index = agent.getIndex();
		saveAttributes(scope, agent);
		if ( agent instanceof IMacroAgent ) {
			saveMicroAgents(scope, (IMacroAgent) agent);
		}
	}

	public SavedAgent(int ind, Map<String, Object> v, Map<String, List<SavedAgent>> inPop){
		this(v, inPop);
		index = ind;
	}	
	
	public SavedAgent(Map<String, Object> v, Map<String, List<SavedAgent>> inPop){
		variables = v;
		innerPopulations = inPop;
	}
	
	public Object getAttributeValue(String attrName){
		return variables.get(attrName);
	}
	
	public Map<String, Object> getVariables(){
		return variables;
	}
	
	public Map<String, List<SavedAgent>> getInnerPopulations() {
		return innerPopulations;
	}
	
	public int getIndex() {
		return index;
	}
	
	/**
	 * Saves agent's attributes to a map.
	 *
	 * @param agent
	 * @throws GamaRuntimeException
	 */
	private void saveAttributes(final IScope scope, final IAgent agent) throws GamaRuntimeException {
		variables = new THashMap<String, Object>(11, 0.9f);
		final ISpecies species = agent.getSpecies();
		for ( final String specVar : species.getVarNames() ) {
			if ( UNSAVABLE_VARIABLES.contains(specVar) ) {
				continue;
			}

			if ( species.getVar(specVar).value(scope, agent) instanceof IPopulation ) {
				continue;
			}		
			
			if ( specVar.equals(IKeyword.SHAPE) ) {
				// variables.put(specVar, geometry.copy());
				// Changed 3/2/12: is it necessary to make the things below ?
//				variables.put(specVar,
//						new GamaShape(agent.getGeometry()));
				// Changed 26/5/16: additional variables exist in the shape. To keep them in the SavedAgent, we add them at hand in the geometry.
				// We cannot keep all the GamaShape, because it contains populations too.
				GamaShape shape = new GamaShape(((GamaShape) species.getVar(specVar).value(scope, agent)).getInnerGeometry());
				
				if(agent.getAttributes() != null) {
					for(Object keyAttr : agent.getAttributes().getKeys()) {
						String attrName = (String) keyAttr;
						if( UNSAVABLE_VARIABLES.contains(attrName)) { continue; }
						if(species.getVarNames().contains(attrName)) { continue; }
						if(agent.getAttribute(keyAttr) instanceof IPopulation) {continue;}
						shape.setAttribute(attrName, agent.getAttribute(keyAttr));
					}	
				}
				
				variables.put(specVar, shape);			
				
				continue;
			}
			variables.put(specVar, species.getVar(specVar).value(scope, agent));
		}
	}

	/**
	 * Recursively save micro-agents of an agent.
	 *
	 * @param agent The agent having micro-agents to be saved.
	 * @throws GamaRuntimeException
	 */
	private void saveMicroAgents(final IScope scope, final IMacroAgent agent) throws GamaRuntimeException {
		innerPopulations = new THashMap<String, List<SavedAgent>>();

		for ( final IPopulation microPop : agent.getMicroPopulations() ) {
			final List<SavedAgent> savedAgents = new ArrayList<SavedAgent>();
			final Iterator<IAgent> it = microPop.iterator();
			while (it.hasNext()) {
				savedAgents.add(new SavedAgent(scope, it.next()));
			}

			innerPopulations.put(microPop.getSpecies().getName(), savedAgents);
		}
	}

	/**
	 * @param scope
	 *            Restores the saved agent as a member of the target population.
	 *
	 * @param targetPopulation The population that the saved agent will be restored to.
	 * @return
	 * @throws GamaRuntimeException
	 */
	public IAgent restoreTo(final IScope scope, final IPopulation targetPopulation) throws GamaRuntimeException {
		final List<Map> agentAttrs = new ArrayList<Map>();
		agentAttrs.add(variables);
		final List<? extends IAgent> restoredAgents = targetPopulation.createAgents(scope, 1, agentAttrs, true, true);
		restoreMicroAgents(scope, restoredAgents.get(0));

		return restoredAgents.get(0);
	}
	
	/**
	 *
	 *
	 * @param host
	 * @throws GamaRuntimeException
	 */
	public void restoreMicroAgents(final IScope scope, final IAgent host) throws GamaRuntimeException {
		if ( innerPopulations != null ) {
			for ( final String microPopName : innerPopulations.keySet() ) {
				final IPopulation microPop = ((IMacroAgent) host).getMicroPopulation(microPopName);

				if ( microPop != null ) {
					final List<SavedAgent> savedMicros = innerPopulations.get(microPopName);
					final List<Map> microAttrs = new ArrayList<Map>();
					for ( final SavedAgent sa : savedMicros ) {
						microAttrs.add(sa.variables);
					}

					final List<? extends IAgent> microAgents =
					microPop.createAgents(scope, savedMicros.size(), microAttrs, true, true);

					for ( int i = 0; i < microAgents.size(); i++ ) {
						savedMicros.get(i).restoreMicroAgents(scope, microAgents.get(i));
					}
				}
			}
		}
	}
}