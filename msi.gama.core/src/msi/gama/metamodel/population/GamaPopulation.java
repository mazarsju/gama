/*********************************************************************************************
 *
 *
 * 'GamaPopulation.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.metamodel.population;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.agent.IMacroAgent;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.metamodel.shape.IShape;
import msi.gama.metamodel.topology.ITopology;
import msi.gama.metamodel.topology.continuous.ContinuousTopology;
import msi.gama.metamodel.topology.filter.IAgentFilter;
import msi.gama.metamodel.topology.filter.In;
import msi.gama.metamodel.topology.graph.GamaSpatialGraph;
import msi.gama.metamodel.topology.graph.GraphTopology;
import msi.gama.metamodel.topology.grid.GamaSpatialMatrix;
import msi.gama.metamodel.topology.grid.GridTopology;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaList;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IContainer;
import msi.gama.util.IList;
import msi.gama.util.file.GamaGridFile;
import msi.gama.util.graph.AbstractGraphNodeAgent;
import msi.gaml.architecture.IArchitecture;
import msi.gaml.compilation.IAgentConstructor;
import msi.gaml.descriptions.SpeciesDescription;
import msi.gaml.descriptions.TypeDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.species.ISpecies;
import msi.gaml.statements.IExecutable;
import msi.gaml.types.GamaTopologyType;
import msi.gaml.types.IType;
import msi.gaml.types.Types;
import msi.gaml.variables.IVariable;

/**
 * Written by drogoul Modified on 6 sept. 2010
 *
 * @todo Description
 *
 */
public class GamaPopulation extends GamaList<IAgent> implements IPopulation {

	public static GamaPopulation createPopulation(final IScope scope, final IMacroAgent host, final ISpecies species) {
		if (species.isGrid()) {
			final IExpression exp = species.getFacet("use_regular_agents");
			final boolean useRegularAgents = exp == null || Cast.asBool(scope, exp.value(scope));
			// if ( useRegularAgents ) { return new GamaPopulation(host,
			// species); }
			// In case of grids, we build the topology first if we use the
			// minimal agents
			final ITopology t = buildGridTopology(scope, species, host);
			final GamaSpatialMatrix m = (GamaSpatialMatrix) t.getPlaces();
			return m.new GridPopulation(t, host, species, useRegularAgents);
		}
		return new GamaPopulation(host, species);
	}

	/**
	 * The agent hosting this population which is considered as the direct
	 * macro-agent.
	 */
	protected IMacroAgent host;

	/**
	 * The object describing how the agents of this population are spatially
	 * organized
	 */
	protected ITopology topology;
	protected final ISpecies species;
	protected final String[] orderedVarNames;
	protected final IVariable[] updatableVars;
	protected int currentAgentIndex;
	protected final IArchitecture architecture;
	protected final IExpression scheduleFrequency;

	/**
	 * Listeners, created in a lazy way
	 */
	private LinkedList<IPopulation.Listener> listeners = null;

	public static IPopulation.IsLiving isLiving = new IPopulation.IsLiving();

	class MirrorPopulationManagement implements IExecutable {

		final IExpression listOfTargetAgents;

		MirrorPopulationManagement(final IExpression exp) {
			listOfTargetAgents = exp;
		}

		@Override
		public Object executeOn(final IScope scope) throws GamaRuntimeException {
			final IPopulation pop = GamaPopulation.this;
			final Set<IAgent> targets = new THashSet(Cast.asList(scope, listOfTargetAgents.value(scope)));
			final List<IAgent> toKill = new ArrayList();
			for (final IAgent agent : pop.iterable(scope)) {
				final IAgent target = Cast.asAgent(scope, agent.getAttribute("target"));
				if (targets.contains(target)) {
					targets.remove(target);
				} else {
					toKill.add(agent);
				}
			}
			for (final IAgent agent : toKill) {
				agent.dispose();
			}
			final List<Map> attributes = new ArrayList();
			for (final IAgent target : targets) {
				final Map<String, Object> att = new THashMap();
				att.put("target", target);
				attributes.add(att);
			}
			return pop.createAgents(scope, targets.size(), attributes, false, true);
		}

	}

