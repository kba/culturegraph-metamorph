package org.culturegraph.metamorph.functions2;

/**
 * @author Markus Michael Geipel
 */
final class Equals extends AbstractSimpleStatelessFunction {

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
