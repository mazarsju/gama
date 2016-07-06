/*********************************************************************************************
 *
 *
 * 'Files.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.operators;

import java.io.File;
import java.util.Map;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.common.util.FileUtils;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.metamodel.shape.IShape;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.operator;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.IOperatorCategory;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaList;
import msi.gama.util.GamaMap;
import msi.gama.util.IContainer;
import msi.gama.util.file.GamaFolderFile;
import msi.gama.util.file.GamaOsmFile;
import msi.gama.util.file.IGamaFile;
import msi.gaml.types.IType;
import msi.gaml.types.Types;

/**
 * Written by drogoul Modified on 20 dec. 2010
 *
 * @todo Description
 *
 */
public class Files {

	public static final String FOLDER = "folder";
	public static final String WRITE = "write";

	@operator(value = IKeyword.FILE, can_be_const = true, category = IOperatorCategory.FILE, concept = {
			IConcept.FILE })
	@doc(value = "Creates a file in read/write mode, setting its contents to the container passed in parameter", comment = "The type of container to pass will depend on the type of file (see the management of files in the documentation). Can be used to copy files since files are considered as containers. For example: save file('image_copy.png', file('image.png')); will copy image.png to image_copy.png")
	public static IGamaFile from(final IScope scope, final String s, final IContainer container) {
		// WARNING Casting to Modifiable is not safe
		// TODO: Add a method toModifiableVersion() to IContainer
		final IType key = container == null ? Types.NO_TYPE : container.getType().getKeyType();
		final IType content = container == null ? Types.NO_TYPE : container.getType().getContentType();
		return (IGamaFile) Types.FILE.cast(scope, s, container, key, content, false);
	}

	@operator(value = IKeyword.FILE, can_be_const = true, category = IOperatorCategory.FILE, concept = {
			IConcept.FILE })
	@doc(value = "opens a file in read only mode, creates a GAML file object, and tries to determine and store the file content in the contents attribute.", comment = "The file should have a supported extension, see file type definition for supported file extensions.", usages = @usage("If the specified string does not refer to an existing file, an exception is risen when the variable is used."), examples = {
			@example(value = "let fileT type: file value: file(\"../includes/Stupid_Cell.Data\"); "),
			@example(value = "			// fileT represents the file \"../includes/Stupid_Cell.Data\""),
			@example(value = "			// fileT.contents here contains a matrix storing all the data of the text file") }, see = {
					"folder", "new_folder" })
	public static IGamaFile from(final IScope scope, final String s) throws GamaRuntimeException {
		return from(scope, s, null);
	}

	@operator(value = "file_exists", can_be_const = true, category = IOperatorCategory.FILE, concept = {
			IConcept.FILE })
	@doc(value = "Test whether the parameter is the path to an existing file.")
	public static boolean exist_file(final IScope scope, final String s) {
		if (s == null) {
			return false;
		}
		if (scope == null) {
			return false;
		} else {
			final String path = FileUtils.constructAbsoluteFilePath(scope, s, false);
			final File f = new File(path);

			return f.exists() && !f.isDirectory();
		}
	}

	// FIXME These methods should not be necessary. To remove at some point in
	// favor of the constructors
	@operator(value = "osm_file", can_be_const = true, index_type = IType.INT, category = IOperatorCategory.FILE, concept = {
			IConcept.FILE, IConcept.OSM })
	@doc(value = "opens a file that a is a kind of OSM file with some filtering.", masterDoc = true, comment = "The file should have a OSM file extension, cf. file type definition for supported file extensions.", usages = @usage("If the specified string does not refer to an existing OSM file, an exception is risen."), examples = {
			@example(value = "file myOSMfile <- osm_file(\"../includes/rouen.osm\", [\"highway\"::[\"primary\",\"motorway\"]]);", test = false) }, see = {
					"file" })
	public static IGamaFile loadOSMFileWithFiltering(final IScope scope, final String s,
			final GamaMap<String, GamaList> filteringOption) throws GamaRuntimeException {
		return new GamaOsmFile(scope, s, filteringOption);
	}

	@operator(value = "osm_file", can_be_const = true, index_type = IType.INT, category = IOperatorCategory.FILE, concept = {})
	@doc(value = "opens a file that a is a kind of OSM file with some filtering, forcing the initial CRS to be the one indicated by the second int parameter (see http://spatialreference.org/ref/epsg/). If this int parameter is equal to 0, the data is considered as already projected.", masterDoc = true, comment = "The file should have a OSM file extension, cf. file type definition for supported file extensions.", usages = @usage("If the specified string does not refer to an existing OSM file, an exception is risen."), examples = {
			@example(value = "file myOSMfile2 <- osm_file(\"../includes/rouen.osm\",[\"highway\"::[\"primary\",\"motorway\"]], 0);", test = false) }, see = {
					"file" })
	public static IGamaFile loadOSMFileWithFiltering(final IScope scope, final String s,
			final GamaMap<String, GamaList> filteringOption, final Integer code) throws GamaRuntimeException {
		return new GamaOsmFile(scope, s, filteringOption, code);
	}

