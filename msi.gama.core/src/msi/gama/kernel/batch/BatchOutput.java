/*********************************************************************************************
 * 
 *
 * 'BatchOutput.java', in plugin 'msi.gama.core', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.kernel.batch;

import java.util.List;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.*;
import msi.gaml.compilation.*;
import msi.gaml.descriptions.IDescription;
import msi.gaml.types.IType;

@symbol(name = IKeyword.SAVE_BATCH, kind = ISymbolKind.BATCH_METHOD, with_sequence = false, concept = {IConcept.BATCH})
@inside(kinds = { ISymbolKind.EXPERIMENT })
@facets(value = { @facet(name = IKeyword.TO, type = IType.LABEL, optional = false),
	@facet(name = IKeyword.REWRITE, type = IType.BOOL, optional = true),
	@facet(name = IKeyword.DATA, type = IType.NONE, optional = true) }, omissible = IKeyword.DATA)
public class BatchOutput extends Symbol {

	// A placeholder for a file output
	// TODO To be replaced by a proper "save" command, now that it accepts
	// new file types.

	public BatchOutput(/* final ISymbol context, */final IDescription desc) {
		super(desc);
	}

	@Override
	public void setChildren(final List<? extends ISymbol> commands) {}

}