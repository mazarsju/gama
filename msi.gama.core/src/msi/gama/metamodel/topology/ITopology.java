/*********************************************************************************************
 *
 *
 * 'ITopology.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.metamodel.topology;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.common.interfaces.IValue;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.metamodel.shape.IShape;
import msi.gama.metamodel.topology.filter.IAgentFilter;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.var;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.IContainer;
import msi.gama.util.path.GamaSpatialPath;
import msi.gaml.types.IType;

/**
 * Written by drogoul Modified on 4 juil. 2011
 *
 * @todo Description
 *
 */
@vars({ @var(name = IKeyword.ENVIRONMENT, type = IType.GEOMETRY, doc = {
		@doc("Returns the environment of this topology, either an agent or a geometry, which defines its boundaries") }),
		@var(name = IKeyword.PLACES, type = IType.CONTAINER, of = IType.GEOMETRY, doc = {
				@doc("Returns the list of discrete places that compose this topology (e.g. the list of cells for a grid topology). The continuous topologies will return a singleton list with their environment") }),
		// Could be replaced by "geometries"
		/*
		 * Normally not necessary as it is inherited from GamaGeometry @var(name
		 * = GamaPath.POINTS, type = IType.LIST, of = IType.POINT)
		 */
})
public interface ITopology extends IValue {

	public abstract ISpatialIndex getSpatialIndex();

	public abstract void initialize(IScope scope, IPopulation pop) throws GamaRuntimeException;

	void updateAgent(Envelope previous, IAgent agent);

	public abstract void removeAgent(final IAgent agent);

	public abstract List<Geometry> listToroidalGeometries(final Geometry geom);

	public abstract IAgent getAgentClosestTo(IScope scope, final IShape source, IAgentFilter filter);

	public abstract IAgent getAgentFarthestTo(IScope scope, final IShape source, IAgentFilter filter);

	public abstract Collection<IAgent> getNeighborsOf(IScope scope, final IShape source, final Double distance,
			IAgentFilter filter) throws GamaRuntimeException;

	public abstract Collection<IAgent> getAgentsIn(IScope scope, final IShape source, final IAgentFilter f,
			boolean covered);

	public abstract boolean isTorus();

	/**
	 * @throws GamaRuntimeException
	 *             Distance between two geometries in this topology.
	 *
	 * @param source
	 *            the source geometry (cannot be null)
	 * @param target
	 *            the target (cannot be null)
	 * @return a double representing the distance between the two geometries, or
	 *         Double.MAX_VALUE if either one of them is not reachable from this
	 *         topology
	 */
	public abstract Double distanceBetween(IScope scope, final IShape source, final IShape target);

	public abstract Double distanceBetween(IScope scope, final ILocation source, final ILocation target);

	public abstract GamaSpatialPath pathBetween(IScope scope, final IShape source, final IShape target)
			throws GamaRuntimeException;

	public abstract GamaSpatialPath pathBetween(IScope scope, final ILocation source, final ILocation target)
			throws GamaRuntimeException;

	/**
	 * Return the location corresponding to a displacement from source, with an
	 * angle (in degrees) given by direction and a given distance. The returned
	 * point is a valid local location of this topology or null.
	 *
	 * @param source
	 *            the point from which the destination is computed
	 * @param direction
	 *            an angle in degrees (between 0 and 360 -- other values will be
	 *            normalized
	 * @param distance
	 *            the distance that should separate the actual location and the
	 *            destination
	 * @param nullIfOutside
	 *            tells wether to return the destination point or null if the
	 *            destination is outside the topology
	 * @return a point or null if no random locations are available
	 */
	public abstract ILocation getDestination(final ILocation source, final int direction, final double distance,
			boolean nullIfOutside);

	/**
	 * Return the location corresponding to a displacement from source, with an
	 * angle (in degrees) given by direction and a given distance. The returned
	 * point is a valid local location of this topology or null.
	 *
	 * @param source
	 *            the point from which the destination is computed
	 * @param direction
	 *            an angle in degrees (between 0 and 360 -- other values will be
	 *            normalized
	 * @param distance
	 *            the distance that should separate the actual location and the
	 *            destination
	 * @param nullIfOutside
	 *            tells wether to return the destination point or null if the
	 *            destination is outside the topology
	 * @return a point or null if no random locations are available
	 */
	public abstract ILocation getDestination3D(final ILocation source, final int heading, final int pitch,
			final double distance, boolean nullIfOutside);

	/**
	 * Return a random location inside the bounds of the environment's shape.
	 * The returned point is a valid local location of this topology.
	 *
	 * @return a point, or null if no random locations are available
	 */
	public abstract ILocation getRandomLocation(IScope scope);

	/**
	 * Return the collection of places (IGeometry) defined by this topology. For
	 * continuous topologies, it is a GamaList with their environment. For
	 * discrete topologies, it can be any of the container supporting the
	 * inclusion of geometries (GamaList, GamaSpatialGraph, GamaMap,
	 * GamaSpatialMatrix)
	 *
	 * @return an instance of IGamaContainer, which geometries can be iterated.
	 */
	@getter(IKeyword.PLACES)
	public abstract IContainer<?, IShape> getPlaces();

	/**
	 * Return the environment of this topology (i.e. the IGeometry that defines
	 * its boundaries).
	 *
	 * @return an instance of IGeometry.
	 */
	@getter(IKeyword.ENVIRONMENT)
	public abstract IShape getEnvironment();

	/**
	 * Normalizes a location so that the returned location is inside the bounds
	 * of the topology. The returned point is a valid local location of this
	 * topology.
	 *
	 * @param p
	 *            the location to normalize
	 * @param nullIfOutside
	 *            tells whether to return null or to coerce p if p is outside
	 *            the bounds of the topology
	 * @return a valid point or null if nullIfOutside is true and the point is
	 *         outside
	 */
	public abstract ILocation normalizeLocation(final ILocation p, boolean nullIfOutside);

	/**
	 * @throws GamaRuntimeException
	 *             Called by a population to tell this topology that the shape
	 *             of its host has changed. If the environment of the topology
	 *             depends on the shape of the host, the topology can choose to
	 *             adapt in consequence.
	 *
	 * @param pop
	 *            the population to which this topology is attached.
	 */
	// public abstract void shapeChanged(IPopulation pop);

	public abstract double getWidth();

	public abstract double getHeight();

	public abstract void dispose();

	public abstract boolean isValidLocation(IScope scope, ILocation p);

	public abstract boolean isValidGeometry(IScope scope, IShape g);

	/**
	 * @param scope
	 *            TODO
	 * @throws GamaRuntimeException
	 * @param source
	 * @param target
	 * @return the direction or null if one these two geometries are invalid in
	 *         this topology
	 */
	public abstract Integer directionInDegreesTo(IScope scope, IShape source, IShape target);

	public abstract List<GamaSpatialPath> KpathsBetween(IScope scope, IShape source, IShape target, int k);

	public abstract List<GamaSpatialPath> KpathsBetween(IScope scope, ILocation source, ILocation target, int k);

}