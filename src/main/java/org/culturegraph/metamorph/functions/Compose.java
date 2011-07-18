package org.culturegraph.metamorph.functions;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Compose extends AbstractFunction {

	private String prefix = "";
	private String postfix = "";

	@Override
	public String process(final String value) {
		return prefix + value + postfix;
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
