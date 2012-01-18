package org.culturegraph.metamorph.core2.functions;

/**
 * @author Markus Michael Geipel
 */
public final class Substring extends AbstractSimpleStatelessFunction {

	private int start;
	private int end;

	@Override
	public String process(final String value) {
		final int length = value.length();

		if (start > length - 1 || end > length) {
			return null;
		}
		final int adjEnd;
		if (end == 0) {
			adjEnd = length;
		} else {
			adjEnd = end;
		}

		return value.substring(start, adjEnd);
	}

	/**
	 * @param start
	 *            start of substring
	 */
	public void setStart(final String start) {
		this.start = Integer.parseInt(start);
	}

	/**
	 * @param end end of substring, if end==0 the the complete remaining string is returned
	 *           
	 */
	public void setEnd(final String end) {
		this.end = Integer.parseInt(end);
	}

}
