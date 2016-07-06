/*********************************************************************************************
 * 
 *
 * 'TypeConverter.java', in plugin 'msi.gama.processor', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.precompiler.doc.utils;

import java.util.HashMap;

import msi.gama.precompiler.IOperatorCategory;

public class TypeConverter {
	
	HashMap<String, String> properNameTypeMap;
	HashMap<String, String> properCategoryNameMap;
	HashMap<Integer, String> typeStringFromIType;
	HashMap<Integer, String> symbolKindStringFromISymbolKind;
	
	public TypeConverter(){
		properNameTypeMap = initProperNameTypeMap();
		properCategoryNameMap = initProperNameCategoriesMap();
		typeStringFromIType = initNameTypeFromIType();
		symbolKindStringFromISymbolKind = initSymbolKindStringFromISymbolKind();
	}

	private HashMap<Integer, String> initSymbolKindStringFromISymbolKind() {
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		hm.put(0, "Species");
		hm.put(1, "Model");
		hm.put(2, "Single statement");
		hm.put(3, "Behavior");
		hm.put(4, "Parameter");
		hm.put(5, "Output");
		hm.put(6, "Layer");
		hm.put(7, "Skill");
		hm.put(8, "Batch section");
		hm.put(9, "Batch method");
		hm.put(10, "Environment");
		hm.put(11, "Sequence of statements or action");
		hm.put(13, "Experiment");
		hm.put(14, "Abstract section");
		hm.put(101, "Variable (number)");
		hm.put(102, "Variable (container)");
		hm.put(103, "Variable (signal)");
		hm.put(104, "Variable (regular)");
		return hm;
	}
	
	private HashMap<String, String> initProperNameTypeMap() {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("msi.gama.metamodel.shape.IShape", "geometry");
		hm.put("msi.gama.metamodel.shape.GamaShape", "geometry");
		
		hm.put("java.lang.Integer", "int");
		hm.put("java.lang.Double", "float");	
		hm.put("java.lang.Long", "float");		
		hm.put("double", "float");	
		hm.put("boolean", "bool");		
		
		// Matrix
		hm.put("msi.gama.util.matrix.IMatrix<T>", "matrix");
		hm.put("msi.gama.util.matrix.IMatrix", "matrix");
		hm.put("msi.gama.util.matrix.GamaMatrix", "matrix");		
		hm.put("msi.gama.util.matrix.GamaIntMatrix", "matrix<int>");
		hm.put("msi.gama.util.matrix.GamaMatrix<T>", "matrix");
		hm.put("msi.gama.util.matrix.GamaMatrix<java.lang.Double>", "matrix<float>");
		hm.put("msi.gama.util.matrix.GamaFloatMatrix", "matrix<float>");
		hm.put("msi.gama.util.matrix.IMatrix<java.lang.Double>", "matrix<float>");
		
		// Files
		hm.put("msi.gama.util.file.IGamaFile", "file");
		hm.put("msi.gama.util.file.GamaFile", "file");		
		hm.put("msi.gama.jogl.files.Gama3DSFile", "file");
		hm.put("msi.gama.jogl.files.GamaObjFile", "file");
		
		// Colors
		hm.put("msi.gama.util.GamaColor", "rgb");
		
		// List
		hm.put("msi.gama.util.IList", "list");
		hm.put("msi.gama.util.GamaList", "list");
		hm.put("java.util.List", "list");
		hm.put("java.util.List<T>", "list");
		hm.put("msi.gama.util.IList<T>", "list");
		hm.put("msi.gama.util.IList<java.lang.Object>", "list");
		hm.put("java.util.List<java.lang.Object>", "list");
		hm.put("msi.gama.util.IList<msi.gama.util.IList<T>>", "list<list>");
		hm.put("msi.gama.util.IList<msi.gama.util.IList>", "list<list>");	
		hm.put("msi.gama.util.GamaList<msi.gama.util.GamaList>", "list<list>");
		hm.put("java.util.List<java.util.List>", "list<list>");
		hm.put("msi.gama.util.IList<msi.gama.metamodel.shape.IShape>", "list<geometry>");
		hm.put("msi.gama.util.IList<msi.gama.metamodel.shape.GamaPoint>", "list<point>");
		hm.put("msi.gama.util.IList<msi.gama.metamodel.agent.IAgent>", "list<agent>");
		hm.put("java.util.List<java.util.List<msi.gama.metamodel.agent.IAgent>>", "list<list<agent>>");
		hm.put("msi.gama.util.IList<msi.gama.util.IList<msi.gama.metamodel.agent.IAgent>>", "list<list<agent>>");
		hm.put("msi.gama.util.GamaList<msi.gama.metamodel.agent.IAgent>", "list<agent>");
		hm.put("msi.gama.util.GamaList<msi.gama.metamodel.shape.IShape>", "list<geometry>");
		hm.put("msi.gama.util.GamaList<msi.gama.util.GamaList<msi.gama.metamodel.shape.GamaPoint>>","list<list<point>>");
		hm.put("msi.gama.util.IList<msi.gama.metamodel.shape.ILocation>", "list<point>");		
		hm.put("java.util.List<msi.gama.util.path.GamaSpatialPath>", "list<path>");		
		hm.put("msi.gama.util.GamaList<java.lang.Double>", "list<float>");
		hm.put("msi.gama.util.IList<java.lang.Double>", "list<float>");
		hm.put("msi.gama.util.IList<msi.gama.util.GamaColor>", "list<rgb>");
		hm.put("msi.gama.util.IList<KeyType>", "list<KeyType>");
		
		hm.put("msi.gama.metamodel.shape.GamaPoint", "point");
		hm.put("msi.gama.metamodel.shape.ILocation", "point");
		hm.put("java.lang.Object", "unknown");
		hm.put("msi.gama.util.GamaPair", "pair");
		hm.put("java.lang.Boolean", "bool");
		hm.put("msi.gama.metamodel.agent.IAgent", "agent");
		hm.put("java.lang.String", "string");
		hm.put("msi.gama.util.graph.IGraph", "graph");
		hm.put("msi.gama.util.graph.GamaGraph", "graph");
		hm.put("msi.gama.metamodel.topology.ITopology", "topology");
		hm.put("msi.gama.util.GamaMap", "map");
		hm.put("msi.gaml.expressions.IExpression", "any expression");
		hm.put("msi.gaml.species.ISpecies", "species");
		
		hm.put("msi.gama.util.IContainer", "container");
		hm.put("msi.gama.util.IContainer<KeyType,ValueType>", "container<KeyType,ValueType>");
		hm.put("msi.gama.util.IContainer<?,msi.gama.metamodel.shape.IShape>", "container<geometry>");
		hm.put("msi.gama.util.IContainer<?,msi.gama.metamodel.agent.IAgent>", "container<agent>");
		hm.put("msi.gama.util.IContainer<?,? extends msi.gama.metamodel.shape.IShape>", "container<agent>");
		hm.put("msi.gama.util.IContainer<?,java.lang.Double>", "container<float>");
		
		hm.put("msi.gama.util.IList", "container");
		hm.put("msi.gama.util.IList<KeyType,ValueType>", "list<KeyType,ValueType>");
		hm.put("msi.gama.util.IList<?,msi.gama.metamodel.shape.IShape>", "list<geometry>");
		hm.put("msi.gama.util.IList<?,msi.gama.metamodel.agent.IAgent>", "list<agent>");
		hm.put("msi.gama.util.IList<?,? extends msi.gama.metamodel.shape.IShape>", "list<agent>");
		hm.put("msi.gama.util.IList<?,java.lang.Double>", "list<float>");
		hm.put("msi.gama.util.IList<? extends msi.gama.metamodel.shape.IShape>", "list<geometry>");
		
		hm.put("java.util.Map", "map");
		hm.put("msi.gama.util.GamaMap<?,?>", "map");	
		hm.put("java.util.Map<java.lang.String,java.lang.Object>", "map<string,unknown>");
		hm.put("java.util.List<java.util.Map<java.lang.String,java.lang.Object>>", "list<map<string,object>>");
		hm.put("msi.gama.util.GamaMap<java.lang.String,java.lang.Object>", "map<string,unknown>");
		hm.put("msi.gama.util.GamaMap<java.lang.String,msi.gama.util.GamaList>", "map<string,list>");
		hm.put("msi.gama.util.GamaMap<msi.gama.metamodel.shape.GamaPoint,java.lang.Double>","map<point,float>");
		hm.put("msi.gama.util.GamaMap<msi.gama.metamodel.shape.IShape,java.lang.Double>","map<agent,float>");
		
		hm.put("msi.gama.util.IList<java.lang.String>", "list<string>");
		
		hm.put("msi.gama.util.GamaFont", "font");
		
		hm.put("msi.gama.util.GamaRegression", "regression");
		
		hm.put("msi.gama.util.GamaDate","date");
		
		// BDI
		hm.put("msi.gaml.architecture.simplebdi.Predicate", "predicate");	
		hm.put("msi.gaml.architecture.simplebdi.BDIPlan", "BDIPlan");
		hm.put("msi.gaml.architecture.simplebdi.Emotion", "emotion");
		
		// FIPA
		hm.put("msi.gaml.extensions.fipa.Conversation", "conversation");
		hm.put("msi.gaml.extensions.fipa.Message", "message");
		
		hm.put("msi.gama.util.IPath", "path");		
		hm.put("msi.gama.util.path.IPath", "path");

		
		hm.put("msi.gama.util.IContainer<KeyType,ValueType>.Addressable<KeyType,ValueType>", "container<KeyType,ValueType>");
		hm.put("msi.gama.util.IAddressableContainer<java.lang.Integer,msi.gama.metamodel.agent.IAgent,java.lang.Integer,msi.gama.metamodel.agent.IAgent>","list<agent>");
		// msi.gama.util.GamaRegression ??????
		// msi.gama.util.GamaFont ????
		return hm;
	}
	
	// FROM IType.java
	private HashMap<Integer,String> initNameTypeFromIType(){
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		hm.put(0, "any type");  // NONE
		hm.put(1, "int");
		hm.put(2, "float");
		hm.put(3, "boolean");
		hm.put(4, "string");
		hm.put(5, "list");
		hm.put(6, "rgb");
		hm.put(7, "point");
		hm.put(8, "matrix");
		hm.put(9, "pair");
		hm.put(10, "map");
		hm.put(11, "agent");		
		hm.put(12, "file");
		hm.put(13, "geometry");
		hm.put(14, "species");
		hm.put(15, "graph");
		hm.put(16, "container");
		hm.put(17, "path");
		hm.put(18, "topology");
		hm.put(50, "available_types");
		hm.put(99, "message");
		hm.put(100, "species_types");
		
		hm.put(-200, "a label");
		hm.put(-201, "an identifier");
		hm.put(-202, "a datatype identifier");
		hm.put(-203, "a new identifier");
		hm.put(-204, "a new identifier");
		return hm;
	}
	
	private HashMap<String, String> initProperNameCategoriesMap() {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Cast", 					IOperatorCategory.CASTING);
		hm.put("Colors", 			IOperatorCategory.COLOR);
		hm.put("DrivingOperators", 	IOperatorCategory.DRIVING);
		hm.put("Comparison", 			IOperatorCategory.COMPARISON);
		hm.put("IContainer", 		IOperatorCategory.CONTAINER);
		hm.put("Containers", 		IOperatorCategory.CONTAINER);
		hm.put("GamaMap", 			IOperatorCategory.CONTAINER);
		hm.put("Files", 				IOperatorCategory.FILE);
		hm.put("GamaFileType", 			IOperatorCategory.FILE);
		hm.put("MessageType",		IOperatorCategory.FIPA);
		hm.put("ConversationType", 	IOperatorCategory.FIPA);
		hm.put("Graphs", 				IOperatorCategory.GRAPH);
		hm.put("GraphsGraphstream", 	IOperatorCategory.GRAPH);
		hm.put("Logic", 			IOperatorCategory.LOGIC);
		hm.put("Maths", 				IOperatorCategory.ARITHMETIC);
		hm.put("GamaFloatMatrix", 	IOperatorCategory.MATRIX);
		hm.put("GamaIntMatrix", 	IOperatorCategory.MATRIX);
		hm.put("GamaMatrix", 		IOperatorCategory.MATRIX);
		hm.put("GamaObjectMatrix", 	IOperatorCategory.MATRIX);
		hm.put("IMatrix", 			IOperatorCategory.MATRIX);
		hm.put("SingleEquationStatement", IOperatorCategory.EDP);
		hm.put("Creation", 			IOperatorCategory.SPATIAL);
		hm.put("Operators", 		IOperatorCategory.SPATIAL);
		hm.put("Points", 			IOperatorCategory.SPATIAL);
		hm.put("Properties", 		IOperatorCategory.SPATIAL);
		hm.put("Punctal", 			IOperatorCategory.SPATIAL);
		hm.put("Queries", 			IOperatorCategory.SPATIAL);
		hm.put("ThreeD", 			IOperatorCategory.SPATIAL);
		hm.put("Statistics", 		IOperatorCategory.SPATIAL);
		hm.put("Transformations", 	IOperatorCategory.SPATIAL);
		hm.put("Relations", 		IOperatorCategory.SPATIAL);
		hm.put("Random", 				IOperatorCategory.RANDOM);
		hm.put("Stats", 			IOperatorCategory.STATISTICAL);
		hm.put("Strings", 				IOperatorCategory.STRING);		
		hm.put("System", 			IOperatorCategory.SYSTEM);
		hm.put("Types", 				IOperatorCategory.TYPE);
		hm.put("WaterLevel", 		IOperatorCategory.WATER);
		return hm;
	}
	
	public String getProperType(String rawName) {
		if ( properNameTypeMap.containsKey(rawName) ) {
			return properNameTypeMap.get(rawName);
		} else {
			return rawName;
		}
	}

	public String getProperOperatorName(String opName) {
		// if("*".equals(opName)) return "`*`";
		return opName;
	}

	public String getProperCategory(String rawName) {
		if ( properCategoryNameMap.containsKey(rawName) ) {
			return properCategoryNameMap.get(rawName);
		} else {
			return rawName;
		}
	}
	
	public String getTypeString(Integer i){
		if ( typeStringFromIType.containsKey(i) ) {
			return typeStringFromIType.get(i);
		} else {
			return ""+i;
		}		
	}	

	public String getTypeString(int[] types){
		StringBuilder s = new StringBuilder(30);
		s.append(types.length < 2 ? "" : "any type in [");
		for ( int i = 0; i < types.length; i++ ) {
			s.append(getTypeString(types[i]));

			if ( i != types.length - 1 ) {
				s.append(", ");
			}
		}
		if ( types.length >= 2 ) {
			s.append("]");
		}
		return s.toString();
	}	
	
	public String getSymbolKindStringFromISymbolKind(Integer i){
		if ( symbolKindStringFromISymbolKind.containsKey(i) ) {
			return symbolKindStringFromISymbolKind.get(i);
		} else {
			return ""+i;
		}		
	}
}
