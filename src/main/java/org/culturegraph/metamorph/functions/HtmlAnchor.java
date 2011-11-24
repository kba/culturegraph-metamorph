package org.culturegraph.metamorph.functions;


/**
 * @author Markus Michael Geipel
 */
final class HtmlAnchor extends AbstractFunction {

	private String prefix = "";
	private String postfix = "";

	@Override
	public String process(final String value) {
		return "<a href=\""+prefix + value + postfix + "\">" + value + "</a>";
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(final String postfix) {
		this.postfix = postfix;
	}



}
