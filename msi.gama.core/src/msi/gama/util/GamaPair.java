/*********************************************************************************************
 *
 *
 * 'GamaPair.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.util;

import java.util.Map;

import gnu.trove.impl.HashFunctions;
import msi.gama.common.util.StringUtils;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.precompiler.ITypeProvider;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.matrix.IMatrix;
import msi.gaml.operators.Cast;
import msi.gaml.types.*;

/**
 * The Class GamaPair.
 */
@vars({
	@var(name = GamaPair.KEY,
		type = ITypeProvider.FIRST_KEY_TYPE,
		doc = { @doc("Returns the key of this pair (can be nil)") }),
	@var(name = GamaPair.VALUE,
	type = ITypeProvider.FIRST_CONTENT_TYPE,
	doc = { @doc("Returns the value of this pair (can be nil)") }) })
public class GamaPair<K, V> implements IContainer<Integer, Object>, IContainer.Addressable<Integer, Object>, Map.Entry<K, V> {

	// TODO Makes it inherit from Map.Entry<K,V> in order to tighten the link between it and GamaMap
	// (have the entrySet() of GamaMap built from GamaPairs)
	// FIXME: This has still to be implemented

	public static final String KEY = "key";
	public static final String VALUE = "value";

	private final IContainerType type;
	public K key;
	public V value;

	public GamaPair(final K k, final V v, final IType keyType, final IType contentsType) {
		key = k;
		value = v;
		type = Types.PAIR.of(keyType, contentsType);
	}

	public GamaPair(final IScope scope, final K k, final V v, final IType keyType, final IType contentsType) {
		key = (K) keyType.cast(scope, k, null, false);
		value = (V) contentsType.cast(scope, v, null, false);
		type = Types.PAIR.of(keyType, contentsType);
	}

	public GamaPair(final Map.Entry<K, V> entry, final IType keyType, final IType contentsType) {
		this(entry.getKey(), entry.getValue(), keyType, contentsType);
	}

	public boolean equals(final GamaPair p) {
		return key.equals(p.key) && value.equals(p.value);
	}
	
	public int hashCode() {
		return HashFunctions.hash(key) + 1024 * HashFunctions.hash(value);
	}

	@Override
	public boolean equals(final Object a) {
		if ( a == null ) { return false; }
		if ( a instanceof GamaPair ) { return equals((GamaPair) a); }
		return false;
	}

	@Override
	public IContainerType getType() {
		return type;
	}

	@Override
	@getter(KEY)
	public K getKey() {
		return key;
	}

	// FIXME: To be removed
	public K first() {
		return key;
	}

	@Override
	@getter(VALUE)
	public V getValue() {
		return value;
	}

	// FIXME: To be removed
	public V last() {
		return value;
	}

	@Override
	public String stringValue(final IScope scope) throws GamaRuntimeException {
		return Cast.asString(scope, key) + "::" + Cast.asString(scope, value);
	}

	@Override
	public String serialize(final boolean includingBuiltIn) {
		return StringUtils.toGaml(key, includingBuiltIn) + "::" + StringUtils.toGaml(value, includingBuiltIn);
	}

	@Override
	public String toString() {
		return (key == null ? "nil" : key.toString()) + "::" + (value == null ? "nil" : value.toString());
	}

	@Override
	public GamaPair<K, V> copy(final IScope scope) {
		return new GamaPair(key, value, type.getKeyType(), type.getContentType());
	}

	@Override
	public V setValue(final V value) {
		this.value = value;
		return value;
	}

	/**
	 * Method get()
	 * @see msi.gama.util.IContainer#get(msi.gama.runtime.IScope, java.lang.Object)
	 */
	@Override
	public Object get(final IScope scope, final Integer index) throws GamaRuntimeException {
		return index == 0 ? key : value;
	}

	/**
	 * Method getFromIndicesList()
	 * @see msi.gama.util.IContainer#getFromIndicesList(msi.gama.runtime.IScope, msi.gama.util.IList)
	 */
	@Override
	public Object getFromIndicesList(final IScope scope, final IList indices) throws GamaRuntimeException {
		return null;
	}

