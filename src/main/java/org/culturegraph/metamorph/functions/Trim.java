package org.culturegraph.metamorph.functions;

/**
 * @author Markus Michael Geipel
 */
final class Trim extends AbstractFunction {

	@Override
	public String process(final String value) {
		return value.trim();
	}
}
