package org.culturegraph.metamorph.core.functions;

/**
 * @author Markus Michael Geipel
 */
public final class Equals extends AbstractFilter{

	@Override
	protected boolean accept(final String value) {
		return getString().equals(value);
	}
}
