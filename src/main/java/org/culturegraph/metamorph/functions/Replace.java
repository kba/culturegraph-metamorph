package org.culturegraph.metamorph.functions;

import java.util.regex.Pattern;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
final class Replace extends AbstractFunction {

	private Pattern pattern;
	private String with;
	
	@Override
	public String process(final String value) {
		return pattern.matcher(value).replaceAll(with);
	}

	/**
	 * @param string the string to set
	 */
	public void setPattern(final String string) {
		this.pattern = Pattern.compile(string);
	}

	/**
	 * @param with the with to set
	 */
	public void setWith(final String with) {
		this.with = with;
	}

}