	protected GamaPopulation(final IMacroAgent host, final ISpecies species) {
		super(0, host == null ? Types.get(IKeyword.EXPERIMENT)
				: host.getModel().getDescription().getTypeNamed(species.getName()));
		this.host = host;
		this.species = species;
		architecture = species.getArchitecture();
		final TypeDescription ecd = (TypeDescription) species.getDescription();
		orderedVarNames = ecd.getVarNames().toArray(new String[0]);
		final List<String> updatableVarNames = ecd.getUpdatableVarNames();
		final int updatableVarsSize = updatableVarNames.size();
		updatableVars = new IVariable[updatableVarsSize];
		for (int i = 0; i < updatableVarsSize; i++) {
			final String s = updatableVarNames.get(i);
			updatableVars[i] = species.getVar(s);
		}
		if (species.isMirror() && host != null) {
			host.getScope().getSimulationScope()
					.postEndAction(new MirrorPopulationManagement(species.getFacet(IKeyword.MIRRORS)));
		}

		// Add an attribute to the agents (dans SpeciesDescription)
		scheduleFrequency = species.getFrequency();
	}

	@Override
	public boolean step(final IScope scope) throws GamaRuntimeException {
		final IList<IAgent> agents = computeAgentsToSchedule(scope);
		final Iterator<IAgent> agentsToSchedule = Iterators.forArray(agents.toArray(new IAgent[0]));
		while (agentsToSchedule.hasNext()) {
			if (!scope.step(agentsToSchedule.next())) {
				continue;
			}
		}
		return true;
	}

	@Override
	public void updateVariables(final IScope scope, final IAgent a) {
		for (final IVariable v : updatableVars) {
			scope.setCurrentSymbol(v);
			v.setVal(scope, a, v.getUpdatedValue(scope));
			// updatableVars[j].updateFor(scope, a);
		}
	}

	@Override
	public boolean init(final IScope scope) {
		return true;
		// // Do whatever the population has to do at the first step ?
		// Ideally, the list of agents to init should be there rather than in
		// the scheduler
	}

	@Override
	public void createVariablesFor(final IScope scope, final IAgent agent) throws GamaRuntimeException {
		for (final String s : orderedVarNames) {
			final IVariable var = species.getVar(s);
			var.initializeWith(scope, agent, null);
		}
	}

	/**
	 *
	 * @see msi.gama.interfaces.IPopulation#computeAgentsToSchedule(msi.gama.interfaces.IScope,
	 *      msi.gama.util.GamaList)
	 */
	// @Override
	public IList<IAgent> computeAgentsToSchedule(final IScope scope) {
		final int frequency = scheduleFrequency == null ? 1 : Cast.asInt(scope, scheduleFrequency.value(scope));
		final int step = scope.getClock().getCycle();
		if (frequency == 0 || step % frequency != 0) {
			return GamaListFactory.create();
		}
		final IExpression ags = getSpecies().getSchedule();
		final IList<IAgent> agents = ags == null ? this : Cast.asList(scope, ags.value(scope));
		return agents;
	}

	@Override
	public IAgent getAgent(final Integer index) {
		for (final IAgent a : this) {
			if (index.equals(a.getIndex())) {
				return a;
			}
		}
		return null;
	}

