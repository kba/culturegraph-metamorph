package org.culturegraph.metamorph.functions;

import org.culturegraph.metamorph.core.KeyValueStoreAggregator;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public interface Function {
	String process(String value);

	void setKeyValueStoreAggregator(KeyValueStoreAggregator keyValueStoreProvider);
}
