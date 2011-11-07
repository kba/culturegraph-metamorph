package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
final class SimpleKeyValueStore implements KeyValueStore {

	private static final long serialVersionUID = 150463997716013252L;

	private final Map<String, String> map;
	private String defaultValue;

	public SimpleKeyValueStore() {
		map = new HashMap<String, String>();
	}
	
	public void put(final String key, final String value){
		map.put(key, value);
	}
	
	@Override
	public String get(final String key) {
		final String value = map.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
		

	/**
	 * sets the default to return when a key was not found. Can take
	 * <code>null</code>.
	 * 
	 * @param value
	 */
	public void setDefaultValue(final String value) {
		defaultValue = value;
	}
}