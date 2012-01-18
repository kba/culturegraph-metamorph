package org.culturegraph.metamorph.core2.functions;


/**
 * @author Markus Michael Geipel
 */
public final class Compose extends AbstractCompose{



	@Override
	public String process(final String value) {
		return getPrefix() + value + getPostfix();
	}



}
