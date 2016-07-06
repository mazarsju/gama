/*********************************************************************************************
 *
 *
 * 'HillClimbing.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.kernel.batch;

import java.util.*;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.kernel.experiment.*;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.types.IType;

@symbol(name = IKeyword.HILL_CLIMBING, kind = ISymbolKind.BATCH_METHOD, with_sequence = false, concept = { IConcept.BATCH, IConcept.ALGORITHM })
@inside(kinds = { ISymbolKind.EXPERIMENT })
@facets(
	value = { @facet(name = IKeyword.NAME, type = IType.ID, optional = false, internal = true),
		@facet(name = HillClimbing.ITER_MAX, type = IType.INT, optional = true, doc = @doc("number of iterations") ),
		@facet(name = IKeyword.MAXIMIZE,
			type = IType.FLOAT,
			optional = true,
			doc = @doc("the value the algorithm tries to maximize") ),
		@facet(name = IKeyword.MINIMIZE,
			type = IType.FLOAT,
			optional = true,
			doc = @doc("the value the algorithm tries to minimize") ),
		@facet(name = IKeyword.AGGREGATION,
			type = IType.LABEL,
			optional = true,
			values = { IKeyword.MIN, IKeyword.MAX },
			doc = @doc("the agregation method") ) },
	omissible = IKeyword.NAME)
@doc(
	value = "This algorithm is an implementation of the Hill Climbing algorithm. See the wikipedia article and [batch161 the batch dedicated page].",
	usages = {
		@usage(
			value = "As other batch methods, the basic syntax of the `hill_climbing` statement uses `method hill_climbing` instead of the expected `hill_climbing name: id` : ",
			examples = { @example(value = "method hill_climbing [facet: value];", isExecutable = false) }),
		@usage(value = "For example: ",
			examples = { @example(value = "method hill_climbing iter_max: 50 maximize : food_gathered; ",
				isExecutable = false) }) })
public class HillClimbing extends LocalSearchAlgorithm {

	protected static final String ITER_MAX = "iter_max";
	private StoppingCriterion stoppingCriterion = null;
	private int maxIt;

	public HillClimbing(final IDescription species) {
		super(species);
		initParams();
	}

	@Override
	public ParametersSet findBestSolution(final IScope scope) throws GamaRuntimeException {
		setBestSolution(this.solutionInit);
		double currentFitness = currentExperiment.launchSimulationsWithSolution(getBestSolution());
		initializeTestedSolutions();
		testedSolutions.put(getBestSolution(), currentFitness);
		int nbIt = 0;

		final Map<String, Object> endingCritParams = new Hashtable<String, Object>();
		endingCritParams.put("Iteration", Integer.valueOf(nbIt));
		while (stoppingCriterion == null || !stoppingCriterion.stopSearchProcess(endingCritParams)) {
			final List<ParametersSet> neighbors = neighborhood.neighbor(scope, getBestSolution());
			if ( neighbors.isEmpty() ) {
				break;
			}
			setBestFitness(currentFitness);
			ParametersSet bestNeighbor = null;

			for ( final ParametersSet neighborSol : neighbors ) {
				if ( neighborSol == null ) {
					continue;
				}
				Double neighborFitness = testedSolutions.get(neighborSol);
				if ( neighborFitness == null ) {
					neighborFitness = Double.valueOf(currentExperiment.launchSimulationsWithSolution(neighborSol));
				}
				testedSolutions.put(neighborSol, neighborFitness);

				if ( isMaximize() && neighborFitness.doubleValue() > getBestFitness() ||
					!isMaximize() && neighborFitness.doubleValue() < getBestFitness() ) {
					bestNeighbor = neighborSol;
					setBestFitness(neighborFitness.doubleValue());
				}
			}
			if ( bestNeighbor != null ) {
				setBestSolution(bestNeighbor);
				currentFitness = getBestFitness();
			} else {
				break;
			}
			nbIt++;
			endingCritParams.put("Iteration", Integer.valueOf(nbIt));
		}
		// System.out.println("Best solution : " + currentSol + " fitness : "
		// + currentFitness);
		return getBestSolution();
	}

	@Override
	public void initializeFor(final IScope scope, final BatchAgent agent) throws GamaRuntimeException {
		super.initializeFor(scope, agent);
	}

	protected void initParams(final IScope scope) {
		final IExpression maxItExp = getFacet(ITER_MAX);
		if ( maxItExp != null ) {
			maxIt = Cast.asInt(scope, maxItExp.value(scope));
			stoppingCriterion = new StoppingCriterionMaxIt(maxIt);
		}
	}

	@Override
	public void addParametersTo(final List<IParameter.Batch> params, final BatchAgent agent) {
		super.addParametersTo(params, agent);
		params
			.add(new ParameterAdapter("Maximum number of iterations", IExperimentPlan.BATCH_CATEGORY_NAME, IType.INT) {

				@Override
				public Object value() {
					return maxIt;
				}

			});
	}

}
