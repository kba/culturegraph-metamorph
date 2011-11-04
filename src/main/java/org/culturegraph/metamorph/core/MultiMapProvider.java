package org.culturegraph.metamorph.core;

import java.util.Map;

public interface MultiMapProvider {
	

	String DEFAULT_MAP_KEY = "__default";
	/**
	 * 
	 * @param mapName
	 * @return map corresponding to mapName. Never return <code>null</code>. If there is no corresponding {@link Map}, return empty one.
	 */
	Map<String, String> getMap(String mapName);
	String getValue(String mapName, String key);
}
