package org.culturegraph.metamorph.core;

import java.util.List;

import org.culturegraph.metamorph.util.string.WildcardTrie;

/**
 * Implements {@link Registry} with a {@link WildcardTrie}.
 * 
 * @author Markus Michael Geipel
 * @param <T>
 */
final class WildcardRegistry<T> implements Registry<T> {

	private final WildcardTrie<T> trie = new WildcardTrie<T>();
	
	@Override
	public void register(final String path, final T value) {
		trie.put(path, value);

	}

	@Override
	public List<T> get(final String path) {
		return trie.get(path);
	}

}
