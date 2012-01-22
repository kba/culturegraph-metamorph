package org.culturegraph.metamorph.core2.functions;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core2.AbstractNamedValuePipe;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;

/**
 * @author Markus Michael Geipel
 */
public abstract class AbstractFunction extends AbstractNamedValuePipe implements Function {

	private SimpleMultiMap multiMapProvider;
	private Map<String, String> localMap;

	protected final String getValue(final String mapName, final String key) {
		return multiMapProvider.getValue(mapName, key);
	}
	
	protected final String getLocalValue(final String key) {
		if(localMap!=null){
			return localMap.get(key);
		}
		return null;
	}
		
	@Override
	public final void putValue(final String key, final String value) {
		if(localMap==null){
			localMap = new HashMap<String, String>();
		}
		localMap.put(key, value);
	}
	
	@Override
	public final void setMultiMap(final SimpleMultiMap multiMapProvider) {
		this.multiMapProvider = multiMapProvider;
	}
}
