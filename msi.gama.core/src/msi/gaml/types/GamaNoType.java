/*********************************************************************************************
 *
 *
 * 'GamaNoType.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.types;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;

/**
 * The type used to represent the absence of type
 *
 * Written by drogoul Modified on 1 ao�t 2010
 *
 * @todo Description
 *
 */
@type(name = IKeyword.UNKNOWN, id = IType.NONE, wraps = {
		Object.class }, kind = ISymbolKind.Variable.REGULAR, concept = { IConcept.TYPE })
public class GamaNoType extends GamaType {

	@Override
	public Object cast(final IScope scope, final Object obj, final Object param, final boolean copy) {
		// WARNING: Should we obey the "copy" parameter in this case ?
		return obj;
	}

	@Override
	public Object getDefault() {
		return null;
	}

	@Override
	public boolean isSuperTypeOf(final IType type) {
		return true;
	}

	@Override
	public IType findCommonSupertypeWith(final IType iType) {
		// By default, this is the supertype common to all subtypes
		return /* iType.getDefault() == null ? iType : */this;
	}

	@Override
	public boolean canCastToConst() {
		return true;
	}

	@Override
	public boolean isTranslatableInto(final IType t) {
		return true;
	}

}
