/*********************************************************************************************
 * 
 * 
 * 'Writer.java', in plugin 'msi.gama.headless', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.headless.xml;

import msi.gama.headless.core.*;
import msi.gama.headless.job.ExperimentJob;
import msi.gama.headless.job.ExperimentJob.ListenedVariable;

public interface Writer {

	public void writeSimulationHeader(ExperimentJob s);

	public void writeResultStep(long step, ListenedVariable[] vars);

	public void close();
}
