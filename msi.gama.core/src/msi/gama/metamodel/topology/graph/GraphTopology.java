/*********************************************************************************************
 * 
 * 
 * 'GraphTopology.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.metamodel.topology.graph;

import gnu.trove.set.hash.THashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.metamodel.shape.IShape;
import msi.gama.metamodel.topology.AbstractTopology;
import msi.gama.metamodel.topology.ITopology;
import msi.gama.metamodel.topology.filter.IAgentFilter;
import msi.gama.metamodel.topology.filter.In;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IContainer;
import msi.gama.util.IList;
import msi.gama.util.path.GamaPath;
import msi.gama.util.path.GamaSpatialPath;
import msi.gama.util.path.IPath;
import msi.gama.util.path.PathFactory;
import msi.gaml.types.Types;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The class GraphTopology.
 * 
 * @author drogoul
 * @since 27 nov. 2011
 * 
 */
public class GraphTopology extends AbstractTopology {

	/**
	 * @param scope
	 * @param env
	 * @param torus
	 */
	public GraphTopology(final IScope scope, final IShape env, final GamaSpatialGraph graph) {
		super(scope, env, null);
		places = graph;
	}

	// The default topologies for graphs.
	public GraphTopology(final IScope scope, final GamaSpatialGraph graph) {
		this(scope, scope.getSimulationScope().getGeometry(), graph);
	}

	@Override
	protected boolean canCreateAgents() {
		return true;
	}

	/**
	 * @throws GamaRuntimeException
	 * @throws GamaRuntimeException
	 * @see msi.gama.environment.ITopology#pathBetween(msi.gama.interfaces.IGeometry, msi.gama.interfaces.IGeometry)
	 */
	@Override
	public GamaSpatialPath pathBetween(final IScope scope, final IShape source, final IShape target) {
		GamaSpatialGraph graph = (GamaSpatialGraph) getPlaces();
		IShape sourceN = source;
		IShape targetN = target;
		boolean targetNode = graph.getVertexMap().containsKey(target);
		boolean isAgentVertex = graph.isEmpty(scope) ? false : (graph.getVertices().get(0) instanceof IAgent);
		boolean targetNSame = isAgentVertex == (target instanceof IAgent);
		boolean sourceNSame = isAgentVertex == (source instanceof IAgent);
		boolean sourceNode = graph.getVertexMap().containsKey(source);
		if ((!sourceNSame && !sourceNode) || (!targetNSame && !targetNode)) {
			for (Object ed : graph.getVertices()) {
				if (((IShape) ed).getLocation().equals(source.getLocation())) {
					sourceN = (IShape)ed;
					sourceNode = true;
				}
				if (((IShape) ed).getLocation().equals(target.getLocation())) {
					targetN = (IShape)ed;
					targetNode = true;
				}
				if (sourceNode && targetNode) break;
			}
			
		} 
		if ( sourceNode && targetNode ) { return (GamaSpatialPath) graph.computeShortestPathBetween(scope, sourceN,
			targetN); }

		IShape edgeS = null, edgeT = null;

		if ( graph.isAgentEdge() ) {
			final IAgentFilter filter = In.edgesOf(getPlaces());
			if ( !sourceNode ) {
				edgeS = getPathEdge(scope,source);
				if (edgeS == null)
					edgeS = scope.getSimulationScope().getAgent().getTopology().getAgentClosestTo(scope, source, filter);
				// We avoid computing the target if we cannot find any source.
				if ( edgeS == null ) { return null; }
			}
			if ( !targetNode ) {
				edgeT = getPathEdge(scope,target);
				if (edgeT == null)
					edgeT = scope.getSimulationScope().getAgent().getTopology().getAgentClosestTo(scope, target, filter);
				if ( edgeT == null ) { return null; }
			}
		} else {
			double distSMin = Double.MAX_VALUE;
			double distTMin = Double.MAX_VALUE;
			edgeS = getPathEdge(scope,source);
			if (edgeS != null) distSMin = 0;
			edgeT = getPathEdge(scope,target);
			if (edgeT != null) distTMin = 0;
			if ((distSMin > 0  && !sourceNode) || (distTMin > 0 && !targetNode)) {
				for ( Object e : graph.getEdges() ) {
					IShape edge = (IShape) e;
					if ( !sourceNode && distSMin > 0 ) {
						double distS = edge.euclidianDistanceTo(source);
						if ( distS < distSMin ) {
							distSMin = distS;
							edgeS = edge;
						}
					}
					if ( !targetNode && distTMin > 0 ) {
						double distT = edge.euclidianDistanceTo(target);
						if ( distT < distTMin ) {
							distTMin = distT;
							edgeT = edge;
						}
					}
				}
			}
			if ( !sourceNode && edgeS == null || !targetNode && edgeT == null ) { return null; }
		}

		if ( getPlaces().isDirected() ) { return pathBetweenCommonDirected(scope, edgeS, edgeT, sourceN, targetN,
			sourceNode, targetNode); }

		return pathBetweenCommon(scope, graph, edgeS, edgeT, sourceN, targetN, sourceNode, targetNode);
	}
	