	/**
	 * Method contains()
	 * @see msi.gama.util.IContainer#contains(msi.gama.runtime.IScope, java.lang.Object)
	 */
	@Override
	public boolean contains(final IScope scope, final Object o) throws GamaRuntimeException {
		return o == null ? key == null || value == null : o.equals(key) || o.equals(value);
	}

	/**
	 * Method firstValue()
	 * @see msi.gama.util.IContainer#firstValue(msi.gama.runtime.IScope)
	 */
	@Override
	public Object firstValue(final IScope scope) throws GamaRuntimeException {
		return key;
	}

	/**
	 * Method lastValue()
	 * @see msi.gama.util.IContainer#lastValue(msi.gama.runtime.IScope)
	 */
	@Override
	public Object lastValue(final IScope scope) throws GamaRuntimeException {
		return value;
	}

	/**
	 * Method length()
	 * @see msi.gama.util.IContainer#length(msi.gama.runtime.IScope)
	 */
	@Override
	public int length(final IScope scope) {
		return 2;
	}

	/**
	 * Method isEmpty()
	 * @see msi.gama.util.IContainer#isEmpty(msi.gama.runtime.IScope)
	 */
	@Override
	public boolean isEmpty(final IScope scope) {
		return false;
	}

	/**
	 * Method reverse()
	 * @see msi.gama.util.IContainer#reverse(msi.gama.runtime.IScope)
	 */
	@Override
	public IContainer reverse(final IScope scope) throws GamaRuntimeException {
		return new GamaPair(value, key, type.getContentType(), type.getKeyType());
	}

	/**
	 * Method anyValue()
	 * @see msi.gama.util.IContainer#anyValue(msi.gama.runtime.IScope)
	 */
	@Override
	public Object anyValue(final IScope scope) {
		int i = scope.getRandom().between(0, 1);
		return i == 0 ? key : value;
	}

	/**
	 * Method listValue()
	 * @see msi.gama.util.IContainer#listValue(msi.gama.runtime.IScope, msi.gaml.types.IType)
	 */
	@Override
	public IList listValue(final IScope scope, final IType contentType, final boolean copy) {
		return GamaListFactory.createWithoutCasting(contentType, contentType.cast(scope, key, null, copy),
			contentType.cast(scope, value, null, copy));
	}

	/**
	 * Method matrixValue()
	 * @see msi.gama.util.IContainer#matrixValue(msi.gama.runtime.IScope, msi.gaml.types.IType)
	 */
	@Override
	public IMatrix matrixValue(final IScope scope, final IType contentType, final boolean copy) {
		return GamaMatrixType.from(scope, listValue(scope, contentType, copy), contentType, null);
	}

	/**
	 * Method matrixValue()
	 * @see msi.gama.util.IContainer#matrixValue(msi.gama.runtime.IScope, msi.gaml.types.IType, msi.gama.metamodel.shape.ILocation)
	 */
	@Override
	public IMatrix matrixValue(final IScope scope, final IType contentType, final ILocation size, final boolean copy) {
		return GamaMatrixType.from(scope, listValue(scope, contentType, copy), contentType, size);
	}

	/**
	 * Method mapValue()
	 * @see msi.gama.util.IContainer#mapValue(msi.gama.runtime.IScope, msi.gaml.types.IType, msi.gaml.types.IType)
	 */
	@Override
	public GamaMap mapValue(final IScope scope, final IType keyType, final IType contentType, final boolean copy) {
		GamaMap result = GamaMapFactory.create(keyType, contentType);
		result.setValueAtIndex(scope, key, value);
		return result;
	}

	/**
	 * Method iterable()
	 * @see msi.gama.util.IContainer#iterable(msi.gama.runtime.IScope)
	 */
	@Override
	public java.lang.Iterable iterable(final IScope scope) {
		return listValue(scope, Types.NO_TYPE, false);
	}

}
