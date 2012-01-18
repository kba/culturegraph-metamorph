package org.culturegraph.metamorph.core2;

public final class DefaultErrorHandler implements MetamorphErrorHandler {
		@Override
		public void error(final Exception exception) {
			throw new MetamorphException("An unhandled exception occured", exception);
		}
}