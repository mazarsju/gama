/*********************************************************************************************
 *
 *
 * 'ParametricType.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.types;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import msi.gama.precompiler.GamlProperties;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.IContainer;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.OperatorProto;
import msi.gaml.descriptions.SpeciesDescription;
import msi.gaml.expressions.IExpression;

/**
 * Class ParametrizedType. A class that allows to build composed types with a
 * content type and a key type
 *
 * @author drogoul
 * @since 19 janv. 2014
 *
 */
public class ParametricType implements IContainerType {

	private final IContainerType type;
	private final IType contentsType;
	private final IType keyType;

	protected ParametricType(final IContainerType t, final IType kt, final IType ct) {
		type = t;
		contentsType = ct;
		keyType = kt;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof ParametricType) {
			return type.equals(((ParametricType) other).getType())
					&& keyType.equals(((ParametricType) other).getKeyType())
					&& contentsType.equals(((ParametricType) other).getContentType());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return type.hashCode() + contentsType.hashCode() * 100 + keyType.hashCode() * 10000;
	}

	@Override
	public String getDefiningPlugin() {
		return type.getDefiningPlugin();
	}

	/**
	 * Method getType()
	 * 
	 * @see msi.gama.common.interfaces.ITyped#getType()
	 */
	@Override
	public IContainerType getType() {
		return type;
	}

	/**
	 * Method cast()
	 * 
	 * @see msi.gaml.types.IType#cast(msi.gama.runtime.IScope, java.lang.Object,
	 *      java.lang.Object, msi.gaml.types.IType, msi.gaml.types.IType)
	 */
	@Override
	public IContainer cast(final IScope scope, final Object obj, final Object param, final IType kt, final IType ct,
			final boolean copy) throws GamaRuntimeException {
		return type.cast(scope, obj, param, keyType, contentsType, copy);
	}

	/**
	 * Method id()
	 * 
	 * @see msi.gaml.types.IType#id()
	 */
	@Override
	public int id() {
		return type.id();
	}

	/**
	 * Method toClass()
	 * 
	 * @see msi.gaml.types.IType#toClass()
	 */
	@Override
	public Class toClass() {
		return type.toClass();
	}

	/**
	 * Method getDefault()
	 * 
	 * @see msi.gaml.types.IType#getDefault()
	 */
	@Override
	public Object getDefault() {
		return type.getDefault();
	}

	/**
	 * Method getVarKind()
	 * 
	 * @see msi.gaml.types.IType#getVarKind()
	 */
	@Override
	public int getVarKind() {
		return ISymbolKind.Variable.CONTAINER;
	}

	/**
	 * Method getGetter()
	 * 
	 * @see msi.gaml.types.IType#getGetter(java.lang.String)
	 */
	@Override
	public OperatorProto getGetter(final String name) {
		return type.getGetter(name);
	}

	/**
	 * Method getFieldDescriptions()
	 * 
	 * @see msi.gaml.types.IType#getFieldDescriptions(msi.gaml.descriptions.ModelDescription)
	 */
	// @Override
	// public Map getFieldDescriptions(final ModelDescription model) {
	// return type.getFieldDescriptions(model);
	// }

	/**
	 * Method isSpeciesType()
	 * 
	 * @see msi.gaml.types.IType#isSpeciesType()
	 */
	@Override
	public boolean isAgentType() {
		// Verify this
		return type.isAgentType();
	}

	/**
	 * Method isSkillType()
	 * 
	 * @see msi.gaml.types.IType#isSkillType()
	 */
	@Override
	public boolean isSkillType() {
		return false;
	}

	/**
	 * Method defaultContentType()
	 * 
	 * @see msi.gaml.types.IType#defaultContentType()
	 */
	@Override
	public IType getContentType() {
		return contentsType;
	}

	/**
	 * Method defaultKeyType()
	 * 
	 * @see msi.gaml.types.IType#defaultKeyType()
	 */
	@Override
	public IType getKeyType() {
		return keyType;
	}

	/**
	 * Method getSpeciesName()
	 * 
	 * @see msi.gaml.types.IType#getSpeciesName()
	 */
	@Override
	public String getSpeciesName() {
		return type.getSpeciesName();
	}

	/**
	 * Method getSpecies()
	 * 
	 * @see msi.gaml.types.IType#getSpecies()
	 */
	@Override
	public SpeciesDescription getSpecies() {
		return type.getSpecies();
	}

