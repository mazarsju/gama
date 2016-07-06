/*********************************************************************************************
 * 
 * 
 * 'GamaGenericAgentType.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.types;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.descriptions.SpeciesDescription;

/**
 * The "generic" agent type.
 * 
 * Written by drogoul Modified on 1 ao�t 2010
 * 
 * @todo Description
 * @modified 08 juin 2012
 * 
 */
@type(name = IKeyword.AGENT, id = IType.AGENT, wraps = { IAgent.class }, kind = ISymbolKind.Variable.REGULAR,
concept = { IConcept.TYPE, IConcept.SPECIES })
public class GamaGenericAgentType extends GamaAgentType {

	public GamaGenericAgentType() {
		super(null, IKeyword.AGENT, IType.AGENT, IAgent.class);
	}

	public void setSpecies(final SpeciesDescription sd) {
		species = sd;
	}

	@Override
	public IAgent cast(final IScope scope, final Object obj, final Object param, final IType keyType,
		final IType contentsType, final boolean copy) throws GamaRuntimeException {
		return cast(scope, obj, param, copy);
	}

	@Override
	public IAgent cast(final IScope scope, final Object obj, final Object param, final boolean copy)
		throws GamaRuntimeException {
		if ( obj == null ) { return getDefault(); }
		if ( obj instanceof IAgent ) { return (IAgent) obj; }
		return getDefault();
	}

	@Override
	public boolean isSuperTypeOf(final IType type) {
		return type != this && type instanceof GamaAgentType;
	}

}
