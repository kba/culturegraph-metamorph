package org.culturegraph.metamorph.util.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple Trie, which accepts a trailing wildcard
 * 
 * @author Markus Michael Geipel
 * 
 * @param <P>
 *            type of value stored
 */
public final class TrailingWildcardTrie<P> {
	private final Node<List<P>> root = new Node<List<P>>(null);

	public void put(final String key, final P value) {
		put(key, value, false);
	}
	
	public void put(final String key, final P value, final boolean asPrefix) {
		final char[] keyChars = key.toCharArray();

		Node<List<P>> node = root;
		Node<List<P>> next;
		for (int i = 0; i < keyChars.length - 1; ++i) {
			next = node.getNext(keyChars[i]);
			if (next == null) {
				next = node.addNext(keyChars[i]);
			}
			node = next;
		}
		next = node.getNext(keyChars[keyChars.length - 1]);
			
		if (next == null) {
			next = node.addNext(keyChars[keyChars.length - 1], new ArrayList<P>());
			next.getValue().add(value);
		} else {
			if(next.getValue()==null){
				next.setValue(new ArrayList<P>());
			}
			next.getValue().add(value);
		}
		
		next.setPrefixMatch(asPrefix);
	}

	public List<P> get(final String key) {
		List<P> matches = Collections.emptyList();
		final char[] keyBytes = key.toCharArray();
		Node<List<P>> node = root;
		
		for (int i = 0; i < keyBytes.length; ++i) {
			node = node.getNext(keyBytes[i]);
			if (node == null) {
				break;
			}else if(node.isPrefixMatch()){
				if(matches==Collections.emptyList()){
					matches = new ArrayList<P>();
				}
				matches.addAll(node.getValue());
			}
		}
		if(node!=null && node.getValue()!=null){
			if(matches==Collections.emptyList()){
				matches = new ArrayList<P>();
			}
			matches.addAll(node.getValue());
		}
		
		return matches;
	}

	/**
	 * 
	 * @param <T>
	 */
	private static final class Node<T> {
		private T value;
		private boolean prefixMatch;
		private final CharMap<Node<T>> links = new CharMap<Node<T>>();

		public Node(final T value) {
			this.value = value;
		}

		public void setPrefixMatch(final boolean isPrefixMatch) {
			this.prefixMatch = isPrefixMatch;
		}
		
		public boolean isPrefixMatch() {
			return prefixMatch;
		}
		
		public Node<T> addNext(final char key) {
			return addNext(key, null);
		}

		public Node<T> addNext(final char key, final T value) {
			final Node<T> next = new Node<T>(value);
			links.put(key, next);
			return next;
		}

		public void setValue(final T value){
			this.value = value;
		}
		
		public T getValue() {
			return value;
		}

		public Node<T> getNext(final char key) {
			return links.get(key);
		}
	}
}
