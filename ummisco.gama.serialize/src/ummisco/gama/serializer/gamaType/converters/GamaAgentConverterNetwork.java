/*********************************************************************************************
 * 
 * 
 * 'GamaAgentConverter.java', in plugin 'ummisco.gama.communicator', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package ummisco.gama.serializer.gamaType.converters;

import msi.gama.kernel.simulation.SimulationAgent;
import msi.gama.metamodel.agent.GamlAgent;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.agent.MinimalAgent;
import msi.gama.metamodel.agent.SavedAgent;
import msi.gama.metamodel.population.GamaPopulation;
import msi.gama.metamodel.population.IPopulation;
import ummisco.gama.serializer.gamaType.reduced.GamaMapReducer;

import java.util.List;

import com.thoughtworks.xstream.converters.*;
import com.thoughtworks.xstream.io.*;

public class GamaAgentConverterNetwork implements Converter {

	ConverterScope convertScope;
	
	public GamaAgentConverterNetwork(ConverterScope s){
		convertScope = s;
	}
	
	@Override
	public boolean canConvert(final Class arg0) {
//		return (arg0.equals(GamlAgent.class) || arg0.equals(MinimalAgent.class));
		if(GamlAgent.class.equals(arg0) || MinimalAgent.class.equals(arg0) || GamlAgent.class.equals(arg0.getSuperclass())){
			return true;
		}		
		
		return false;
	}

	@Override
	public void marshal(final Object arg0, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		MinimalAgent agt = (MinimalAgent) arg0;
		writer.startNode("agentpopulation");
			writer.setValue(agt.getPopulation().getName());
		writer.endNode();
		writer.startNode("agentData");
		System.out.println("ConvertAnother : AgentConverter Network " + agt.getClass());
		context.convertAnother(new SavedAgent(convertScope.getScope(), agt));
		System.out.println("===========END ConvertAnother : GamaAgent Network");
		writer.endNode();
		writer.startNode("agentReference");
		writer.setValue(agt.getName());
		writer.endNode();
	
	}

	@Override
	public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext arg1) {
		reader.moveDown();
		String populationName = reader.getValue();
		reader.moveUp();
		reader.moveDown();
			SavedAgent rmt = (SavedAgent) arg1.convertAnother(null, SavedAgent.class);
			IPopulation mpop = (IPopulation) convertScope.getScope().getAgentScope().getPopulationFor(populationName);
			((SavedAgent)rmt).restoreTo(convertScope.getScope(), mpop);		
		reader.moveUp();
		reader.moveDown();
		SimulationAgent simAgt = convertScope.getSimulationAgent();
		List<IAgent> lagt;
		if(simAgt == null) {
			lagt = (convertScope.getScope()).getSimulationScope().getAgents(convertScope.getScope());
		} else {
			lagt = simAgt.getAgents(convertScope.getScope());
		}
		
		boolean found = false;
		int i = 0;
		IAgent agt = null;
		while(!found && (i < lagt.size())) {
			if(lagt.get(i).getName().equals(reader.getValue())) {
				found = true;
				agt = lagt.get(i);
			}
			i++;
		}
		reader.moveUp();
		return agt;
	}

}
