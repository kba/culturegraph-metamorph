package org.culturegraph.metamorph.multimap;

import java.util.Collection;
import java.util.Map;

/**
 * Simple value store with following a  Map&lt;String, Map&lt;String, String&gt;&gt; model. Just keeping the interface simpler. 
 * 
 * @author Markus Michael Geipel
 *
 */
public interface SimpleMultiMap {
	

	String DEFAULT_MAP_KEY = "__default";
	
	/**
	 * 
	 * @param mapName
	 * @return map corresponding to mapName. Never return <code>null</code>. If there is no corresponding {@link Map}, return empty one.
	 */
	Collection<String> getMapNames();
	Map<String, String> getMap(String mapName);
	String getValue(String mapName, String key);
	
	Map<String, String> putMap(String mapName, Map<String, String> map);
	String putValue(String mapName, String key, String value);

}
