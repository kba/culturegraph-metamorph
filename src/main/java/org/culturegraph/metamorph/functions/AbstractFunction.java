package org.culturegraph.metamorph.functions;

import org.culturegraph.metamorph.core.KeyValueStoreAggregator;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
abstract class AbstractFunction implements Function {

	public abstract String process(final String  value);

	@Override
	public void setKeyValueStoreAggregator(final KeyValueStoreAggregator dataSourceProvider) {
		// do nothing
	}
}
