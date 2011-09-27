package org.culturegraph.metamorph.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author Markus Michael Geipel
 */
public final class ListMap<K,V>{

	private final Map<K, List<V>> map = new HashMap<K, List<V>>();
	
	
	public void clear(){
		map.clear();
	}
	
	public void removeKey(final K key){
		map.remove(key);
	}
	
	public void clearKey(final K key){
		final List<V> values = map.get(key);
		if(values!=null){
			values.clear();
		}
	}

	public void clearAllKeys(){
		for(Entry<K, List<V>> entry : map.entrySet()){
			entry.getValue().clear();
		}
	}
	
	public Set<Entry<K, List<V>>> entrySet(){
		return map.entrySet();
	}


	public void put(final K name, final V value) {
		
		List<V> values = map.get(name);
		if(values == null){
			values = new ArrayList<V>();
			map.put(name, values);
		}
		
		values.add(value);
	}

	public List<V> get(final K name){
		final List<V> values = map.get(name);
		if(values==null){
			return Collections.emptyList();
		}
		return values;
	}
	
	public boolean existsKey(final K name){
		return getFirst(name)!=null;
	}
	
	public V getFirst(final K name){
		final List<V> values = map.get(name);
		if(values==null || values.isEmpty()){
			return null;
		}
		return values.get(0);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