	@Override
	public int compareTo(final IPopulation o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public ITopology getTopology() {
		return topology;
	}

	@Override
	public String getName() {
		return species.getName();
	}

	@Override
	public boolean isGrid() {
		return species.isGrid();
	}

	@Override
	public ISpecies getSpecies() {
		return species;
	}

	@Override
	public Iterable<? extends IAgent> iterable(final IScope scope) {
		return (Iterable<IAgent>) getAgents(scope);
	}

	@Override
	public void dispose() {
		// scope.getGui().debug("GamaPopulation.dispose : " + this);
		killMembers();
		/* agents. */clear();
		firePopulationCleared();
		if (topology != null) {
			topology.dispose();
			topology = null;
		}
	}

	@Override
	public IAgent[] toArray() {
		return super.toArray(new IAgent[0]);
	}

	/**
	 * Special case for creating agents directly from geometries
	 * 
	 * @param scope
	 * @param number
	 * @param initialValues
	 * @param geometries
	 * @return
	 */
	@Override
	public IList<? extends IAgent> createAgents(final IScope scope, final IContainer<?, IShape> geometries) {
		final int number = geometries.length(scope);
		if (number == 0) {
			return GamaListFactory.create();
		}
		final IList<IAgent> list = GamaListFactory.create(getType().getContentType(), number);
		final IAgentConstructor constr = ((SpeciesDescription) species.getDescription()).getAgentConstructor();
		for (final IShape geom : geometries.iterable(scope)) {
			// WARNING Should be redefined somehow
			final IAgent a = constr.createOneAgent(this);
			final int ind = currentAgentIndex++;
			a.setIndex(ind);
			a.setGeometry(geom);
			list.add(a);
		}
		/* agents. */addAll(list);

		for (final IAgent a : list) {
			a.schedule(scope);
			// a.scheduleAndExecute(null);
		}
		createVariablesFor(scope, list, Collections.EMPTY_LIST);
		fireAgentsAdded(list);
		return list;

	}

	@Override
	public IAgent createAgentAt(final IScope scope, final int index, final Map<String, Object> initialValues,
			final boolean isRestored, final boolean toBeScheduled) throws GamaRuntimeException {

		final List<Map<String, Object>> mapInitialValues = new ArrayList<>();
		mapInitialValues.add(initialValues);

		// TODO : think to another solution... it is ugly
		final int tempIndexAgt = currentAgentIndex;

		currentAgentIndex = index;
		final IList<? extends IAgent> listAgt = createAgents(scope, 1, mapInitialValues, isRestored, toBeScheduled);
		currentAgentIndex = tempIndexAgt;

		return listAgt.firstValue(scope);
	}

	@Override
	public IList<? extends IAgent> createAgents(final IScope scope, final int number,
			final List<? extends Map> initialValues, final boolean isRestored, final boolean toBeScheduled)
			throws GamaRuntimeException {
		if (number == 0) {
			return GamaListFactory.create();
		}
		final IList<IAgent> list = GamaListFactory.create(getType().getContentType(), number);
		final IAgentConstructor constr = ((SpeciesDescription) species.getDescription()).getAgentConstructor();
		for (int i = 0; i < number; i++) {
			final IAgent a = constr.createOneAgent(this);
			final int ind = currentAgentIndex++;
			a.setIndex(ind);
			// Try to grab the location earlier
			if (initialValues != null && !initialValues.isEmpty()) {
				final Map<Object, Object> init = initialValues.get(i);
				if (init.containsKey(IKeyword.SHAPE)) {
					final Object val = init.get(IKeyword.SHAPE);
					if (val instanceof GamaPoint) {
						a.setGeometry(new GamaShape((IShape) val));
					} else {
						a.setGeometry((IShape) val);
					}
					init.remove(IKeyword.SHAPE);
				} else if (init.containsKey(IKeyword.LOCATION)) {
					a.setLocation((GamaPoint) init.get(IKeyword.LOCATION));
					init.remove(IKeyword.LOCATION);
				}
			}
			list.add(a);
		}
		addAll(list);
		createVariablesFor(scope, list, initialValues);
		if (!isRestored) {
			for (final IAgent a : list) {
				// if agent is restored (on the capture or release); then don't
				// need to run the "init"
				// reflex
				a.schedule(scope);
				// a.scheduleAndExecute(sequence);
			}
		}
		fireAgentsAdded(list);
		return list;
	}

	public void createVariablesFor(final IScope scope, final List<? extends IAgent> agents,
			final List<? extends Map> initialValues) throws GamaRuntimeException {
		createAndUpdateVariablesFor(scope, agents, initialValues, false);
	}

	// public void createVariablesFor(final IScope scope, final List<? extends
	// IAgent> agents,
	// final List<? extends Map> initialValues) throws GamaRuntimeException {
	// if ( agents == null || agents.isEmpty() ) { return; }
	// final boolean empty = initialValues == null || initialValues.isEmpty();
	// Map<String, Object> inits;
	// for ( int i = 0, n = agents.size(); i < n; i++ ) {
	// final IAgent a = agents.get(i);
	// try {
	// // a.acquireLock();
	// if ( empty ) {
	// inits = Collections.EMPTY_MAP;
	// } else {
	// inits = initialValues.get(i);
	// }
	// for ( final String s : orderedVarNames ) {
	// final IVariable var = species.getVar(s);
	// var.initializeWith(scope, a, empty ? null : inits.get(s));
	// }
	// } finally {
	// // a.releaseLock();
	// }
	// }
	// }

	public void createAndUpdateVariablesFor(final IScope scope, final List<? extends IAgent> agents,
			final List<? extends Map> initialValues, final boolean update) throws GamaRuntimeException {
		if (agents == null || agents.isEmpty()) {
			return;
		}
		final boolean empty = initialValues == null || initialValues.isEmpty();
		Map<String, Object> inits;
		for (int i = 0, n = agents.size(); i < n; i++) {
			final IAgent a = agents.get(i);
			if (empty) {
				inits = Collections.EMPTY_MAP;
			} else {
				inits = initialValues.get(i);
			}
			for (final String s : orderedVarNames) {
				final IVariable var = species.getVar(s);
				final Object initGet = empty || !allowVarInitToBeOverridenByExternalInit(var) ? null : inits.get(s);
				if (!update || initGet != null) {
					var.initializeWith(scope, a, initGet);
				} // else if initGet == null : do not do anything, this will
					// keep the previously defined value for the variable
			}
		}
	}

	protected boolean allowVarInitToBeOverridenByExternalInit(final IVariable var) {
		return true;
	}

	@Override
	public void initializeFor(final IScope scope) throws GamaRuntimeException {
		dispose();
		computeTopology(scope);
		if (topology != null) {
			topology.initialize(scope, this);
		}
	}

	@Override
	public boolean hasVar(final String n) {
		return species.getVar(n) != null;
	}

	@Override
	public boolean hasAspect(final String default1) {
		return species.hasAspect(default1);
	}

	@Override
	public IExecutable getAspect(final String default1) {
		return species.getAspect(default1);
	}

	@Override
	public Collection<String> getAspectNames() {
		return species.getAspectNames();
	}

	@Override
	public IVariable getVar(final IAgent a, final String s) {
		return species.getVar(s);
	}

	// @Override
	// public boolean manages(final ISpecies s, final boolean direct) {
	// if ( species == s ) { return true; }
	// if ( !direct ) { return species.extendsSpecies(s); }
	// return false;
	// }

	@Override
	public boolean hasUpdatableVariables() {
		return updatableVars.length > 0;
	}

	@Override
	public IAgent getAgent(final IScope scope, final ILocation coord) {
		final IAgentFilter filter = In.list(scope, this);
		if (filter == null) {
			return null;
		}

		return topology == null ? null : topology.getAgentClosestTo(scope, coord, filter);
	}

	/**
	 * Initializes the appropriate topology.
	 *
	 * @param scope
	 * @return
	 * @throws GamaRuntimeException
	 */
	protected void computeTopology(final IScope scope) throws GamaRuntimeException {
		final IExpression expr = species.getFacet(IKeyword.TOPOLOGY);
		final boolean fixed = species.isGraph() || species.isGrid();
		if (expr != null) {
			if (!fixed) {
				topology = GamaTopologyType.staticCast(scope, scope.evaluate(expr, host), false);
				return;
			}
			throw GamaRuntimeException.warning(
					"Impossible to assign a topology to " + species.getName() + " as it already defines one.", scope);
		}
		if (species.isGrid()) {
			topology = buildGridTopology(scope, species, getHost());
		} else if (species.isGraph()) {
			final IExpression spec = species.getFacet(IKeyword.EDGE_SPECIES);
			final String edgeName = spec == null ? "base_edge" : spec.literalValue();
			final ISpecies edgeSpecies = scope.getSimulationScope().getModel().getSpecies(edgeName);
			final IType edgeType = scope.getModelContext().getTypeNamed(edgeName);
			final IType nodeType = getType().getContentType();
			// TODO Specifier directed quelque part dans l'esp�ce
			final GamaSpatialGraph g = new GamaSpatialGraph(GamaListFactory.create(), false, false,
					new AbstractGraphNodeAgent.NodeRelation(), edgeSpecies, scope, nodeType, edgeType);
			this.addListener(g);
			g.postRefreshManagementAction(scope);
			topology = new GraphTopology(scope, this.getHost(), g);
		} else {
			topology = new ContinuousTopology(scope, this.getHost());
		}

	}

	protected static ITopology buildGridTopology(final IScope scope, final ISpecies species, final IAgent host) {
		IExpression exp = species.getFacet(IKeyword.WIDTH);
		final int rows = exp == null
				? species.hasFacet(IKeyword.CELL_WIDTH)
						? (int) (scope.getSimulationScope().getGeometry().getEnvelope().getWidth()
								/ Cast.asInt(scope, species.getFacet(IKeyword.CELL_WIDTH).value(scope)))
						: 100
				: Cast.asInt(scope, exp.value(scope));
		exp = species.getFacet(IKeyword.HEIGHT);
		final int columns = exp == null
				? species.hasFacet(IKeyword.CELL_HEIGHT)
						? (int) (scope.getSimulationScope().getGeometry().getEnvelope().getHeight()
								/ Cast.asInt(scope, species.getFacet(IKeyword.CELL_HEIGHT).value(scope)))
						: 100
				: Cast.asInt(scope, exp.value(scope));

		// exp = species.getFacet(IKeyword.TORUS);
		// final boolean isTorus = exp != null && Cast.asBool(scope,
		// exp.value(scope));
		final boolean isTorus = host.getTopology().isTorus();
		exp = species.getFacet("use_individual_shapes");
		final boolean useIndividualShapes = exp == null || Cast.asBool(scope, exp.value(scope));
		exp = species.getFacet("use_neighbors_cache");
		final boolean useNeighborsCache = exp == null || Cast.asBool(scope, exp.value(scope));
		exp = species.getFacet(IKeyword.NEIGHBORS);
		if (exp == null) {
			exp = species.getFacet(IKeyword.NEIGHBOURS);
		}
		final boolean usesVN = exp == null || Cast.asInt(scope, exp.value(scope)) == 4;
		final boolean isHexagon = exp != null && Cast.asInt(scope, exp.value(scope)) == 6;
		exp = species.getFacet(IKeyword.FILE);
		final GamaGridFile file = (GamaGridFile) (exp != null ? exp.value(scope) : null);
		if (file == null) {
			return new GridTopology(scope, host, rows, columns, isTorus, usesVN, isHexagon, useIndividualShapes,
					useNeighborsCache);
		}
		return new GridTopology(scope, host, file, isTorus, usesVN, useIndividualShapes, useNeighborsCache);
	}

	@Override
	public IMacroAgent getHost() {
		return host;
	}

	// @Override
	// public synchronized IAgent[] toArray() {
	// return Arrays.copyOf(super.toArray(), size(), IAgent[].class);
	// }

	@Override
	public Iterator<IAgent> iterator() {
		return super.iterator();
		// return GamaListFactory.create(this,
		// getHost().getModel().getDescription().getTypeNamed(getName())).iterator();
		// return Iterators.forArray(toArray(new IAgent[0]));
	}

	@Override
	public boolean equals(final Object o) {
		return this == o;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	@Override
	public void killMembers() throws GamaRuntimeException {
		final IAgent[] ag = toArray();
		for (final IAgent a : ag) {
			if (a != null) {
				a.dispose();
			}
		}
		// topology = null;
		// for ( int i = 0; i < updatableVars.length; i++ ) {
		// updatableVars[i] = null;
		// }
		// In case
		this.clear();
		// final Iterator<IAgent> it = ag.iterator();
		// while (it.hasNext()) {
		// final IAgent a = it.next();
		// a.dispose();
		// }
	}

	// @Override
	// public void hostChangesShape() {
	// topology.shapeChanged(this);
	// }

	@Override
	public String toString() {
		return "Population of " + species.getName();
	}

	//
	// @Override
	// public IAgent[] toArray() {
	// return (IAgent[]) super.toArray();
	// }

	/**
	 * @see msi.gama.common.interfaces.IGamlable#toGaml()
	 */
	// AD 19/01/16: Commented to address Issue #1451
	// @Override
	// public String serialize(final boolean includingBuiltIn) {
	// return "list(" + species.getName() + ")";
	// }

	@Override
	public void addValue(final IScope scope, final IAgent value) {
		fireAgentAdded(value);
		add(value);
	}

	@Override
	public void addValueAtIndex(final IScope scope, final Object index, final IAgent value) {
		addValue(scope, value);
	}

	public void addAll(final IScope scope, final IAgent value) {
		addValue(scope, value);
	}

	@Override
	public void addValues(final IScope scope, final IContainer values) {
		for (final IAgent o : (java.lang.Iterable<IAgent>) values.iterable(scope)) {
			addValue(scope, o);
		}
	}

	@Override
	public void removeValue(final IScope scope, final Object value) {
		if (value instanceof IAgent && super.remove(value)) {
			if (topology != null)
				topology.removeAgent((IAgent) value);
			fireAgentRemoved((IAgent) value);
		}
	}

	@Override
	public void removeValues(final IScope scope, final IContainer values) {
		for (final Object o : values.iterable(scope)) {
			removeValue(scope, o);
		}
	}

	@Override
	public void removeAllOccurrencesOfValue(final IScope scope, final Object value) {
		removeValue(scope, value);
	}

	@Override
	public boolean remove(final Object a) {
		removeValue(null, a);
		return true;
	}

	@Override
	public boolean contains(final IScope scope, final Object o) {
		if (!(o instanceof IAgent)) {
			return false;
		}
		return ((IAgent) o).getPopulation() == this;
	}

	private boolean hasListeners() {
		return listeners != null && !listeners.isEmpty();
	}

	@Override
	public void addListener(final IPopulation.Listener listener) {
		if (listeners == null) {
			listeners = new LinkedList<IPopulation.Listener>();
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(final IPopulation.Listener listener) {
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
	}

	protected void fireAgentAdded(final IAgent agent) {
		if (!hasListeners()) {
			return;
		}
		try {
			for (final IPopulation.Listener l : listeners) {
				l.notifyAgentAdded(this, agent);
			}
		} catch (final RuntimeException e) {
			e.printStackTrace();
		}
	}

	protected void fireAgentsAdded(final IList container) {
		if (!hasListeners()) {
			return;
		}
		// create list
		final Collection agents = new LinkedList();
		final Iterator it = container.iterator();
		while (it.hasNext()) {
			agents.add(it.next());
		}
		// send event
		try {
			for (final IPopulation.Listener l : listeners) {
				l.notifyAgentsAdded(this, agents);
			}
		} catch (final RuntimeException e) {
			e.printStackTrace();
		}
	}

	protected void fireAgentRemoved(final IAgent agent) {
		if (!hasListeners()) {
			return;
		}
		try {
			for (final IPopulation.Listener l : listeners) {
				l.notifyAgentRemoved(this, agent);
			}
		} catch (final RuntimeException e) {
			e.printStackTrace();
		}
	}

	// protected void fireAgentsRemoved(final IContainer container) {
	// if ( !hasListeners() ) { return; }
	// // create list
	// final Collection agents = new LinkedList();
	// final Iterator it = container.iterator();
	// while (it.hasNext()) {
	// agents.add(it.next());
	// }
	// // send event
	// try {
	// for ( final IPopulation.Listener l : listeners ) {
	// l.notifyAgentsRemoved(this, agents);
	// }
	// } catch (final RuntimeException e) {
	// e.printStackTrace();
	// }
	// }

	protected void firePopulationCleared() {
		if (!hasListeners()) {
			return;
		}
		// send event
		try {
			for (final IPopulation.Listener l : listeners) {
				l.notifyPopulationCleared(this);
			}
		} catch (final RuntimeException e) {
			e.printStackTrace();
		}
	}

	// Filter methods

	/**
	 * Method getAgents()
	 * 
	 * @see msi.gama.metamodel.topology.filter.IAgentFilter#getAgents()
	 */
	@Override
	public IContainer<?, ? extends IShape> getAgents(final IScope scope) {
		return GamaListFactory.create(scope, getType().getContentType(), GamaPopulation.allLivingAgents(this));
	}

	/**
	 * Method accept()
	 * 
	 * @see msi.gama.metamodel.topology.filter.IAgentFilter#accept(msi.gama.runtime.IScope,
	 *      msi.gama.metamodel.shape.IShape, msi.gama.metamodel.shape.IShape)
	 */
	@Override
	public boolean accept(final IScope scope, final IShape source, final IShape a) {
		final IAgent agent = a.getAgent();
		if (agent == null) {
			return false;
		}
		if (agent.getPopulation() != this) {
			return false;
		}
		if (agent.dead()) {
			return false;
		}
		final IAgent as = source.getAgent();
		// if ( as != null && as.getPopulation() != pop ) {
		if (agent == as) {
			return false;
		}
		// }
		return true;
	}

	/**
	 * Method filter()
	 * 
	 * @see msi.gama.metamodel.topology.filter.IAgentFilter#filter(msi.gama.runtime.IScope,
	 *      msi.gama.metamodel.shape.IShape, java.util.Collection)
	 */
	@Override
	public void filter(final IScope scope, final IShape source, final Collection<? extends IShape> results) {
		final IAgent sourceAgent = source == null ? null : source.getAgent();
		results.remove(sourceAgent);
		final Iterator<? extends IShape> it = results.iterator();
		while (it.hasNext()) {
			final IShape s = it.next();
			final IAgent a = s.getAgent();
			if (a == null || a.dead()
					|| a.getPopulation() != this
							&& (a.getPopulation().getType().getContentType() != this.getType().getContentType()
									|| !this.contains(a))) {
				it.remove();
			}

		}

	}

	@Override
	public Collection<? extends IPopulation> getPopulations(final IScope scope) {
		return Collections.singleton(this);
	}

	//
	// @Override
	// public IPopulation getPopulation(final IScope scope, final String name) {
	// return getName().equals(name) ? this : null;
	// }

	@Override
	public IAgent getFromIndicesList(final IScope scope, final IList indices) throws GamaRuntimeException {
		if (indices == null) {
			return null;
		}
		final int size = indices.size();
		switch (size) {
		case 0:
			return null;
		case 1:
			return super.getFromIndicesList(scope, indices);
		case 2:
			return this.getAgent(scope,
					new GamaPoint(Cast.asFloat(scope, indices.get(0)), Cast.asFloat(scope, indices.get(1))));
		default:
			throw GamaRuntimeException.error("Populations cannot be accessed with 3 or more indexes", scope);

		}

	}

	/**
	 * @param actionScope
	 * @param iterable
	 * @return
	 */
	public static Iterable<IAgent> allLivingAgents(final Iterable<IAgent> iterable) {
		return Iterables.filter(iterable, GamaPopulation.isLiving);
	}
}