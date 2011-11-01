package org.culturegraph.metamorph.functions;

import org.culturegraph.metamorph.core.KeyValueStoreAggregator;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public abstract class AbstractFunction implements Function {

	@Override
	public abstract String process(final String  value);

	@Override
	public void setKeyValueStoreAggregator(final KeyValueStoreAggregator dataSourceProvider) {
		// do nothing
	}
}
