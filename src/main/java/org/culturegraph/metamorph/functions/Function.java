package org.culturegraph.metamorph.functions;

import java.util.Map;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public interface Function {
	String process(String value);

	void setMultiMap(Map<String, Map<String, String>> multiMap);
}
