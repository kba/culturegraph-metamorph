package org.culturegraph.metamorph.functions;

import java.util.Map;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public abstract class AbstractFunction implements Function {

	@Override
	public abstract String process(final String  value);

	@Override
	public void setMultiMap(final Map<String, Map<String, String>> multiMap) {
		// do nothing
	}
}