	public IShape getPathEdge(IScope scope, IShape ref) {
		if (ref.getAgent() != null) {
			IPath path = (GamaPath) ref.getAgent().getAttribute("current_path");
				if ( path != null && path.getTopology(scope) != null && path.getTopology(scope).equals(this) &&
					 ((IShape) path.getStartVertex()).getLocation().equals(ref.getLocation()) ) {
				int index = path.indexOf(ref.getAgent());
				if (index >= path.getEdgeList().size())
					return (IShape) path.getEdgeList().get(path.getEdgeList().size() - 1);
				return (IShape) path.getEdgeList().get(index);
			}
		}
		return null;
	}

	public GamaSpatialPath
		pathBetweenCommon(final IScope scope, final GamaSpatialGraph graph, final IShape edgeS, final IShape edgeT,
			final IShape source, final IShape target, final boolean sourceNode, final boolean targetNode) {
		IList<IShape> edges = GamaListFactory.create(Types.GEOMETRY);
		if ( sourceNode && !targetNode ) {
			IShape nodeT1 = (IShape) graph.getEdgeSource(edgeT);
			IShape nodeT2 = (IShape) graph.getEdgeTarget(edgeT);
			double l1 = 0;
			boolean computeOther = true;
			if ( nodeT1 == source ) {
				l1 = lengthEdge(edgeT, target, nodeT2, nodeT1);
			} else {
				edges = getPlaces().computeBestRouteBetween(scope, source, nodeT1);
				boolean isEmpty = edges.isEmpty();
				l1 = isEmpty ? Double.MAX_VALUE : pathlengthEdges(edges) + lengthEdge(edgeT, target, nodeT2, nodeT1);
				if ( !isEmpty ) {
					IShape el = edges.get(edges.size() - 1);
					if ( graph.getEdgeSource(el) == nodeT2 || graph.getEdgeTarget(el) == nodeT2 ) {
						// edges.remove(edges.size()-1);
						computeOther = false;
					}
				}
			}
			if ( computeOther ) {
				double l2 = 0;
				IList<IShape> edges2 = GamaListFactory.create(Types.GEOMETRY);
				if ( nodeT2 == source ) {
					l2 = lengthEdge(edgeT, target, nodeT1, nodeT2);
				} else {
					edges2 = getPlaces().computeBestRouteBetween(scope, source, nodeT2);
					l2 =
						edges2.isEmpty() ? Double.MAX_VALUE : pathlengthEdges(edges2) +
							lengthEdge(edgeT, target, nodeT1, nodeT2);
				}
				if ( l2 < l1 ) {
					edges = edges2;
					l1 = l2;
				}
			}
			if ( l1 == Double.MAX_VALUE ) { return null; }
			if ( edges.isEmpty() || edges.get(edges.size() - 1) != edgeT ) {
				edges.add(edgeT);
			}
		} else if ( !sourceNode && targetNode ) {
			IShape nodeS1 = (IShape) graph.getEdgeSource(edgeS);
			IShape nodeS2 = (IShape) graph.getEdgeTarget(edgeS);
			double l1 = 0;
			boolean computeOther = true;
			if ( nodeS1 == target ) {
				l1 = lengthEdge(edgeS, source, nodeS2, nodeS1);
			} else {
				edges = getPlaces().computeBestRouteBetween(scope, nodeS1, target);
				boolean isEmpty = edges.isEmpty();
				l1 = isEmpty ? Double.MAX_VALUE : pathlengthEdges(edges) + lengthEdge(edgeS, source, nodeS2, nodeS1);
				if ( !isEmpty ) {
					IShape e0 = edges.get(0);
					if ( graph.getEdgeSource(e0) == nodeS2 || graph.getEdgeTarget(e0) == nodeS2 ) {
						// edges.remove(0);
						computeOther = false;
					}
				}
			}
			if ( computeOther ) {
				double l2 = 0;
				IList<IShape> edges2 = GamaListFactory.create(Types.GEOMETRY);
				if ( nodeS2 == target ) {
					l2 = lengthEdge(edgeS, source, nodeS1, nodeS2);
				} else {
					edges2 = getPlaces().computeBestRouteBetween(scope, nodeS2, target);
					l2 =
						edges2.isEmpty() ? Double.MAX_VALUE : pathlengthEdges(edges2) +
							lengthEdge(edgeS, source, nodeS1, nodeS2);
				}
				if ( l2 < l1 ) {
					edges = edges2;
					l1 = l2;
				}
			}
			if ( l1 == Double.MAX_VALUE ) { return null; }
			if ( edges.isEmpty() || edges.get(0) != edgeS ) {
				edges.add(0, edgeS);
			}
		} else {
			IShape nodeS1 = (IShape) graph.getEdgeSource(edgeS);
			IShape nodeS2 = (IShape) graph.getEdgeTarget(edgeS);
			IShape nodeT1 = (IShape) graph.getEdgeSource(edgeT);
			IShape nodeT2 = (IShape) graph.getEdgeTarget(edgeT);

			double lmin = Double.MAX_VALUE;

			boolean computeS1T2 = true;
			boolean computeS2T1 = true;
			boolean computeS2T2 = true;
			if ( nodeS1 == nodeT1 ) {
				lmin = lengthEdge(edgeS, source, nodeS2, nodeS1) + lengthEdge(edgeT, target, nodeT2, nodeT1);
			} else {
				edges = getPlaces().computeBestRouteBetween(scope, nodeS1, nodeT1);
				boolean isEmpty = edges.isEmpty();

				double els = lengthEdge(edgeS, source, nodeS2, nodeS1);
				double elt = lengthEdge(edgeT, target, nodeT2, nodeT1);
				lmin = isEmpty ? Double.MAX_VALUE : pathlengthEdges(edges) + els + elt;

				if ( !isEmpty ) {
					IShape e0 = edges.get(0);
					IShape el = edges.get(edges.size() - 1);
					if ( e0 != el ) {
						boolean ts1 = graph.getEdgeSource(e0) == nodeS2 || graph.getEdgeTarget(e0) == nodeS2;
						boolean ts2 = graph.getEdgeSource(el) == nodeT2 || graph.getEdgeTarget(el) == nodeT2;
						double valmin = lmin;
						if ( ts1 ) {
							computeS2T1 = false;
							double val = lmin - els - e0.getPerimeter() + lengthEdge(edgeS, source, nodeS1, nodeS2);
							if ( valmin > val ) {
								valmin = val;
							}
						}
						if ( ts2 ) {
							computeS1T2 = false;
							double val = lmin - elt - el.getPerimeter() + lengthEdge(edgeT, target, nodeT1, nodeT2);
							if ( valmin > val ) {
								valmin = val;
							}
						}
						if ( ts1 && ts2 ) {
							computeS2T2 = false;
							double val =
								lmin - els - e0.getPerimeter() - elt - el.getPerimeter() +
									lengthEdge(edgeS, source, nodeS1, nodeS2) +
									lengthEdge(edgeT, target, nodeT1, nodeT2);
							if ( valmin > val ) {
								valmin = val;
							}
						}
						lmin = valmin;
					}
				}
			}
			if ( computeS2T1 ) {
				double l2 = 0;
				IList<IShape> edges2 = GamaListFactory.create(Types.GEOMETRY);
				if ( nodeS2 == nodeT1 ) {
					l2 = lengthEdge(edgeS, source, nodeS1, nodeS2) + lengthEdge(edgeT, target, nodeT2, nodeT1);
				} else {
					edges2 = getPlaces().computeBestRouteBetween(scope, nodeS2, nodeT1);
					boolean isEmpty = edges2.isEmpty();
					double els = lengthEdge(edgeS, source, nodeS1, nodeS2);
					double elt = lengthEdge(edgeT, target, nodeT2, nodeT1);

					l2 = isEmpty ? Double.MAX_VALUE : pathlengthEdges(edges2) + els + elt;
					if ( !isEmpty ) {
						IShape e0 = edges2.get(0);
						IShape el = edges2.get(edges2.size() - 1);
						if ( e0 != el ) {
							boolean ts1 = graph.getEdgeSource(e0) == nodeS1 || graph.getEdgeTarget(e0) == nodeS1;

							boolean ts2 = graph.getEdgeSource(el) == nodeT2 || graph.getEdgeTarget(el) == nodeT2;
							double valmin = l2;
							if ( ts2 ) {
								computeS2T2 = false;
								double val = l2 - elt - el.getPerimeter() + lengthEdge(edgeT, target, nodeT1, nodeT2);
								if ( valmin > val ) {
									valmin = val;
								}
							}
							if ( ts1 && ts2 ) {
								computeS1T2 = false;
								double val =
									l2 - els - e0.getPerimeter() - elt - el.getPerimeter() +
										lengthEdge(edgeS, source, nodeS2, nodeS1) +
										lengthEdge(edgeT, target, nodeT1, nodeT2);
								if ( valmin > val ) {
									valmin = val;
								}
							}
							l2 = valmin;
						}

					}
				}
				if ( l2 < lmin ) {
					edges = edges2;
					lmin = l2;
				}
			}
			if ( computeS1T2 ) {

				double l2 = 0;
				IList<IShape> edges2 = GamaListFactory.create(Types.GEOMETRY);
				if ( nodeS1 == nodeT2 ) {
					l2 = lengthEdge(edgeS, source, nodeS2, nodeS1) + lengthEdge(edgeT, target, nodeT1, nodeT2);
				} else {
					edges2 = getPlaces().computeBestRouteBetween(scope, nodeS1, nodeT2);
					boolean isEmpty = edges2.isEmpty();
					double els = lengthEdge(edgeS, source, nodeS2, nodeS1);
					double elt = lengthEdge(edgeT, target, nodeT1, nodeT2);
					l2 = isEmpty ? Double.MAX_VALUE : pathlengthEdges(edges2) + els + elt;
					if ( !isEmpty ) {
						IShape e0 = edges2.get(0);
						boolean ts1 = graph.getEdgeSource(e0) == nodeS2 || graph.getEdgeTarget(e0) == nodeS2;
						if ( ts1 ) {
							computeS2T2 = false;
							double val = l2 - els - e0.getPerimeter() + lengthEdge(edgeS, source, nodeS1, nodeS2);
							if ( l2 > val ) {
								l2 = val;
							}
						}

					}
				}
				if ( l2 < lmin ) {
					edges = edges2;
					lmin = l2;
				}
			}
			if ( computeS2T2 ) {
				double l2 = 0;
				IList<IShape> edges2 = GamaListFactory.create(Types.GEOMETRY);
				if ( nodeS2 == nodeT2 ) {
					l2 = lengthEdge(edgeS, source, nodeS1, nodeS2) + lengthEdge(edgeT, target, nodeT1, nodeT2);
				} else {
					edges2 = getPlaces().computeBestRouteBetween(scope, nodeS2, nodeT2);
					l2 =
						edges2.isEmpty() ? Double.MAX_VALUE : pathlengthEdges(edges2) +
							lengthEdge(edgeS, source, nodeS1, nodeS2) + lengthEdge(edgeT, target, nodeT1, nodeT2);
				}

				if ( l2 < lmin ) {
					edges = edges2;
					lmin = l2;
				}
			}
			if ( lmin == Double.MAX_VALUE ) { return null; }
			if ( edges.isEmpty() || edges.get(0) != edgeS ) {
				edges.add(0, edgeS);
			}

			if ( edges.get(edges.size() - 1) != edgeT ) {
				edges.add(edgeT);
			}

		}
		return PathFactory.newInstance(this, source, target, edges);
	}

