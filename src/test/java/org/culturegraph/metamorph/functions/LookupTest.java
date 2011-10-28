package org.culturegraph.metamorph.functions;

import junit.framework.Assert;

import org.culturegraph.metamorph.core.KeyValueStoreAggregator;
import org.junit.Test;



/**
 * tests {@link Lookup}
 * 
 * @author Markus Michael Geipel
 */

public final class LookupTest {
	
	
	private static final String KEY = "Franz"; 
	private static final String KEY_WRONG = "Josef"; 
	private static final String VALUE = "Kafka"; 
	
	@Test
	public void testLookup() {
		final Lookup lookup = new Lookup();
		lookup.setKeyValueStoreAggregator(new KeyValueStoreAggregator() {
			@Override
			public String getValue(final String source, final String key) {
				if(KEY.equals(key)){
					return VALUE;
				}
				return null;
			}
		});
		Assert.assertEquals(VALUE, lookup.process(KEY));
		Assert.assertNull(lookup.process(KEY_WRONG));
	}
	

}
