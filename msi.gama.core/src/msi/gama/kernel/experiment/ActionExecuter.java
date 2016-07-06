/*********************************************************************************************
 *
 *
 * 'ActionExecuter.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.kernel.experiment;

import java.util.ArrayList;
import java.util.List;

import msi.gama.runtime.IScope;
import msi.gaml.statements.IExecutable;

public class ActionExecuter {

	private static final int BEGIN = 0;
	private static final int END = 1;
	private static final int DISPOSE = 2;
	private static final int ONE_SHOT = 3;

	final List<IExecutable>[] actions = new List[4];
	protected final IScope scope;

	public ActionExecuter(final IScope scope) {
		this.scope = scope.copy("of ActionExecuter");
	}

	public void dispose() {
		executeActions(DISPOSE);
	}

	public void removeAction(final IExecutable haltAction) {
		if (actions == null) {
			return;
		}
		for (final List<IExecutable> list : actions) {
			if (list != null && list.remove(haltAction)) {
				return;
			}
		}
	}

	private IExecutable insertAction(final IExecutable action, final int type) {
		List<IExecutable> list = actions[type];
		if (list == null) {
			list = new ArrayList();
			actions[type] = list;
		}
		if (list.add(action)) {
			return action;
		}
		return null;
	}

	public IExecutable insertDisposeAction(final IExecutable action) {
		return insertAction(action, DISPOSE);
	}

	public IExecutable insertEndAction(final IExecutable action) {
		return insertAction(action, END);
	}

	public IExecutable insertOneShotAction(final IExecutable action) {
		return insertAction(action, ONE_SHOT);
	}

	public void executeEndActions() {
		if (scope.interrupted()) {
			return;
		}
		executeActions(END);
	}

	public void executeDisposeActions() {
		executeActions(DISPOSE);
	}

	public void executeOneShotActions() {
		if (scope.interrupted()) {
			return;
		}
		executeActions(ONE_SHOT);
		actions[ONE_SHOT] = null;
	}

	private void executeActions(final int type) {
		if (actions[type] == null) {
			return;
		}
		final int size = actions[type].size();
		if (size == 0)
			return;
		final IExecutable[] array = actions[type].toArray(new IExecutable[size]);
		for (final IExecutable action : array) {
			if (!scope.interrupted()) {
				action.executeOn(scope);
			}
		}
	}

	public synchronized void executeOneAction(final IExecutable action) {
		final boolean paused = scope.isPaused();
		if (paused) {
			action.executeOn(scope);
		} else {
			insertOneShotAction(action);
		}
	}

	public void executeBeginActions() {
		if (scope.interrupted()) {
			return;
		}
		executeActions(BEGIN);
	}

}