	GamaSpatialPath pathFromEdgesUndirected(final IScope scope, IList<IShape> edges, final IShape edgeS,
		final IShape edgeT, final IShape source, final IShape target, final boolean sourceNode,
		final boolean targetNode, final IShape nodeS, final IShape nodeSbis, final IShape nodeT,
		final boolean computeOther) {
		if ( edges.isEmpty() || edges.get(0) == null ) { return null; }
		if ( !sourceNode ) {
			Set edgesSetInit = new THashSet(Arrays.asList(edges.get(0).getInnerGeometry().getCoordinates()));
			final Set edgesSetS = new THashSet(Arrays.asList(edgeS.getInnerGeometry().getCoordinates()));
			if ( !edgesSetS.equals(edgesSetInit) ) {
				double l1 = 0;
				double l2 = 1;
				IList<IShape> edgesbis = null;
				if ( computeOther ) {
					l1 = pathlengthEdges(edges) + lengthEdge(edgeS, source, nodeSbis, nodeS);
					edgesbis = getPlaces().computeBestRouteBetween(scope, nodeSbis, nodeT);
					l2 = pathlengthEdges(edgesbis) + lengthEdge(edgeS, source, nodeS, nodeSbis);
				}
				if ( l1 < l2 || edgesbis == null || edgesbis.isEmpty() || edgesbis.get(0) == null ) {
					edges.add(0, edgeS);
				} else {
					edges = edgesbis;
					edgesSetInit = new THashSet(Arrays.asList(edges.get(0).getInnerGeometry().getCoordinates()));
					if ( !edgesSetS.equals(edgesSetInit) ) {
						edges.add(0, edgeS);
					}
				}

			}
		}
		if ( !targetNode ) {
			final Set edgesSetEnd =
				new THashSet(Arrays.asList(edges.get(edges.size() - 1).getInnerGeometry().getCoordinates()));
			final Set edgesSetT = new THashSet(Arrays.asList(edgeT.getInnerGeometry().getCoordinates()));

			if ( !edgesSetT.equals(edgesSetEnd) ) {
				edges.add(edgeT);
			}
		}

		// return new GamaPath(this, source, target, edges);
		return PathFactory.newInstance(this, source, target, edges);
	}

