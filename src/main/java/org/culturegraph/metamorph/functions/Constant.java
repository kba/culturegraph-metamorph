package org.culturegraph.metamorph.functions;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Constant extends AbstractFunction {

	private String value;

	/**
	 * @param value the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String process(final String value) {
		return this.value;
	}



}
