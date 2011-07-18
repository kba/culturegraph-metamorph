package org.culturegraph.metamorph.functions;

import junit.framework.Assert;

import org.culturegraph.metamorph.core.KeyValueStoreAggregator;
import org.junit.Test;



/**
 * @author Markus Michael Geipel
 * @status Prototype
 */

public final class LookupTest {
	
	
	private static final String KEY = "adfsf"; 
	private static final String KEY_WRONG = "asdff"; 
	private static final String VALUE = "ghjfgjgj"; 
	
	@Test
	public void testLookup() {
		final Lookup lookup = new Lookup();
		lookup.setKeyValueStoreAggregator(new KeyValueStoreAggregator() {
			@Override
			public String getValue(final String source, final String key) {
				if(KEY.equals(key)){
					return VALUE;
				}else{
					return null;
				}
			}
		});
		Assert.assertEquals(VALUE, lookup.process(KEY));
		Assert.assertNull(lookup.process(KEY_WRONG));
	}
	

}
