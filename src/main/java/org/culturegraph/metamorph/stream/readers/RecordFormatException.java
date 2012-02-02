package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;

public final class RecordFormatException extends MetamorphException {


	private static final long serialVersionUID = -5767420416327213311L;

	public RecordFormatException(final String message) {
		super(message);
	}

	public RecordFormatException(final Throwable cause) {
		super(cause);
	}

	public RecordFormatException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
