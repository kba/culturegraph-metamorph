package org.culturegraph.metamorph.core;

import java.util.HashMap;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
final class SimpleKeyValueStore extends HashMap<String, String> implements KeyValueStore {

	private static final long serialVersionUID = 150463997716013252L;

	private String defaultValue;

	@Override
	public String get(final String key) {
		final String value = super.get(key);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
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