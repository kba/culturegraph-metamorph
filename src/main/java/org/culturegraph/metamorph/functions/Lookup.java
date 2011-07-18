package org.culturegraph.metamorph.functions;

import org.culturegraph.metamorph.core.KeyValueStoreAggregator;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Lookup implements Function {

	private KeyValueStoreAggregator keyValueStoreProvider;
	private String datastoreName;


	@Override
	public String process(final String key) {
			return keyValueStoreProvider.getValue(datastoreName, key);
	}
	
	public void setIn(final String datastoreName){
		this.datastoreName = datastoreName;
	}


	@Override
	public void setKeyValueStoreAggregator(final KeyValueStoreAggregator dataSourceProvider) {
		this.keyValueStoreProvider = dataSourceProvider;
	}
}