	public double pathlengthEdges(final IList<IShape> edges) {
		double length = 0;
		for ( IShape sp : edges ) {
			length += sp.getPerimeter();
		}
		return length;
	}

	public double lengthEdge(final IShape edge, final IShape location, final IShape source, final IShape target) {
		double dist = source.getLocation().euclidianDistanceTo(target.getLocation());
		return dist == 0 ? 0 : edge.getPerimeter() * location.euclidianDistanceTo(target.getLocation()) / dist;
	}

	public GamaSpatialPath pathBetweenCommonDirected(final IScope scope, final IShape edgeS, final IShape edgeT,
		final IShape source, final IShape target, final boolean sourceNode, final boolean targetNode) {
		IList<IShape> edges;

		if ( !sourceNode && !targetNode && edgeS.equals(edgeT) ) {
			GamaPoint ptS = new GamaPoint(edgeS.getInnerGeometry().getCoordinates()[0]);
			if ( source.euclidianDistanceTo(ptS) < target.euclidianDistanceTo(ptS) ) {
				edges = GamaListFactory.create(Types.GEOMETRY);
				edges.add(edgeS);
				return PathFactory.newInstance(this, source, target, edges);
			}
		}
		IShape nodeS = sourceNode ? source : getPlaces().getEdgeTarget(edgeS);
		IShape nodeT = targetNode ? target : getPlaces().getEdgeSource(edgeT);

		if ( nodeS.equals(nodeT) ) {
			edges = GamaListFactory.create(Types.GEOMETRY);
			if ( edgeS != null ) {
				edges.add(edgeS);
			}
			if ( edgeT != null ) {
				edges.add(edgeT);
			}
			return PathFactory.newInstance(this, source, target, edges);
		}
		edges = getPlaces().computeBestRouteBetween(scope, nodeS, nodeT);
		if ( edges.isEmpty() || edges.get(0) == null ) { return null; }

		if ( !sourceNode ) {
			edges.add(0, edgeS);
		}
		if ( !targetNode ) {
			edges.add(edges.size(), edgeT);
		}

		// return new GamaPath(this, source, target, edges);
		return PathFactory.newInstance(this, source, target, edges);
	}
	
