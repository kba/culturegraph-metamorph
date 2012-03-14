package org.culturegraph.metamorph.core.exceptions;

/**
 * Throw if there is a significant mismatch between reality and the programmers weltbild
 * 
 * @author Markus Michael Geipel
 *
 */
public class ShouldNeverHappenException extends RuntimeException {

	private static final long serialVersionUID = -469007913093544145L;

	public ShouldNeverHappenException(final String message) {
		super(message);
	}

	public ShouldNeverHappenException(final Throwable cause) {
		super(cause);
	}

	public ShouldNeverHappenException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
