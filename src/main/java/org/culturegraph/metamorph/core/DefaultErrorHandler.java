package org.culturegraph.metamorph.core;


/**
 * Default error handler used by {@link Metamorph}. Just repackages exceptions as {@link MetamorphException}s.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class DefaultErrorHandler implements MetamorphErrorHandler {
		@Override
		public void error(final Exception exception) {
			throw new MetamorphException("An unhandled exception occured", exception);
		}
}