	public GamaSpatialPath pathBetween(final IScope scope, final IShape source, final IShape target, final IShape existingEdge) {
			IList<IShape> edges;
			if ( source.equals(target) ) {
				edges = GamaListFactory.create(Types.GEOMETRY);
				if (existingEdge != null)
					edges.add(existingEdge);
				return PathFactory.newInstance(this, source, target, edges);
			}
			edges = getPlaces().computeBestRouteBetween(scope, source, target);
			if (existingEdge != null)
				edges.add(0, existingEdge);
			if ( edges.isEmpty() || edges.get(0) == null ) { return null; }
			
			return PathFactory.newInstance(this, source, target, edges);
		}

	@Override
	public GamaSpatialPath pathBetween(final IScope scope, final ILocation source, final ILocation target) {
		return pathBetween(scope, (IShape) source, (IShape) target);
	}

	/**
	 * @see msi.gama.interfaces.IValue#stringValue()
	 */
	@Override
	public String stringValue(final IScope scope) throws GamaRuntimeException {
		return "GraphTopology";
	}

	/**
	 * @see msi.gama.environment.AbstractTopology#_toGaml()
	 */
	@Override
	protected String _toGaml(final boolean includingBuiltIn) {
		return "GraphTopology";
	}

	/**
	 * @see msi.gama.environment.AbstractTopology#_copy()
	 */
	@Override
	protected ITopology _copy(final IScope scope) {
		return new GraphTopology(scope, environment, (GamaSpatialGraph) places);
	}

	/**
	 * @see msi.gama.environment.AbstractTopology#getRandomPlace()
	 */

	@Override
	public ISpatialGraph getPlaces() {
		return (GamaSpatialGraph) super.getPlaces();
	}

	/**
	 * @see msi.gama.environment.ITopology#isValidLocation(msi.gama.util.GamaPoint)
	 */
	@Override
	public boolean isValidLocation(final IScope scope, final ILocation p) {
		return isValidGeometry(scope, p.getGeometry());
	}

	/**
	 * @see msi.gama.environment.ITopology#isValidGeometry(msi.gama.interfaces.IGeometry)
	 */
	@Override
	public boolean isValidGeometry(final IScope scope, final IShape g) {
		// Geometry g2 = g.getInnerGeometry();
		for ( final IShape g1 : places.iterable(scope) ) {
			if ( g1.intersects(g) ) { return true; }
			// TODO covers or intersects ?
		}
		return false;
	}

