package org.culturegraph.metamorph.core;


/**
 * Thrown by {@link MetamorphBuilder} if the definition is syntactically incorrect or components for dynamic loading are not found.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class MetamorphDefinitionException extends RuntimeException {

	private static final long serialVersionUID = -3130648074493084946L;

	/**
	 * @param message
	 */
	public MetamorphDefinitionException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MetamorphDefinitionException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MetamorphDefinitionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}