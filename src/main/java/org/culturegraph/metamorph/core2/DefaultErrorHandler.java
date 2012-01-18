package org.culturegraph.metamorph.core2;

import org.culturegraph.metamorph.core2.exceptions.MetamorphException;

public final class DefaultErrorHandler implements MetamorphErrorHandler {
		@Override
		public void error(final Exception exception) {
			throw new MetamorphException("An unhandled exception occured", exception);
		}
}