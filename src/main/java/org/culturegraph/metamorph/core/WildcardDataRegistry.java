package org.culturegraph.metamorph.core;

import java.util.List;

import org.culturegraph.metamorph.util.string.WildcardTrie;

/**
 * Implements {@link DataRegistry} with a {@link WildcardTrie}.
 * 
 * @author Markus Michael Geipel
 *
 */
final class WildcardDataRegistry implements DataRegistry {

	private final WildcardTrie<Data> trie = new WildcardTrie<Data>();
	
	@Override
	public void register(final String path, final Data data) {
		trie.put(path, data);

	}

	@Override
	public List<Data> get(final String path) {
		return trie.get(path);
	}

}
