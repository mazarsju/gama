/*********************************************************************************************
 * 
 *
 * 'InspectSpeciesHandler.java', in plugin 'msi.gama.application', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package ummisco.gama.ui.commands;

import msi.gama.outputs.InspectDisplayOutput;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.species.ISpecies;
import org.eclipse.core.commands.*;

public class InspectSpeciesHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		try {
			InspectDisplayOutput.browse(GAMA.getSimulation(), (ISpecies) null);
		} catch (GamaRuntimeException e) {
			throw new ExecutionException(e.getMessage());
		}
		return null;
	}
}
