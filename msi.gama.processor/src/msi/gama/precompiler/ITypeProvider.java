/*********************************************************************************************
 *
 *
 * 'ITypeProvider.java', in plugin 'msi.gama.processor', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.precompiler;

/**
 * Written by drogoul Modified on 2 ao�t 2010. See IType
 *
 * @todo Description
 *
 */
public interface ITypeProvider {

	/*
	 * The content type is provided by the content type of the first child of the expression
	 */
	static final int FIRST_CONTENT_TYPE = -20;
	static final int OWNER_CONTENT_TYPE = FIRST_CONTENT_TYPE;
	static final int FIRST_KEY_TYPE = -23;
	static final int OWNER_KEY_TYPE = FIRST_KEY_TYPE;
	/*
	 * The content type is provided by the content type of the second child of the expression
	 */
	static final int SECOND_CONTENT_TYPE = -19;
	static final int SECOND_KEY_TYPE = -24;
	/*
	 * The content type is provided by the type of the first child of the expression
	 */
	static final int FIRST_TYPE = -18;
	/**
	 * For variables, represents the type of the owner (i.e. the species) holding this attribute
	 */
	static final int OWNER_TYPE = FIRST_TYPE;
	/*
	 * The content type is provided by the type of the second child of the expression
	 */
	static final int SECOND_TYPE = -17;
	static final int SECOND_CONTENT_TYPE_OR_TYPE = -25;
	static final int FIRST_CONTENT_TYPE_OR_TYPE = -26;

	/**
	 * The type of the model itself
	 */
	static final int MODEL_TYPE = -27;
	/**
	 * The type of the agents mirrored by a species
	 */
	static final int MIRROR_TYPE = -28;
	/**
	 * The type of the macro-agent
	 */
	static final int MACRO_TYPE = -29;
	/*
	 * The type returned is the type of the internal buffer of the object (file)
	 */
	static final int WRAPPED = -30;
	/*
	 * The content type is provided by the type of the expression itself (i.e. species)
	 */
	static final int TYPE = -14;
	/*
	 * The content type cannot be computed
	 */
	static final int NONE = -13;
	/*
	 * The type or content type are provided by both or all operands (which must match).
	 */
	static final int BOTH = -21;
	static final int ALL = BOTH;
	/*
	 * The content type is provided by the content type of the first element of the child (if the
	 * child is a container) -- EXPERIMENTAL RIGHT NOW (and probably limited to the matrix and
	 * as_matrix operators)
	 * e.g. : matrix ([[4, 5, 6],[1, 2, 4]]) should get int as a content type.
	 */
	static final int FIRST_ELEMENT_CONTENT_TYPE = -22;

	/**
	 * The type, content type, key type are provided by the element computed by its index. 0 for the first element, 1
	 * for the second, etc.
	 * For instance, TYPE_AT_INDEX + 2 will represent the third argument
	 */
	static final int INDEXED_TYPES = -100;
	static final int TYPE_AT_INDEX = -200;
	static final int CONTENT_TYPE_AT_INDEX = -300;
	static final int KEY_TYPE_AT_INDEX = -400;

}
