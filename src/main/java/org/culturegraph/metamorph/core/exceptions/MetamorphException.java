package org.culturegraph.metamorph.core.exceptions;

import org.culturegraph.metamorph.core.Metamorph;

/**
 * Thrown if an error occurs during the processing in {@link Metamorph}
 * 
 * @author Markus Michael Geipel
 */
public class MetamorphException extends RuntimeException {

	private static final long serialVersionUID = -3130648074493084946L;

	/**
	 * @param message
	 */
	public MetamorphException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MetamorphException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MetamorphException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
