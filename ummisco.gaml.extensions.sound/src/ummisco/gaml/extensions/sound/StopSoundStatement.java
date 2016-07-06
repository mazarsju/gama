/*********************************************************************************************
 * 
 *
 * 'StopSoundStatement.java', in plugin 'ummisco.gaml.extensions.sound', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package ummisco.gaml.extensions.sound;

import java.util.List;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.IDescriptionValidator;
import msi.gaml.compilation.ISymbol;
import msi.gaml.descriptions.IDescription;
import msi.gaml.statements.AbstractStatementSequence;
import ummisco.gaml.extensions.sound.StopSoundStatement.StopSoundValidator;

@symbol(name = IKeyword.STOP_SOUND, kind = ISymbolKind.SEQUENCE_STATEMENT, with_sequence = true,
concept = { IConcept.SOUND })
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT })
@validator(StopSoundValidator.class)
public class StopSoundStatement extends AbstractStatementSequence {
	
	public static class StopSoundValidator implements IDescriptionValidator {

		/**
		 * Method validate()
		 * @see msi.gaml.compilation.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription cd) {
			
			// what to validate?
		}
	}
	
	
	private AbstractStatementSequence sequence = null;


	public StopSoundStatement(IDescription desc) {
		super(desc);
	}

	@Override
	public void setChildren(final List<? extends ISymbol> com) {
		sequence = new AbstractStatementSequence(description);
		sequence.setName("commands of " + getName());
		sequence.setChildren(com);
	}

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		IAgent currentAgent = scope.getAgentScope();
		
		GamaSoundPlayer soundPlayer = SoundPlayerBroker.getInstance().getSoundPlayer(currentAgent);
		soundPlayer.stop(false);

		if (sequence != null) {
			Object[] result = new Object[1];
			scope.execute(sequence, currentAgent, null, result);
		}

		
		return null;
	}
}
