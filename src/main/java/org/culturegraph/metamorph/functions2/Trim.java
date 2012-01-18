package org.culturegraph.metamorph.functions2;

/**
 * @author Markus Michael Geipel
 */
final class Trim extends AbstractSimpleStatelessFunction {

	@Override
	public String process(final String value) {
		return value.trim();
	}
}
