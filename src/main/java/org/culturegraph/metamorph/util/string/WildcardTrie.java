package org.culturegraph.metamorph.util.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A simple Trie, which accepts a trailing wildcard
 * 
 * @author Markus Michael Geipel
 * 
 * @param <P>
 *            type of value stored
 */
public final class WildcardTrie<P> {
	public static final char STAR_WILDCARD = '*';
	public static final char Q_WILDCARD = '?';
	public static final String OR_STRING = "|";
	
	private static final Pattern OR_PATTERN = Pattern.compile(OR_STRING, Pattern.LITERAL);
	private final Node<P> root = new Node<P>();

	private Set<Node<P>> nodes = new HashSet<Node<P>>();
	private Set<Node<P>> nextNodes = new HashSet<Node<P>>();

	/**
	 * inserts keys into the try. Use '|' to concatenate. Use '*' (0,inf) and '?' (1,1) to express wildcards.
	 * 
	 * @param keys
	 * @param value
	 */
	public void put(final String keys, final P value) {
		if (keys.contains(OR_STRING)) {
			final String[] keysSplit = OR_PATTERN.split(keys);
			for (String string : keysSplit) {
				simpleyPut(string, value);
			}
		} else {
			simpleyPut(keys, value);
		}
	}

	private void simpleyPut(final String key, final P value) {

		final char[] keyChars = key.toCharArray();

		final int length;
		length = keyChars.length - 1;

		Node<P> node = root;
		Node<P> next;
		for (int i = 0; i < length; ++i) {
			next = node.getNext(keyChars[i]);
			if (next == null) {
				next = node.addNext(keyChars[i]);
			}
			node = next;
		}
		next = node.getNext(keyChars[length]);

		if (next == null) {
			next = node.addNext(keyChars[length]);
			next.addValue(value);
		} else {
			next.addValue(value);
		}

	}

	public List<P> get(final String key) {
		final List<P> matches = new ArrayList<P>();
		final char[] keyChars = key.toCharArray();

		nodes.add(root);

		for (int i = 0; i < keyChars.length; ++i) {
			for (Node<P> node : nodes) {
				final Node<P> next = node.getNext(keyChars[i]);
				if (next != null) {
					nextNodes.add(next);
				}
				final Node<P> question = node.getNext(Q_WILDCARD);
				if (question != null) {
					nextNodes.add(question);
				}

				final Node<P> star = node.getNext(STAR_WILDCARD);
				if (star != null) {
					nextNodes.add(star);
				}
			}
			nodes.clear();
			final Set<Node<P>> temp = nodes;
			nodes = nextNodes;
			nextNodes = temp;
		}

		for (Node<P> node : nodes) {
			final List<P> values = node.getValues();
			if (!values.isEmpty()) {
				matches.addAll(values);
			}
		}
		nodes.clear();
		nextNodes.clear();
		return matches;
	}



	/**
	 * 
	 * @param <T>
	 */
	private final class Node<T> {

		private List<T> values = Collections.emptyList();
		private final CharMap<Node<T>> links = new CharMap<Node<T>>();

	
		protected Node() {
			// nothing to do
		}

		public Node<T> addNext(final char key) {
			final Node<T> next;
			if (key == STAR_WILDCARD) {
				next = this;
			} else {
				next = new Node<T>();
			}
			links.put(key, next);
			return next;
		}

		public void addValue(final T value) {
			if (values == Collections.emptyList()) {
				values = new ArrayList<T>();
			}
			this.values.add(value);
		}

		public List<T> getValues() {
			return values;
		}

		public Node<T> getNext(final char key) {
			return links.get(key);
		}
	}
}
