/*********************************************************************************************
 * 
 * 
 * 'Mutation1Var.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.kernel.batch;

import java.util.List;

import msi.gama.kernel.experiment.IParameter;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;

public class Mutation1Var implements Mutation {

	public Mutation1Var() {
	}

	@Override
	public Chromosome mutate(final IScope scope, final Chromosome chromosome, final List<IParameter.Batch> variables)
			throws GamaRuntimeException {
		final Chromosome chromoMut = new Chromosome(chromosome);

		final int indexMut = scope.getRandom().between(0, chromoMut.getGenes().length - 1);
		final String varStr = chromoMut.getPhenotype()[indexMut];
		IParameter.Batch var = null;
		for (final IParameter.Batch p : variables) {
			if (p.getName().equals(varStr)) {
				var = p;
				break;
			}
		}
		// TODO Lourd et pas du tout optimis�.
		if (var != null) {
			chromoMut.setGene(scope, var, indexMut);
		}
		return chromoMut;
	}

}