	/**
	 * @throws GamaRuntimeException
	 * @see msi.gama.environment.ITopology#distanceBetween(msi.gama.interfaces.IGeometry, msi.gama.interfaces.IGeometry, java.lang.Double)
	 */
	@Override
	public Double distanceBetween(final IScope scope, final IShape source, final IShape target) {
		final GamaSpatialPath path = this.pathBetween(scope, source, target);
		if ( path == null ) { return Double.MAX_VALUE; }
		if (path.getEdgeList().isEmpty()) return 0.0;
		Coordinate[] coordsSource = path.getEdgeList().get(0).getInnerGeometry().getCoordinates();
		Coordinate[] coordsTarget = path.getEdgeList().get(path.getEdgeList().size() - 1).getInnerGeometry().getCoordinates();
		if ( coordsSource.length == 0 || coordsTarget.length == 0 ) { return Double.MAX_VALUE; }
		GamaPoint sourceEdges = new GamaPoint(coordsSource[0]);
		GamaPoint targetEdges = new GamaPoint(coordsTarget[coordsTarget.length - 1]);
		path.setSource(sourceEdges);
		path.setTarget(targetEdges);
		return path.getDistance(scope);
	}

	@Override
	public Double distanceBetween(final IScope scope, final ILocation source, final ILocation target) {
		final GamaSpatialPath path = this.pathBetween(scope, source, target);
		if ( path == null ) { return Double.MAX_VALUE; }
		if (path.getEdgeList().isEmpty()) return 0.0;
		Coordinate[] coordsSource = path.getEdgeList().get(0).getInnerGeometry().getCoordinates();
		Coordinate[] coordsTarget = path.getEdgeList().get(path.getEdgeList().size() - 1).getInnerGeometry().getCoordinates();
		if ( coordsSource.length == 0 || coordsTarget.length == 0 ) { return Double.MAX_VALUE; }
		GamaPoint sourceEdges = new GamaPoint(coordsSource[0]);
		GamaPoint targetEdges = new GamaPoint(coordsTarget[coordsTarget.length - 1]);
		path.setSource(sourceEdges);
		path.setTarget(targetEdges);
		return path.getDistance(scope);
	}

	/**
	 * @throws GamaRuntimeException
	 * @see msi.gama.environment.ITopology#directionInDegreesTo(msi.gama.interfaces.IGeometry, msi.gama.interfaces.IGeometry)
	 */
	@Override
	public Integer directionInDegreesTo(final IScope scope, final IShape source, final IShape target) {
		// WARNING As it is computed every time the location of an agent is set, and as the source and target in that
		// case do not correspond to existing nodes, it may be safer (and faster) to call the root topology
		return root.directionInDegreesTo(scope, source, target);
		// final GamaSpatialPath path = this.pathBetween(scope, source, target);
		// if ( path == null ) { return null; }
		// // LineString ls = (LineString) path.getEdgeList().first().getInnerGeometry();
		// // TODO Check this
		// final double dx = target.getLocation().getX() - source.getLocation().getX();
		// final double dy = target.getLocation().getY() - source.getLocation().getY();
		// final double result = Maths.atan2Opt(dy, dx);
		// return Maths.checkHeading((int) result);
	}

	/**
	 * @see msi.gama.environment.ITopology#getAgentsIn(msi.gama.interfaces.IGeometry, msi.gama.environment.IAgentFilter, boolean)
	 */
	@Override
	public Collection<IAgent> getAgentsIn(final IScope scope, final IShape source, final IAgentFilter f,
		final boolean covered) {
		Collection<IAgent> result = super.getAgentsIn(scope, source, f, covered);
		Iterator<IAgent> it = result.iterator();
		while (it.hasNext()) {
			IAgent ag = it.next();
			if ( ag.dead() || !isValidGeometry(scope, ag) ) {
				it.remove();
			}
		}
		return result;
	}

	@Override
	public boolean isTorus() {
		// TODO Why is it the case ?
		return false;
	}

	@Override
	public List KpathsBetween(final IScope scope, final IShape source, final IShape target, final int k) {
		ISpatialGraph graph = getPlaces();
		boolean sourceNode = graph.containsVertex(source);
		boolean targetNode = graph.containsVertex(target);
		if ( sourceNode && targetNode ) { return graph.computeKShortestPathsBetween(scope, source, target, k); }

		IShape edgeS = null, edgeT = null;

		final IAgentFilter filter = In.edgesOf(getPlaces());

		if ( !sourceNode ) {
			if (getPlaces().getEdgeSpecies() != null) edgeS = getAgentClosestTo(scope, source, filter);
			else edgeS = shapeClosest(this.getPlaces().getEdges(), source);
			
			// We avoid computing the target if we cannot find any source.
			if ( edgeS == null ) { return null; }
		}
		if ( !targetNode ) {
			if (getPlaces().getEdgeSpecies() != null) edgeT = getAgentClosestTo(scope, target, filter);
			else edgeT = shapeClosest(this.getPlaces().getEdges(), target);
			if ( edgeT == null ) { return null; }
		}

		if ( getPlaces().isDirected() ) { return KpathsBetweenCommonDirected(scope, edgeS, edgeT, source, target,
			sourceNode, targetNode, k); }

		return KpathsBetweenCommon(scope, edgeS, edgeT, source, target, sourceNode, targetNode, k);
	}
	
