/*********************************************************************************************
 *
 *
 * 'IExploration.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.kernel.batch;

import java.util.List;
import msi.gama.kernel.experiment.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.ISymbol;
import msi.gaml.expressions.IExpression;

/**
 * The class IExploration.
 *
 * @author drogoul
 * @since 26 d�c. 2011
 *
 */
public interface IExploration extends ISymbol {// , Runnable {

	public final static short C_MAX = 0, C_MIN = 1, C_MEAN = 2;

	public abstract void initializeFor(IScope scope, final BatchAgent agent) throws GamaRuntimeException;

	public abstract String getCombinationName();

	public abstract void addParametersTo(final List<IParameter.Batch> exp, BatchAgent agent);

	public abstract Double getBestFitness();

	public abstract IExpression getFitnessExpression();

	public abstract ParametersSet getBestSolution();

	public abstract short getCombination();

	/**
	 * @param scope
	 */
	public abstract void run(IScope scope);

}