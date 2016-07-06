/*********************************************************************************************
 *
 *
 * 'IGamaFile.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.util.file;

import com.vividsolutions.jts.geom.Envelope;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.precompiler.ITypeProvider;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gaml.types.IType;

/**
 * Written by drogoul
 * Modified on 14 nov. 2011
 *
 * @todo Description
 *
 * @param <K>
 * @param <V>
 */
@vars({ @var(name = IKeyword.NAME, type = IType.STRING, doc = { @doc("Returns the name of the receiver file") }),
	@var(name = IKeyword.EXTENSION, type = IType.STRING, doc = { @doc("Returns the extension of the receiver file") }),
	@var(name = IKeyword.PATH, type = IType.STRING, doc = { @doc("Returns the absolute path of the receiver file") }),
	@var(name = IKeyword.EXISTS,
	type = IType.BOOL,
	doc = { @doc("Returns whether the receiver file exists or not in the filesystem") }),
	@var(name = IKeyword.ISFOLDER,
	type = IType.BOOL,
	doc = { @doc("Returns whether the receiver file is a folder or not") }),
	@var(name = IKeyword.READABLE,
	type = IType.BOOL,
	doc = { @doc("Returns true if the contents of the receiver file can be read") }),
	@var(name = IKeyword.WRITABLE,
	type = IType.BOOL,
	doc = { @doc("Returns true if the contents of the receiver file can be written") }),
	@var(name = IKeyword.ATTRIBUTES,
	type = IType.LIST,
	of = IType.STRING,
	doc = {
		@doc("Retrieves the list of 'attributes' present in the receiver files that support this concept (and an empty list for the others). For instance, in a CSV file, the attributes represent the headers of the columns (if any); in a shape file, the attributes provided to the objects, etc.") }),
	@var(name = IKeyword.CONTENTS,
	type = ITypeProvider.WRAPPED,
	of = ITypeProvider.FIRST_CONTENT_TYPE,
	index = ITypeProvider.FIRST_KEY_TYPE,
	doc = { @doc("Returns the contents of the receiver file in the form of a container") }) })
public interface IGamaFile<C extends IModifiableContainer, ValueToAdd, K, V> extends IModifiableContainer<K, V, K, ValueToAdd>, IAddressableContainer<K, V, K, V> {

	public abstract void setWritable(final boolean w);

	public abstract void setContents(final C cont) throws GamaRuntimeException;

	@Override
	public abstract IGamaFile copy(IScope scope);

	@getter(value = IKeyword.EXISTS, initializer = true)
	public abstract Boolean exists();

	@getter(value = IKeyword.EXTENSION, initializer = true)
	public abstract String getExtension();

	@getter(value = IKeyword.NAME, initializer = true)
	public abstract String getName();

	@getter(value = IKeyword.PATH, initializer = true)
	public abstract String getPath();

	@getter(IKeyword.CONTENTS)
	public abstract C getContents(IScope scope) throws GamaRuntimeException;

	@getter(IKeyword.ATTRIBUTES)
	/**
	 * Retrieves the list of "attributes" present in files that support this concept (and an empty list for the others). For instance, in a CSV file, attributes represent the headers of the columns
	 * (if any); in a shape file, the attributes provided to the objects, etc.
	 * @param scope
	 * @return a list of string or an empty list (never null)
	 */
	public abstract IList<String> getAttributes(IScope scope);

	@getter(value = IKeyword.ISFOLDER, initializer = true)
	public abstract Boolean isFolder();

	@getter(value = IKeyword.READABLE, initializer = true)
	public abstract Boolean isReadable();

	@getter(value = IKeyword.WRITABLE, initializer = true)
	public abstract Boolean isWritable();

	public Envelope computeEnvelope(final IScope scope);

}