	public IShape shapeClosest(List<IShape> shapes, IShape geom){
		IShape cp = null;
		double distMin = Double.MAX_VALUE;
		for (IShape shp : shapes) {
			double dist = shp.euclidianDistanceTo(geom);
			if (dist < distMin) {
				distMin = dist;
				cp = shp;
			}
		}
		return cp;
	}

	@Override
	public List<GamaSpatialPath> KpathsBetween(final IScope scope, final ILocation source, final ILocation target,
		final int k) {
		return KpathsBetween(scope, source.getGeometry(), target.getGeometry(), k);
	}

	public List KpathsBetweenCommon(final IScope scope, final IShape edgeS, final IShape edgeT, final IShape source,
		final IShape target, final boolean sourceNode, final boolean targetNode, final int k) {
		IShape nodeS = source;
		IShape nodeSbis = source;
		IShape nodeT = target;

		if ( !targetNode ) {
			IShape t1 = null;
			IShape t2 = null;
			t1 = getPlaces().getEdgeSource(edgeT);
			t2 = getPlaces().getEdgeTarget(edgeT);
			if ( t1 == null || t2 == null ) { return null; }
			nodeT = t1;
			if ( t1.getLocation().euclidianDistanceTo(target.getLocation()) > t2.getLocation().euclidianDistanceTo(
				target.getLocation()) ) {
				nodeT = t2;
			}
		}
		if ( !sourceNode ) {
			IShape s1 = null;
			IShape s2 = null;
			s1 = getPlaces().getEdgeSource(edgeS);
			s2 = getPlaces().getEdgeTarget(edgeS);
			if ( s1 == null || s2 == null ) { return null; }
			nodeS = s1;
			nodeSbis = s2;
			if ( s1.equals(nodeT) ||
				!s2.equals(nodeT) &&
				s1.getLocation().euclidianDistanceTo(source.getLocation()) > s2.getLocation().euclidianDistanceTo(
					source.getLocation()) ) {
				nodeS = s2;
				nodeSbis = s1;
			}
		}
		List<IList<IShape>> edgesList = getPlaces().computeKBestRoutesBetween(scope, nodeS, nodeT, k);
		List results = GamaListFactory.create(Types.PATH);
		for ( IList<IShape> edges : edgesList ) {
			GamaSpatialPath pp =
				pathFromEdgesUndirected(scope, edges, edgeS, edgeT, source, target, sourceNode, targetNode, nodeS,
					nodeSbis, nodeT, false);
			if ( pp != null ) {
				results.add(pp);
			}

		}
		Collections.sort(results);
		return results;
	}

	public List KpathsBetweenCommonDirected(final IScope scope, final IShape edgeS, final IShape edgeT,
		final IShape source, final IShape target, final boolean sourceNode, final boolean targetNode, final int k) {
		List results = GamaListFactory.create(Types.PATH);
		if ( edgeS.equals(edgeT) ) {
			GamaPoint ptS = new GamaPoint(edgeS.getInnerGeometry().getCoordinates()[0]);
			if ( source.euclidianDistanceTo(ptS) < target.euclidianDistanceTo(ptS) ) {
				IList<IShape> edges = GamaListFactory.create(Types.GEOMETRY);
				edges.add(edgeS);
				results.add(PathFactory.newInstance(this, source, target, edges));
				return results;
			}
		}
		IShape nodeS = sourceNode ? source : getPlaces().getEdgeTarget(edgeS);
		IShape nodeT = targetNode ? target : getPlaces().getEdgeSource(edgeT);

		if ( nodeS.equals(nodeT) ) {
			IList<IShape> edges = GamaListFactory.create(Types.GEOMETRY);
			edges.add(edgeS);
			edges.add(edgeT);
			results.add(PathFactory.newInstance(this, source, target, edges));
			return results;
		}
		List<IList<IShape>> edgesList = getPlaces().computeKBestRoutesBetween(scope, nodeS, nodeT, k);
		for ( IList<IShape> edges : edgesList ) {
			edges = getPlaces().computeBestRouteBetween(scope, nodeS, nodeT);
			if ( edges.isEmpty() || edges.get(0) == null ) {
				continue;
			}

			if ( !sourceNode ) {
				edges.add(0, edgeS);
			}
			if ( !targetNode ) {
				edges.add(edges.size(), edgeT);
			}

			// return new GamaPath(this, source, target, edges);
			GamaSpatialPath pp = PathFactory.newInstance(this, source, target, edges);
			if ( pp != null ) {
				results.add(pp);
			}
		}
		Collections.sort(results);
		return results;

	}
	
