package org.culturegraph.metamorph.stream.receivers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.culturegraph.metamorph.stream.Collector;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.NamedValue;

/**
 * Collects {@link NamedValue}s in a {@link Set}. So there will not be duplicates.
 * @author Markus Michael Geipel
 */
public final class NamedValueSetWriter  implements StreamReceiver, Set<NamedValue>, Collector<Set<NamedValue>>{

	private Collection<Set<NamedValue>> collection;
	private Set<NamedValue> set;

	public NamedValueSetWriter() {
		super();
		set=new HashSet<NamedValue>();
		this.collection =null;
		
	}
	
	/**
	 * @param set is filled with the received results.
	 */
	public NamedValueSetWriter(final Collection<Set<NamedValue>> collection) {
		super();
		set=new HashSet<NamedValue>();
		this.collection = collection;
	}
	
	
	@Override
	public Collection<Set<NamedValue>> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(final Collection<Set<NamedValue>> collection) {
		this.collection = collection;
	}
	

	@Override
	public void startRecord(final String identifier){
		set.clear();
	}


	@Override
	public void literal(final String name, final String value) {
		set.add(new NamedValue(name, value));
	}
	
	@Override
	public String toString() {
		return set.toString();
	}
	@Override
	public int size() {
		return set.size();
	}
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}
	@Override
	public boolean contains(final Object obj) {
		return set.contains(obj);
	}
	@Override
	public Iterator<NamedValue> iterator() {
		return set.iterator();
	}
	@Override
	public Object[] toArray() {
		return set.toArray();
	}
	@Override
	public <T> T[] toArray(final T[] arr) {
		return set.toArray(arr);
	}
	@Override
	public boolean add(final NamedValue ele) {
		return set.add(ele);
	}
	@Override
	public boolean remove(final Object obj) {
		return set.remove(obj);
	}
	@Override
	public boolean containsAll(final Collection<?> col) {
		return set.containsAll(col);
	}
	@Override
	public boolean addAll(final Collection<? extends NamedValue> col) {
		return set.addAll(col);
	}
	@Override
	public boolean retainAll(final Collection<?> col) {
		return set.retainAll(col);
	}
	@Override
	public boolean removeAll(final Collection<?> col) {
		return set.removeAll(col);
	}
	@Override
	public void clear() {
		set.clear();
	}
	@Override
	public boolean equals(final Object obj) {
		return set.equals(obj);
	}
	@Override
	public int hashCode() {
		return set.hashCode();
	}

	@Override
	public void endRecord() {
		if(collection!=null){
			collection.add(set);
			set=new HashSet<NamedValue>();
		}
		
	}

	@Override
	public void startEntity(final String name) {
		// nothing
		
	}

	@Override
	public void endEntity() {
		// nothing
		
	}

}
