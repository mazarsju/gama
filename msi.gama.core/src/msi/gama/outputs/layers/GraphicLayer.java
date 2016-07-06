/*********************************************************************************************
 *
 *
 * 'GraphicLayer.java', in plugin 'msi.gama.application', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.outputs.layers;

import msi.gama.common.interfaces.*;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;

public class GraphicLayer extends AbstractLayer {

	protected GraphicLayer(final ILayerStatement layer) {
		super(layer);
	}

	@Override
	protected void privateDrawDisplay(final IScope scope, final IGraphics g) throws GamaRuntimeException {
		Object[] result = new Object[1];
		IAgent agent = scope.getAgentScope();
		scope.execute(((GraphicLayerStatement) definition).getAspect(), agent, null, result);
	}

	@Override
	public String getType() {
		return IKeyword.GRAPHICS;
	}


}