	/**
	 * Method isAssignableFrom()
	 * 
	 * @see msi.gaml.types.IType#isAssignableFrom(msi.gaml.types.IType)
	 */
	@Override
	public boolean isAssignableFrom(final IType l) {
		return type.isAssignableFrom(l.getType()) && contentsType.isAssignableFrom(l.getContentType())
				&& keyType.isAssignableFrom(l.getKeyType());
	}

	/**
	 * Method isTranslatableInto()
	 * 
	 * @see msi.gaml.types.IType#isTranslatableInto(msi.gaml.types.IType)
	 */
	@Override
	public boolean isTranslatableInto(final IType l) {
		return type.isTranslatableInto(l.getType()) && contentsType.isTranslatableInto(l.getContentType())
				&& keyType.isTranslatableInto(l.getKeyType());
	}

	@Override
	public boolean isParametricFormOf(final IType l) {
		return !l.isParametricType() && type.equals(l);
	}

	@Override
	public boolean isParametricType() {
		return true;
	}

	/**
	 * Method setParent()
	 * 
	 * @see msi.gaml.types.IType#setParent(msi.gaml.types.IType)
	 */
	@Override
	public void setParent(final IType p) {
		// if ( p instanceof GamaContainerType ) {
		// type = (GamaContainerType) p;
		// }
	}

	/**
	 * Method getParent()
	 * 
	 * @see msi.gaml.types.IType#getParent()
	 */
	@Override
	public IType getParent() {
		return type;
	}

	/**
	 * Method coerce()
	 * 
	 * @see msi.gaml.types.IType#coerce(msi.gaml.types.IType,
	 *      msi.gaml.descriptions.IDescription)
	 */
	@Override
	public IType coerce(final IType expr, final IDescription context) {
		return null;
	}

	/**
	 * Method distanceTo()
	 * 
	 * @see msi.gaml.types.IType#distanceTo(msi.gaml.types.IType)
	 */
	@Override
	public int distanceTo(final IType t) {
		return t.getType().distanceTo(type) + t.getContentType().distanceTo(contentsType)
				+ t.getKeyType().distanceTo(keyType);
	}

	/**
	 * Method setFieldGetters()
	 * 
	 * @see msi.gaml.types.IType#setFieldGetters(java.util.Map)
	 */
	@Override
	public void setFieldGetters(final Map map) {
	}

	/**
	 * Method canBeTypeOf()
	 * 
	 * @see msi.gaml.types.IType#canBeTypeOf(msi.gama.runtime.IScope,
	 *      java.lang.Object)
	 */
	@Override
	public boolean canBeTypeOf(final IScope s, final Object c) {
		return type.canBeTypeOf(s, c);
	}

	/**
	 * Method init()
	 * 
	 * @see msi.gaml.types.IType#init(int, int, java.lang.String,
	 *      java.lang.Class[])
	 */
	@Override
	public void init(final int varKind, final int id, final String name, final Class... supports) {
	}

	/**
	 * Method isContainer()
	 * 
	 * @see msi.gaml.types.IType#isContainer()
	 */
	@Override
	public boolean isContainer() {
		return true; // ???
	}

	/**
	 * Method isFixedLength()
	 * 
	 * @see msi.gaml.types.IType#isFixedLength()
	 */
	@Override
	public boolean isFixedLength() {
		return type.isFixedLength();
	}

	/**
	 * Method findCommonSupertypeWith()
	 * 
	 * @see msi.gaml.types.IType#findCommonSupertypeWith(msi.gaml.types.IType)
	 */
	@Override
	public IType findCommonSupertypeWith(final IType iType) {
		if (iType instanceof ParametricType) {
			final IType pType = iType;
			final IType cType = type.findCommonSupertypeWith(pType.getType());
			if (cType.isContainer()) {
				final IType kt = keyType.findCommonSupertypeWith(pType.getKeyType());
				final IType ct = contentsType.findCommonSupertypeWith(pType.getContentType());
				return GamaType.from(cType, kt, ct);
			} else {
				return cType;
			}
		} else if (iType.isContainer()) {
			final IType cType = type.findCommonSupertypeWith(iType);
			return cType; // dont we need to use the key and contents type here
							// ?
		} else {
			return type.findCommonSupertypeWith(iType);
		}
	}

	/**
	 * Method isParented()
	 * 
	 * @see msi.gaml.types.IType#isParented()
	 */
	@Override
	public boolean isParented() {
		return true;
		// return type != null; // ??
	}