	@Override
	public Collection<IAgent> getNeighborsOf(final IScope scope, final IShape source, final Double distance,
		final IAgentFilter filter) throws GamaRuntimeException {
		final ISpatialGraph graph = this.getPlaces();
		boolean searchEdges = false;
		boolean searchVertices = false;
		final Set<IAgent> agents = new THashSet<IAgent>();
		IShape realS = null;
		IShape tcr =  null;
		if (graph.containsEdge(source)) realS = source;
		else {
			double minDist = Double.POSITIVE_INFINITY;
			for (IShape e : graph.getEdges()) {
				double d = e.euclidianDistanceTo(source) ;
				if (d < minDist ) {
					minDist = d;
					tcr = e;
				}
			}
			if (source.euclidianDistanceTo(graph.getEdgeSource(tcr)) < source.euclidianDistanceTo(graph.getEdgeTarget(tcr))) {
				realS = graph.getEdgeSource(tcr);
			} else {
				realS = graph.getEdgeTarget(tcr);
			}
		}
		if (filter.getSpecies() != null) {
			searchEdges = filter.getSpecies() == graph.getEdgeSpecies();
			searchVertices = filter.getSpecies() == graph.getVertexSpecies();
		}
		if (searchEdges) {
			Set<IShape> edgs = getNeighborsOfRec(scope, realS,true,distance, graph,new THashSet<IShape>());
			for (IShape ed : edgs ) agents.add(ed.getAgent());
			return agents; 
		} else if (searchVertices) {
			Set<IShape> nds =  getNeighborsOfRec(scope, realS,false,distance, graph,new THashSet<IShape>());
			for (IShape nd : nds ) agents.add(nd.getAgent());
			return agents;
		}
		IContainer agentsTotest = null;
		if(filter.getSpecies() != null) 
			agentsTotest = filter.getSpecies().getAgents(scope);
		else 
			agentsTotest = scope.getSimulationScope().getAgents(scope);
		final Set<IShape> edges = getNeighborsOfRec(scope, realS,true,distance, graph, new THashSet<IShape>());	
		for (Object ob : agentsTotest.iterable(scope)) {
			IShape ag = (IShape) ob;
			if (filter.accept(scope, source, ag)) {
				IShape rd = null;
				if (graph.getEdges().contains(ag)) rd = ag;
				else {
					double minDist = Double.POSITIVE_INFINITY;
					for (IShape e : graph.getEdges()) {
						double d = e.euclidianDistanceTo(ag) ;
						if (d < minDist ) {
							minDist = d;
							rd = e;
						}
					}
				}
					
				if (edges.contains(rd) && this.distanceBetween(scope, source, ag) <= distance) {
					agents.add(ag.getAgent());
				}
					
			}
		}
		
		return agents;

	}
	
	public Set<IShape> getNeighborsOfRec(final IScope scope, final IShape currentSource, 
		final boolean edge, double currentDist,ISpatialGraph graph, Set<IShape> alr) throws GamaRuntimeException {
		final Set<IShape> edges = new THashSet<IShape>();
		Set<IShape> eds = (graph.isDirected()) ? graph.outgoingEdgesOf(currentSource) : graph.edgesOf(currentSource);
		if (!edge) edges.add(currentSource.getAgent());
		for (IShape ed : eds){
			if (alr.contains(ed)) continue;
			alr.add(ed);
			double dist = ed.getPerimeter();
			if (edge) edges.add(ed);
			if ((currentDist - dist) > 0) {
				IShape nextNode = null;
				if (graph.isDirected()) nextNode = graph.getEdgeTarget(ed);
				else nextNode = (currentSource == graph.getEdgeTarget(ed)) ? graph.getEdgeSource(ed) : graph.getEdgeTarget(ed);
				edges.addAll(getNeighborsOfRec(scope, nextNode,edge,currentDist - dist, graph,alr));
			}
		}
		return edges;
	}
	
	@Override
	public IAgent getAgentClosestTo(final IScope scope, final IShape source, final IAgentFilter filter) {
		//A better solution is required !!! this solution is just here to ensure the consistency of the closest operator on graph !
		
		List<IAgent> listAgents  = null;
		if (filter instanceof GamaSpatialGraph) {
			listAgents = new ArrayList<IAgent>(filter.getAgents(scope).listValue(scope, Types.AGENT, false));
		} else 	if (filter.getSpecies() != null) {
			listAgents = new ArrayList<IAgent>(filter.getSpecies().getAgents(scope).listValue(scope, Types.AGENT, false));
		} else if (filter instanceof In) {
			listAgents = new ArrayList<IAgent>(filter.getAgents(scope).listValue(scope, Types.AGENT, false));
		} else {
			listAgents = new ArrayList<IAgent>(scope.getSimulationScope().getAgents(scope).listValue(scope, Types.AGENT, false));
		}
		listAgents.remove(source);
		IAgent closest = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (IAgent ag : listAgents) {
			Double dist = this.distanceBetween(scope, source, ag);
			if (dist != null && dist < minDist) {
				if (dist == 0) return ag;
				closest = ag;
				minDist = dist;
			}
		}
		return closest;
	}
}
