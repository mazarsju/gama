/*********************************************************************************************
 * 
 *
 * 'TraceStatement.java', in plugin 'msi.gama.core', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.statements;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gaml.descriptions.IDescription;

/**
 * Class TraceStatement.
 * 
 * @author drogoul
 * @since 23 févr. 2014
 * 
 */
@symbol(name = IKeyword.TRACE, kind = ISymbolKind.SEQUENCE_STATEMENT, with_sequence = true,
concept = { IConcept.DISPLAY })
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT, ISymbolKind.LAYER })
@doc(value="All the statements executed in the trace statement are displayed in the console.")
public class TraceStatement extends AbstractStatementSequence {

	/**
	 * @param desc
	 */
	public TraceStatement(final IDescription desc) {
		super(desc);
	}

	@Override
	public void enterScope(final IScope scope) {
		super.enterScope(scope);
		scope.setTrace(true);
	}

	@Override
	public void leaveScope(final IScope scope) {
		scope.setTrace(false);
		super.leaveScope(scope);
	}
}
