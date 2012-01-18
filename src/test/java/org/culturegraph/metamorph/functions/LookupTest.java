package org.culturegraph.metamorph.functions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.culturegraph.metamorph.multimap.MultiMap;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.junit.Test;



/**
 * tests {@link Lookup}
 * 
 * @author Markus Michael Geipel
 */

public final class LookupTest {
	
	private static final String MAP_NAME = "Authors"; 
	private static final String MAP_NAME_WRONG = "Directors"; 
	private static final String KEY = "Franz"; 
	private static final String KEY_WRONG = "Josef"; 
	private static final String VALUE = "Kafka"; 
	
	@Test
	public void testLookup() {
		final Lookup lookup = new Lookup();
		final SimpleMultiMap multiMapProvider = new MultiMap();
		final Map<String, String> map = new HashMap<String, String>();
		map.put(KEY, VALUE);
		
		multiMapProvider.putMap(MAP_NAME, map);
		
		lookup.setMultiMap(multiMapProvider);
		lookup.setIn(MAP_NAME_WRONG);
		Assert.assertNull(lookup.process(KEY));
		Assert.assertNull(lookup.process(KEY_WRONG));
				
		lookup.setIn(MAP_NAME);
		Assert.assertEquals(VALUE, lookup.process(KEY));
		Assert.assertNull(lookup.process(KEY_WRONG));
		
		map.put(SimpleMultiMap.DEFAULT_MAP_KEY, VALUE);
		Assert.assertEquals(VALUE, lookup.process(KEY_WRONG));
	}
	
	
}
