package org.culturegraph.metamorph.readers;

import org.culturegraph.metamorph.core.MetamorphException;

public class IllegalEncodingException extends MetamorphException {

	private static final long serialVersionUID = 8892778770795248635L;

	public IllegalEncodingException(final String message) {
		super(message);
	}

	public IllegalEncodingException(final Throwable cause) {
		super(cause);
	}

	public IllegalEncodingException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
