package org.culturegraph.metamorph.stream.receivers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.stream.Collector;
import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Collects the received results in a {@link Map}. Duplicate names are thus lost.
 * 
 * @author Markus Michael Geipel
 */
public final class MapWriter implements StreamReceiver, Map<String, String> , Collector<Map<String, String>>{
	
	private Collection<Map<String, String>> collection;
	
	private Map<String, String> map;

	public MapWriter() {
		super();
		map = new HashMap<String, String>();
		collection=null;
	}
	
	public MapWriter(final Collection<Map<String, String>> collection) {
		super();
		map = new HashMap<String, String>();
		this.collection=collection;
	}
	
	/**
	 * @param map is filled with the received results.
	 */
	public MapWriter(final Map<String, String> map){
		super();
		this.map = map;
	}
	
	@Override
	public Collection<Map<String, String>> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(final Collection<Map<String, String>> collection) {
		this.collection = collection;
	}
	
	protected void setMap(final Map<String, String> map) {
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

	@Override
	public void endRecord() {
		if(collection!=null){
			collection.add(map);
			map=new HashMap<String, String>();
		}
		
	}

	@Override
	public void startEntity(final String name) {
		// do nothing
		
	}

	@Override
	public void endEntity() {
		// do nothing
	}

}
