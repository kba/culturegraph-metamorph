package org.culturegraph.metamorph.functions2;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
abstract class AbstractCompose extends AbstractSimpleStatelessFunction {

	private String prefix = "";
	private String postfix = "";

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	protected String getPrefix() {
		return prefix;
	}

	protected String getPostfix() {
		return postfix;
	}

	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(final String postfix) {
		this.postfix = postfix;
	}



}
