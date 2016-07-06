/*********************************************************************************************
 *
 *
 * 'SyntacticSingleElement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.compilation;

import org.eclipse.emf.ecore.EObject;
import msi.gaml.statements.Facets;

/**
 * The class SyntacticSingleElement.
 *
 * @author drogoul
 * @since 5 f�vr. 2012
 * @modified 9 sept. 2013
 *
 */
public class SyntacticSingleElement extends AbstractSyntacticElement {

	final EObject element;

	SyntacticSingleElement(final String keyword, final Facets facets, final EObject statement) {
		super(keyword, facets);
		this.element = statement;
	}

	@Override
	public EObject getElement() {
		return element;
	}

}
