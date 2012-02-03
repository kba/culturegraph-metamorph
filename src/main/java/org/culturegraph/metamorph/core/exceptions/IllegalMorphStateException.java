package org.culturegraph.metamorph.core.exceptions;

import org.culturegraph.metamorph.core.Metamorph;


/**
 * Thrown by {@link Metamorph} if the input stream is structurally invalid. For example entities remain open on record end.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class IllegalMorphStateException extends MetamorphException {

	private static final long serialVersionUID = 8738071969657414980L;

	public IllegalMorphStateException(final String message) {
		super(message);
	}

	public IllegalMorphStateException(final Throwable cause) {
		super(cause);
	}

	public IllegalMorphStateException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
