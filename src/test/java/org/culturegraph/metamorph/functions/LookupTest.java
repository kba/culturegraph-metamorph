package org.culturegraph.metamorph.functions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.culturegraph.metamorph.multimap.MultiMapProvider;
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
		final SimpleMultiMapProvider multiMapProvider = new SimpleMultiMapProvider();
		final Map<String, String> map = new HashMap<String, String>();
		map.put(KEY, VALUE);
		
		multiMapProvider.putMap(MAP_NAME, map);
		
		lookup.setMultiMapProvider(multiMapProvider);
		lookup.setIn(MAP_NAME_WRONG);
		Assert.assertNull(lookup.process(KEY));
		Assert.assertNull(lookup.process(KEY_WRONG));
				
		lookup.setIn(MAP_NAME);
		Assert.assertEquals(VALUE, lookup.process(KEY));
		Assert.assertNull(lookup.process(KEY_WRONG));
		
		map.put(MultiMapProvider.DEFAULT_MAP_KEY, VALUE);
		Assert.assertEquals(VALUE, lookup.process(KEY_WRONG));
	}
	
	private static final class SimpleMultiMapProvider implements MultiMapProvider{
		private final Map<String, Map<String, String>> multiMap = new HashMap<String, Map<String,String>>();
		
		protected SimpleMultiMapProvider() {
			// nothing
		}
		
		public void putMap(final String name, final Map<String, String> map){
			multiMap.put(name, map);
		}
		
		@Override
		public Map<String, String> getMap(final String mapName) {
			final Map<String, String> map = multiMap.get(mapName);
			if(map==null){
				return Collections.emptyMap();
			}
			return map;
		}

		@Override
		public String getValue(final String mapName, final String key) {
			final Map<String, String> map = getMap(mapName);
			final String value = map.get(key);
			if (value == null) {
				return map.get(MultiMapProvider.DEFAULT_MAP_KEY);
			}
			return value;
		}
	}
}
