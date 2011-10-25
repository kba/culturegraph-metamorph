package org.culturegraph.metamorph.functions;

/**
 * @author Markus Michael Geipel
 */
public final class Equals extends AbstractFunction {

	private String string;

	@Override
	public String process(final String value) {
		if(value.equals(string)){
			return value;
		}
		return null;
	}

	/**
	 * @param string
	 */
	public void setString(final String string) {
		this.string = string;
	}
}
