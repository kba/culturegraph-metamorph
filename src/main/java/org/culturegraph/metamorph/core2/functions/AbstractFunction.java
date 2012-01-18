package org.culturegraph.metamorph.core2.functions;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.multimap.SimpleMultiMap;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public abstract class AbstractFunction implements Function {

	private SimpleMultiMap multiMapProvider;
	private Map<String, String> localMap;
	private ValueReceiver valueReceiver;



	protected final ValueReceiver getValueReceiver() {
		return valueReceiver;
	}
	

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
	}
	

	@Override
	public final void setMultiMap(final SimpleMultiMap multiMapProvider) {
		this.multiMapProvider = multiMapProvider;
	}
	
	@Override
	public final void setValueReceiver(final ValueReceiver valueReceiver) {
		this.valueReceiver = valueReceiver;
	}
}
