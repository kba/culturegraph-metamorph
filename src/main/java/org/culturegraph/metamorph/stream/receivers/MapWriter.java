package org.culturegraph.metamorph.stream.receivers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Collects the received results in a {@link Map}. Duplicate names are thus lost.
 * 
 * @author Markus Michael Geipel
 */
public final class MapWriter extends DefaultStreamReceiver implements Map<String, String> {

	private final Map<String, String> map;

	public MapWriter() {
		super();
		map = new HashMap<String, String>();
	}
	
	/**
	 * @param map is filled with the received results.
	 */
	public MapWriter(final Map<String, String> map){
		super();
		this.map = map;
	}
	
	@Override
	public void startRecord(final String identifier){
		map.clear();
	}

	@Override
	public void literal(final String name, final String value) {
		map.put(name, value);
	}

	@Override
	public int size() {
		return map.size();
	}
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}
	@Override
	public boolean containsKey(final Object key) {
		return map.containsKey(key);
	}
	@Override
	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}
	@Override
	public String get(final Object key) {
		return map.get(key);
	}
	@Override
	public String put(final String key, final String value) {
		return map.put(key, value);
	}
	@Override
	public String remove(final Object key) {
		return map.remove(key);
	}
	@Override
	public void putAll(final Map<? extends String, ? extends String> otherMap) {
		map.putAll(otherMap);
	}
	@Override
	public void clear() {
		map.clear();
	}
	@Override
	public Set<String> keySet() {
		return map.keySet();
	}
	@Override
	public Collection<String> values() {
		return map.values();
	}
	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return map.entrySet();
	}
	@Override
	public boolean equals(final Object obj) {
		return map.equals(obj);
	}
	@Override
	public int hashCode() {
		return map.hashCode();
	}

}
