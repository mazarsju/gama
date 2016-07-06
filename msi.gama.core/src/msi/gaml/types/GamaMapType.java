/*********************************************************************************************
 * 
 * 
 * 'GamaMapType.java', in plugin 'msi.gama.core', is part of the source code of the
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
import msi.gama.util.*;
import msi.gaml.expressions.IExpression;

@type(name = IKeyword.MAP, id = IType.MAP, wraps = { GamaMap.class }, kind = ISymbolKind.Variable.CONTAINER,
concept = { IConcept.TYPE, IConcept.CONTAINER, IConcept.MAP })
public class GamaMapType extends GamaContainerType<GamaMap> {

	@Override
	public GamaMap cast(final IScope scope, final Object obj, final Object param, final IType keyType,
		final IType contentType, final boolean copy) throws GamaRuntimeException {
		GamaMap result = staticCast(scope, obj, keyType, contentType, copy);
		return result;
	}

	public static GamaMap staticCast(final IScope scope, final Object obj, final IType keyType,
		final IType contentsType, final boolean copy) {
		if ( obj == null ) { return GamaMapFactory.create(keyType, contentsType); }
		// if ( obj instanceof GamaPair ) { return GamaMapFactory.create(GamaPairType.staticCast(scope, obj, keyType,
		// contentsType)); }
		if ( obj instanceof IAgent ) {
			// We collect all the variables / attributes of the agent
			IAgent agent = (IAgent) obj;
			GamaMap<String, Object> map = GamaMapFactory.create(Types.STRING, Types.NO_TYPE);
			for ( String s : agent.getSpecies().getVarNames() ) {
				map.put(s, agent.getDirectVarValue(scope, s));
			}
			map.putAll(agent.getAttributes());
			GamaMap shapeAttr = agent.getGeometry().getAttributes();
			if ( shapeAttr != null ) {
				map.putAll(shapeAttr);
			}
			IType kt = keyType == null || keyType == Types.NO_TYPE ? Types.STRING : keyType;
			return map.mapValue(scope, kt, contentsType, false);
		}
		if ( obj instanceof IContainer ) { return ((IContainer) obj).mapValue(scope, keyType, contentsType, copy); }
		final GamaMap result = GamaMapFactory.create(keyType, contentsType);
		result.setValueAtIndex(scope, obj, obj);
		return result;
	}

	@Override
	public IType keyTypeIfCasting(final IExpression exp) {
		IType itemType = exp.getType();
		if ( itemType.isAgentType() ) { return Types.get(STRING); }
		switch (itemType.id()) {
			case PAIR:
			case MAP:
				return itemType.getKeyType();
			case MATRIX:
				return itemType.getContentType();
			case GRAPH:
				return Types.get(PAIR);
			case LIST:
				if ( itemType.getContentType().id() == IType.PAIR ) {
					return itemType.getContentType().getKeyType();
				} else {
					return itemType.getContentType();
				}
		}
		return itemType;
	}

	@Override
	public IType contentsTypeIfCasting(final IExpression exp) {
		IType itemType = exp.getType();
		if ( itemType.isAgentType() ) { return Types.NO_TYPE; }
		switch (itemType.id()) {
			case LIST:
				if ( itemType.getContentType().id() == IType.PAIR ) {
					return itemType.getContentType().getContentType();
				} else {
					return itemType.getContentType();
				}
			case PAIR:
			case GRAPH:
			case MAP:
			case MATRIX:
				return itemType.getContentType();

		}
		return itemType;
	}

	@Override
	public boolean canCastToConst() {
		return true;
	}
}
