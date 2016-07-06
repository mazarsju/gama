/*********************************************************************************************
 *
 *
 * 'GamlCompilationError.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.compilation;

import org.eclipse.emf.ecore.EObject;

/**
 * The Class GamlCompilationError. Represents the errors produced by the validation/compilation of IDescription's.
 */
public class GamlCompilationError {

	protected boolean isWarning = false;
	protected boolean isInfo = false;
	protected final String message;
	protected String code;
	protected String[] data;
	protected EObject source;

	public GamlCompilationError(final String string, final String code, final EObject object, final boolean warning,
		final boolean info, final String ... data) {

		message = string;
		isWarning = warning;
		isInfo = info;
		this.code = code;
		this.data = data;
		source = object;
	}

	public String[] getData() {
		return data;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return message;
	}

	public boolean isWarning() {
		return isWarning && !isInfo;
	}

	public boolean isInfo() {
		return isInfo;
	}

	public EObject getStatement() {
		return source;
	}

	public boolean isError() {
		return !isInfo && !isWarning;
	}

	@Override
	public boolean equals(final Object other) {
		if ( this == other ) { return true; }
		if ( !(other instanceof GamlCompilationError) ) { return false; }
		GamlCompilationError error = (GamlCompilationError) other;
		return message.equals(error.message) && source == error.source;
	}

	@Override
	public int hashCode() {
		return message.hashCode() + (source == null ? 0 : source.hashCode());
	}
}
