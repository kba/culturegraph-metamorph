package org.culturegraph.metamorph.core2.exceptions;

/**
 * @author Markus Michael Geipel
 * @status Experimental
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
