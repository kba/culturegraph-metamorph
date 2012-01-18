package org.culturegraph.metamorph.functions2;


/**
 * @author Markus Michael Geipel
 */
final class Compose extends AbstractCompose{



	@Override
	public String process(final String value) {
		return getPrefix() + value + getPostfix();
	}



}
