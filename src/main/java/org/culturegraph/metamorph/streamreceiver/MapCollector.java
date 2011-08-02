package org.culturegraph.metamorph.streamreceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Collects the received results in a {@link Map}. Duplicate names are thus lost.
 * 
 * @author Markus Michael Geipel
 */
public final class MapCollector extends DefaultStreamReceiver {

	private final Map<String, String> map;

	public MapCollector() {
		super();
		map = new HashMap<String, String>();
	}
	
	/**
	 * @param map is filled with the received results.
	 */
	public MapCollector(final Map<String, String> map){
		super();
		this.map = map;
	}
	
	@Override
	public void startRecord(){
		map.clear();
	}

	@Override
	public void literal(final String name, final String value) {
		map.put(name, value);
	}

	/**
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}

}
