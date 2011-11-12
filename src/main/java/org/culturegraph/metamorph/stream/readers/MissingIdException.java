package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metamorph.core.MetamorphException;

public final class MissingIdException extends MetamorphException {

	private static final long serialVersionUID = 2048460214057525724L;

	public MissingIdException(final String message) {
		super(message);
	}

	public MissingIdException(final Throwable cause) {
		super(cause);
	}

	public MissingIdException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
