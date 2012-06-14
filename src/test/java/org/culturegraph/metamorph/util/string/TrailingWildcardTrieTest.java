package org.culturegraph.metamorph.util.string;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * tests {@link TrailingWildcardTrie}
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class TrailingWildcardTrieTest {

	private static final String NEW_YORK_CITY = "New York City";
	private static final String YORK = "York";
	private static final String NEW_YORK = "New York";

	@Test
	public void testAddAndMatch() {
		final TrailingWildcardTrie<String> trie = new TrailingWildcardTrie<String>();
		Assert.assertTrue(trie.get(YORK).isEmpty());
		
		trie.put(NEW_YORK_CITY, NEW_YORK_CITY);
		trie.put(YORK, YORK, true);
		trie.put(NEW_YORK, NEW_YORK, true);

		List<String> results = trie.get(NEW_YORK_CITY);
		
		Assert.assertTrue(results.contains(NEW_YORK_CITY));
		Assert.assertTrue(results.contains(NEW_YORK));
		Assert.assertFalse(results.contains(YORK));
		
		results = trie.get("York Town");
		Assert.assertTrue(results.contains(YORK));
		
		results = trie.get(YORK);
		Assert.assertTrue(results.contains(YORK));
		
		
	}

}
