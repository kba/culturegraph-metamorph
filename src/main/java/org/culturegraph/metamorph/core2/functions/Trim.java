package org.culturegraph.metamorph.core2.functions;

/**
 * @author Markus Michael Geipel
 */
public final class Trim extends AbstractSimpleStatelessFunction {

	@Override
	public String process(final String value) {
		return value.trim();
	}
}