	@Override
	public boolean isDrawable() {
		return type.isDrawable();
	}

	/**
	 * Method setSupport()
	 * 
	 * @see msi.gaml.types.IType#setSupport(java.lang.Class)
	 */
	@Override
	public void setSupport(final Class clazz) {
	}

	@Override
	public IContainer cast(final IScope scope, final Object obj, final Object param, final boolean copy)
			throws GamaRuntimeException {
		return cast(scope, obj, param, keyType, contentsType, copy);
	}

	@Override
	public String toString() {
		if (type.id() == IType.LIST || type.id() == IType.MATRIX
				|| type.id() == IType.CONTAINER && keyType == Types.NO_TYPE) {
			return type.toString() + "<" + contentsType.toString() + ">";
		}
		if (type.id() == IType.SPECIES) {
			return type.toString() + "<" + contentsType.toString() + ">";
		}
		return type.toString() + "<" + keyType.toString() + ", " + contentsType.toString() + ">";
	}

	@Override
	public String asPattern() {
		final boolean vowel = StringUtils.startsWithAny(type.getName(), vowels);
		return "${" + (vowel ? "an_" : "a_") + serialize(true) + "}";
	}

	@Override
	public String serialize(final boolean includingBuiltIn) {
		if (type.id() == IType.LIST || type.id() == IType.MATRIX
				|| type.id() == IType.CONTAINER && keyType == Types.NO_TYPE) {
			return type.toString() + "<" + contentsType.toString() + ">";
		}
		if (type.id() == IType.SPECIES) {
			return type.toString() + "<" + contentsType.toString() + ">";
		}
		return type.toString() + "<" + keyType.serialize(includingBuiltIn) + ", "
				+ contentsType.serialize(includingBuiltIn) + ">";
	}

	@Override
	public IContainerType typeIfCasting(final IExpression exp) {
		if (contentsType == Types.NO_TYPE || keyType == Types.NO_TYPE) {
			final IType genericCast = type.typeIfCasting(exp);
			final IType ct = contentsType == Types.NO_TYPE ? genericCast.getContentType() : contentsType;
			final IType kt = keyType == Types.NO_TYPE ? genericCast.getKeyType() : keyType;
			return new ParametricType(type, kt, ct);
		}
		return this;
	}

	/**
	 * Method getTitle()
	 * 
	 * @see msi.gaml.descriptions.IGamlDescription#getTitle()
	 */
	@Override
	public String getTitle() {
		return toString().replace('<', '[').replace('>', ']');
	}

	/**
	 * Method getDocumentation()
	 * 
	 * @see msi.gaml.descriptions.IGamlDescription#getDocumentation()
	 */
	@Override
	public String getDocumentation() {
		return getTitle();
	}

	/**
	 * Method getName()
	 * 
	 * @see msi.gaml.descriptions.IGamlDescription#getName()
	 */
	@Override
	public String getName() {
		return toString();
	}

	@Override
	public void setName(final String name) {
		// Nothing
	}

	@Override
	public boolean canCastToConst() {
		return type.canCastToConst() && contentsType.canCastToConst() && keyType.canCastToConst();
	}

	@Override
	public IContainerType of(final IType... subs) {
		if (subs.length == 0) {
			return this;
		}
		IType kt = subs.length == 1 ? getKeyType() : subs[0];
		IType ct = subs.length == 1 ? subs[0] : subs[1];
		if (ct == Types.NO_TYPE) {
			if (kt == Types.NO_TYPE) {
				return this;
			}
			ct = getContentType();
		}
		if (kt == Types.NO_TYPE) {
			kt = getKeyType();
		}
		return new ParametricType(this, kt, ct);

	}

	/**
	 * Method setDefiningPlugin()
	 * 
	 * @see msi.gaml.types.IType#setDefiningPlugin(java.lang.String)
	 */
	@Override
	public void setDefiningPlugin(final String plugin) {
	}

	@Override
	public void collectMetaInformation(final GamlProperties meta) {
		type.collectMetaInformation(meta);
		contentsType.collectMetaInformation(meta);
		keyType.collectMetaInformation(meta);
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public IType getWrappedType() {
		return Types.NO_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see msi.gaml.types.IType#getFieldGetters()
	 */
	@Override
	public Map getFieldGetters() {
		return type.getFieldGetters();
	}

}
