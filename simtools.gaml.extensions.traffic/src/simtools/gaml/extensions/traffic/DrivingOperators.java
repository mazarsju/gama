/*********************************************************************************************
 * 
 * 
 * 'DrivingOperators.java', in plugin 'simtools.gaml.extensions.traffic', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package simtools.gaml.extensions.traffic;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.shape.IShape;
import msi.gama.metamodel.topology.graph.GamaSpatialGraph;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.operator;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.util.*;
import msi.gama.util.graph.IGraph;
import msi.gaml.types.Types;

public class DrivingOperators {

	@operator(value = "as_driving_graph",
		content_type = ITypeProvider.SECOND_CONTENT_TYPE,
		index_type = ITypeProvider.FIRST_CONTENT_TYPE,
				concept = { IConcept.GRAPH, IConcept.TRANSPORT })
	@doc(value = "creates a graph from the list/map of edges given as operand and connect the node to the edge",
		examples = { @example(value = "as_driving_graph(road,node)  --:  build a graph while using the road agents as edges and the node agents as nodes",
			isExecutable = false) },
		see = { "as_intersection_graph", "as_distance_graph", "as_edge_graph" })
	public static
		IGraph spatialDrivingFromEdges(final IScope scope, final IContainer edges, final IContainer nodes) {
		IGraph graph = new GamaSpatialGraph(edges, nodes, scope);
		for ( Object edge : edges.iterable(scope) ) {
			if ( edge instanceof IShape ) {
				IAgent ag = ((IShape) edge).getAgent();
				if ( ag.hasAttribute(RoadSkill.LANES) && ag.hasAttribute(RoadSkill.AGENTS_ON) ) {
					int lanes = (Integer) ag.getAttribute(RoadSkill.LANES);
					if ( lanes > 0 ) {
						IList agentsOn = (IList) ag.getAttribute(RoadSkill.AGENTS_ON);
						for ( int i = 0; i < lanes; i++ ) {
							int nbSeg = ag.getInnerGeometry().getNumPoints() - 1;
							IList lisSg = GamaListFactory.create(Types.NO_TYPE);
							for ( int j = 0; j < nbSeg; j++ ) {
								lisSg.add(GamaListFactory.create(Types.NO_TYPE));
							}
							agentsOn.add(lisSg);
						}
					}
					ag.setAttribute(RoadSkill.AGENTS, GamaListFactory.create(Types.NO_TYPE));
				}

			}
		}
		return graph;
	}

}
