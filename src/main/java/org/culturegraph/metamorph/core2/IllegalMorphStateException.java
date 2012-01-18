package org.culturegraph.metamorph.core2;

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
