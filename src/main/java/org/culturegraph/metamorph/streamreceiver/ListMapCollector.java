package org.culturegraph.metamorph.streamreceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Collects the received results in a {@link Map}. Duplicate names are thus lost.
 * 
 * @author Markus Michael Geipel
 */
public final class ListMapCollector extends DefaultStreamReceiver {

	private final Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	
	@Override
	public void startRecord(){
		clear();
	}
	
	public void clear(){
		for(Entry<String, List<String>> entry : map.entrySet()){
			entry.getValue().clear();
		}
	}
	
	public void clearKey(final String key){
		final List<String> values = map.get(key);
		if(values!=null){
			values.clear();
		}
	}

	@Override
	public void literal(final String name, final String value) {
		
		List<String> values = map.get(name);
		if(values == null){
			values = new LinkedList<String>();
			map.put(name, values);
		}
		
		values.add(value);
	}
	
	public Set<Entry<String, List<String>>> getEntrySet(){
		return Collections.unmodifiableSet(map.entrySet());
	}

	public List<String> getValues(final String name){
		final List<String> values = map.get(name);
		if(values==null){
			return Collections.emptyList();
		}else{
			return values;
		}
	}
	
	public String getFirstValue(final String name){
		final List<String> values = map.get(name);
		if(values==null || values.size()<1){
			return null;
		}else{
			return values.get(0);
		}
	}
	
	@Override
	public String toString() {
		return map.toString();
	}

}
