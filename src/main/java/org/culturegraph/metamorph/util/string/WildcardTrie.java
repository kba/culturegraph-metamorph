package org.culturegraph.metamorph.util.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
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
	// private Queue<Node<List<P>>> nodes = new LinkedList<Node<List<P>>>();
	// private Queue<Node<List<P>>> nextNodes = new LinkedList<Node<List<P>>>();

	private Set<Node<P>> nodes = new HashSet<Node<P>>();
	private Set<Node<P>> nextNodes = new HashSet<Node<P>>();

	public void put(final String key, final P value) {
		if (key.contains(OR_STRING)) {
			final String[] keys = OR_PATTERN.split(key);
			for (String string : keys) {
				simpleyPut(string, value);
			}
		} else {
			simpleyPut(key, value);
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

	// public List<P> get(final String key) {
	// final List<P> matches = new ArrayList<P>();
	// final char[] keyChars = key.toCharArray();
	// nodes.add(root);
	// while (!nodes.isEmpty()) {
	// final Node<List<P>> node = nodes.poll();
	//
	// if (node.getDepth() == keyChars.length) {
	// if (node.getValue() != null) {
	// matches.addAll(node.getValue());
	// }
	// } else {
	// final Node<List<P>> next = node.getNext(keyChars[node.getDepth()]);
	// if (next != null) {
	// nodes.add(next);
	// }
	// final Node<List<P>> question = node.getNext(Q_WILDCARD);
	// if (question != null) {
	// nodes.add(question);
	// }
	// }
	// }
	// return matches;
	// }

	/**
	 * 
	 * @param <T>
	 */
	private final class Node<T> {

		private List<T> values = Collections.emptyList();
		private final CharMap<Node<T>> links = new CharMap<Node<T>>();

		// rivate final int depth;

		// public Node(final T value) {
		// values.add(value);
		// }
		//
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
