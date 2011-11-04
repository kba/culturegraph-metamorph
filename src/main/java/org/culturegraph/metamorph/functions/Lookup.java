package org.culturegraph.metamorph.functions;

import java.util.Map;

import org.culturegraph.metamorph.core.Metamorph;

/**
 * @author Markus Michael Geipel
 */
final class Lookup implements Function {

	private Map<String, Map<String, String>> multiMap;
	private String mapName;


	@Override
	public String process(final String key) {
			final Map<String, String> map = multiMap.get(mapName);
			if(map!=null){
				final String value = map.get(key);
				if(value==null){
					return map.get(Metamorph.DEFAULT_MAP_KEY);
				}
			}
			return null;
	}
	
	public void setIn(final String datastoreName){
		this.mapName = datastoreName;
	}

	@Override
	public void setMultiMap(final Map<String, Map<String, String>> multiMap) {
		this.multiMap = multiMap;
	}
}
