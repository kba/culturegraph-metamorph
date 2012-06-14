package org.culturegraph.metamorph.core.functions;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.AbstractNamedValuePipeHead;
import org.culturegraph.metamorph.core.EntityEndIndicator;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;

/**
 * @author Markus Michael Geipel
 */
public abstract class AbstractFunction extends AbstractNamedValuePipeHead implements Function {

	private SimpleMultiMap multiMap;
	private Map<String, String> localMap;
	private String mapName;

	protected final String getValue(final String mapName, final String key) {
		return multiMap.getValue(mapName, key);
	}

	protected final String getLocalValue(final String key) {
		if (localMap != null) {
			return localMap.get(key);
		}
		return null;
	}

	protected final String getMapName() {
		return mapName;
	}

	protected final Map<String, String> getMap() {
		if (localMap == null) {
			return multiMap.getMap(mapName);
		}
		return localMap;
	}

	protected final void setMap(final String mapName) {
		this.mapName = mapName;
	}

	@Override
	public final void putValue(final String key, final String value) {
		if (localMap == null) {
			localMap = new HashMap<String, String>();
		}
		localMap.put(key, value);
	}

	@Override
	public final void setMultiMap(final SimpleMultiMap multiMapProvider) {
		this.multiMap = multiMapProvider;
	}

	@Override
	public void setEntityEndIndicator(final EntityEndIndicator indicator) {
		// nothing by default
	}
}