	@operator(value = FOLDER, can_be_const = true, index_type = IType.INT, category = IOperatorCategory.FILE, concept = {
			IConcept.FILE })
	@doc(value = "opens an existing repository", usages = @usage("If the specified string does not refer to an existing repository, an exception is risen."), examples = {
			@example(value = "folder(\"../includes/\")", raises = "error"),
			@example(value = "file dirT <- folder(\"../includes/\");", isExecutable = false),
			@example(value = "				// dirT represents the repository \"../includes/\""),
			@example(value = "				// dirT.contents here contains the list of the names of included files") }, see = {
					"file", "new_folder" })
	public static IGamaFile folderFile(final IScope scope, final String s) throws GamaRuntimeException {
		return new GamaFolderFile(scope, s);
	}

	@operator(value = "writable", category = IOperatorCategory.FILE, concept = { IConcept.FILE })
	@doc(value = "Marks the file as read-only or not, depending on the second boolean argument, and returns the first argument", comment = "A file is created using its native flags. This operator can change them. Beware that this change is system-wide (and not only restrained to GAMA): changing a file to read-only mode (e.g. \"writable(f, false)\")", examples = {
			@example(value = "shape_file(\"../images/point_eau.shp\") writable false", equals = "returns a file in read-only mode", test = false) }, see = "file")
	public static IGamaFile writable(final IScope scope, final IGamaFile s, final Boolean writable) {
		if (s == null) {
			throw GamaRuntimeException.error("Attempt to change the mode of a non-existent file", scope);
		}
		final boolean b = writable == null ? false : writable;
		s.setWritable(b);
		return s;
	}

	/**
	 * Allows to read the value of an attribute stored in a GIS if the agent has
	 * been created from this GIS. Values are either conserved in a special
	 * subclass of GamaGeometry or available during creation time in the flow of
	 * features.
	 *
	 * @param scope
	 *            the current execution stack
	 * @param s
	 *            the name of the attribute to read
	 * @return
	 */
	@operator(value = { "read", "get" }, category = IOperatorCategory.FILE, concept = { IConcept.ATTRIBUTE,
			IConcept.FILE })
	@doc(value = "Reads an attribute of the agent. The attribute's name is specified by the operand.", masterDoc = true, examples = {
			@example(var = "agent_name", value = "read ('name')", equals = "reads the 'name' variable of agent then assigns the returned value to the 'agent_name' variable. ", test = false) })
	public static Object opRead(final IScope scope, final String s) throws GamaRuntimeException {
		// First try to read in the temp attributes
		final Map attributes = scope.peekReadAttributes();
		if (attributes != null) {
			return attributes.get(s);
		}
		// Then try to read in the agent, if it has been created from a GIS/CSV
		// file.
		return opRead(scope, scope.getAgentScope(), s);
	}

	@operator(value = "get", category = IOperatorCategory.CONTAINER, concept = { IConcept.CONTAINER, IConcept.SPECIES,
			IConcept.ATTRIBUTE })
	// @doc(examples = {
	// "let agent_name value: an_agent get ('name'); --: reads the 'name'
	// variable of agent then assigns the returned value to the
	// 'second_variable' variable."
	// })
	@doc(value = "Reads an attribute of the specified agent (left operand). The attribute name is specified by the right operand.", usages = {
			@usage(value = "Reading the attribute of another agent", examples = @example(value = "string agent_name <- an_agent get('name');     // reads then 'name' attribute of an_agent then assigns the returned value to the agent_name variable", isExecutable = false)) })
	public static Object opRead(final IScope scope, final IAgent g, final String s) throws GamaRuntimeException {
		if (g == null) {
			return null;
		}
		return g.get(scope, s);
	}

	@operator(value = "get", category = IOperatorCategory.FILE, concept = { IConcept.GEOMETRY })
	// @doc(examples = {
	// "let geom_area value: a_geometry get ('area'); --: reads the 'area'
	// attribute of the 'a_geometry' geometry then assigns the returned value to
	// the 'geom_area' variable."
	// })
	@doc(value = "Reads an attribute of the specified geometry (left operand). The attribute name is specified by the right operand.", usages = {
			@usage(value = "Reading the attribute of a geometry", examples = @example(value = "string geom_area <- a_geometry get('area');     // reads then 'area' attribute of 'a_geometry' variable then assigns the returned value to the geom_area variable", isExecutable = false)) })
	public static Object opRead(final IScope scope, final IShape g, final String s) throws GamaRuntimeException {
		if (g == null) {
			return null;
		}
		return ((GamaShape) g.getGeometry()).getAttribute(s);
	}

	@operator(value = {
			"new_folder" }, index_type = IType.INT, content_type = IType.STRING, category = IOperatorCategory.FILE, concept = {
					IConcept.FILE })
	@doc(value = "opens an existing repository or create a new folder if it does not exist.", comment = "", usages = {
			@usage("If the specified string does not refer to an existing repository, the repository is created."),
			@usage("If the string refers to an existing file, an exception is risen.") }, examples = {
					@example("file dirNewT <- new_folder(\"incl/\");   	// dirNewT represents the repository \"../incl/\""),
					@example("															// eventually creates the directory ../incl") }, see = {
							"folder", "file" })
	public static IGamaFile newFolder(final IScope scope, final String folder) throws GamaRuntimeException {
		String theName;
		theName = FileUtils.constructAbsoluteFilePath(scope, folder, false);

		final File file = new File(theName);
		if (file.exists() && !file.isDirectory()) {
			throw GamaRuntimeException.error("The folder " + folder + " can not overwrite a file with the same name",
					scope);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		return new GamaFolderFile(scope, folder);

	}

}
