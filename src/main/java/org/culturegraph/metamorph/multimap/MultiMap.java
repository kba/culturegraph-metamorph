package org.culturegraph.metamorph.multimap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of {@link SimpleMultiMap}
 * 
 * @author Markus Michael Geipel
 * 
 */

public class MultiMap implements SimpleMultiMap{

	private final Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
	


	@Override
	public final Map<String, String> getMap(final String mapName) {
		final Map<String, String> map = data.get(mapName);
		if (map == null) {
			return Collections.emptyMap();
		}
		return map;
	}


	@Override
	public final String getValue(final String mapName, final String key) {
		final Map<String, String> map = getMap(mapName);
		final String value = map.get(key);
		if (value == null) {
			return map.get(SimpleMultiMap.DEFAULT_MAP_KEY);
		}
		return value;
	}
	
	@Override
	public final String toString() {
		return data.toString();
	}


	@Override
	public final Map<String, String> putMap(final String mapName, final Map<String, String> map) {
		return data.put(mapName, map);
	}


	@Override
	public final String putValue(final String mapName, final String key, final String value) {
		Map<String, String> map = data.get(mapName);
		if (map == null) {
			map = new HashMap<String, String>();
			data.put(mapName, map);
		}
		return map.put(key, value);
	}
}
