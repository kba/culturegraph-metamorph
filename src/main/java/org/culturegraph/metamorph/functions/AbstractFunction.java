package org.culturegraph.metamorph.functions;

import java.util.Map;

import org.culturegraph.metamorph.multimap.SimpleMultiMap;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public abstract class AbstractFunction implements Function {

	private SimpleMultiMap multiMapProvider;

	protected final Map<String, String> getMap(final String mapName){
		return multiMapProvider.getMap(mapName);
	}
	
	protected final String getValue(final String mapName, final String key) {
		return multiMapProvider.getValue(mapName, key);
	}
	
	@Override
	public abstract String process(final String value);

	@Override
	public final void setMultiMapProvider(final SimpleMultiMap multiMapProvider) {
		this.multiMapProvider = multiMapProvider;
	}
}
