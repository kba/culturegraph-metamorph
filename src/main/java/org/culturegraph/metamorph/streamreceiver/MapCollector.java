package org.culturegraph.metamorph.streamreceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class MapCollector implements StreamReceiver {

	private final Map<String, String> map = new HashMap<String, String>();

	@Override
	public void startRecord(){
		map.clear();
	}


	@Override
	public void endRecord(){/* do nothing */}


	@Override
	public void startEntity(final String name) {/* do nothing */}


	@Override
	public void endEntity(){/* do nothing */}


	@Override
	public void literal(final String name, final String value) {
		map.put(name, value);
	}


	/**
	 * @return
	 */
	public Map<String, String> getMap() {
		return map;
	}

}
