package org.culturegraph.metamorph.util.string;

/**
 * A simple Trie, nothing fancy at all
 * 
 * @author Markus Michael Geipel

 * @param <P> type of value stored
 */
public final class SimpleTrie<P> {
	private final Node<P> root = new Node<P>(null);
	
	public void put(final String key, final P value){
				
		Node<P> node = root;
		Node<P> next;
		final int length = key.length();
		for (int i = 0; i < length-1; ++i) {
			next = node.getNext(key.charAt(i));
			if(next==null){
				next = node.addNext(key.charAt(i));
			}
			node = next;
		}
		next = node.getNext(key.charAt(length-1));
		if(next==null){
			next = node.addNext(key.charAt(length-1), value);
		}else{
			throw new IllegalStateException("Value '" + value + "' already in trie");
		}
	}
	
	public P get(final String key){
		Node<P> node = root;
		final int length = key.length();
		for (int i = 0; i < length; ++i) {
			node = node.getNext(key.charAt(i));
			if(node==null){
				return null;
			}
		}
		return node.getValue();
	}
	
	/**
	 *
	 * @param <P>
	 */
	private static final class Node<P> {
		private final P value;
		private final CharMap<Node<P>> links = new CharMap<Node<P>>();
		
		public Node(final P value) {
			this.value = value;
		}
		
		public Node<P> addNext(final char key){
			return addNext(key, null);
		}
		
		public Node<P> addNext(final char key, final P value){
			final Node<P> next = new Node<P>(value);
			links.put(key, next);
			return next;
		}
		
		public P getValue(){
			return value;
		}
		
		public Node<P> getNext(final char key){
			return links.get(key);
		}
	}
}
