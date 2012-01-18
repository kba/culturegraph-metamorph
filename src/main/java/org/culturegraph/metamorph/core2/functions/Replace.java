package org.culturegraph.metamorph.core2.functions;

import java.util.regex.Pattern;


/**
 * @author Markus Michael Geipel
 */
final class Replace extends AbstractSimpleStatelessFunction {

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
