/*********************************************************************************************
 * 
 * 
 * 'SyntacticModelElement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.compilation;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import gnu.trove.set.hash.TLinkedHashSet;
import msi.gaml.statements.Facets;

/**
 * Class SyntacticModelElement.
 * 
 * @author drogoul
 * @since 12 avr. 2014
 * 
 */
public class SyntacticModelElement extends SyntacticComposedElement {

	final Set<URI> absoluteAlternatePaths;
	boolean urisFixed = false;

	public SyntacticModelElement(final String keyword, final Facets facets, final EObject statement,
			final Object... imports) {
		super(keyword, facets, statement);
		if (imports == null || imports.length == 0) {
			this.absoluteAlternatePaths = Collections.EMPTY_SET;
		} else {
			this.absoluteAlternatePaths = new TLinkedHashSet();
			for (final Object o : imports) {
				if (o instanceof URI)
					this.absoluteAlternatePaths.add((URI) o);
			}
		}
	}

	public Set<URI> getAbsoluteAlternatePaths() {
		return absoluteAlternatePaths;
	}

	public boolean areURIFixed() {
		return urisFixed;
	}

	public void setAbsoluteAlternatePaths(final Set<URI> uris) {
		if (uris != absoluteAlternatePaths) {
			absoluteAlternatePaths.clear();
			absoluteAlternatePaths.addAll(uris);
		}
		urisFixed = true;
	}

}
