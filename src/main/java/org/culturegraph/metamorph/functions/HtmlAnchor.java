package org.culturegraph.metamorph.functions;


/**
 * @author Markus Michael Geipel
 */
final class HtmlAnchor extends AbstractCompose {
	@Override
	public String process(final String value) {
		return "<a href=\""+getPrefix() + value + getPostfix() + "\">" + value + "</a>";
	}
}
