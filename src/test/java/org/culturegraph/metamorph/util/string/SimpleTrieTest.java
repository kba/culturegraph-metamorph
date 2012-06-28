package org.culturegraph.metamorph.util.string;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

/**
 * tests {@link SimpleTrie}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class SimpleTrieTest {
		
	private static final String KEY = "key";
	private static final String VALUE = "value";

	@Test
	public void testAdd() {
		final SimpleTrie<String> trie = new SimpleTrie<String>();
		Assert.assertNull(trie.get(KEY));
		trie.put(KEY, VALUE);
		Assert.assertEquals(VALUE, trie.get(KEY));
	}
	
	@Test
	public void testMultiAdd(){
		final SimpleTrie<String> trie = new SimpleTrie<String>();
		
		final String[] megacities =  { "Brisbane", "Sydney", "Melbourne", "Adelaide", "Perth", "Berlin", "Berlin Center", "Bremen", "Petersburg"};
		
		for (int i = 0; i < megacities.length; ++i) {
			final String city = megacities[i];
			trie.put(city, city.toUpperCase(Locale.US));
		}
		
		for (int i = 0; i < megacities.length; ++i) {
			final String city = megacities[i];
			Assert.assertEquals(city.toUpperCase(Locale.US), trie.get(city));
		}
	}
}
