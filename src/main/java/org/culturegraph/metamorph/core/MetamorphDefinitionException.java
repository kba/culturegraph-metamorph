package org.culturegraph.metamorph.core;

final class MetamorphDefinitionException extends RuntimeException {

	private static final long serialVersionUID = -3130648074493084946L;

	/**
	 * @param message
	 */
	MetamorphDefinitionException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	MetamorphDefinitionException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	MetamorphDefinitionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}