package org.culturegraph.metamorph.stream.receivers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.culturegraph.metamorph.types.NamedValue;

/**
 * Collects the received results in a {@link List}. 
 * 
 * @author Markus Michael Geipel
 */
public final class NamedValueListWriter extends DefaultStreamReceiver implements List<NamedValue> {

	private final List<NamedValue> list;

	public NamedValueListWriter() {
		super();
		list = new ArrayList<NamedValue>();
	}
	

	@Override
	public void startRecord(final String identifier){
		list.clear();
	}

	@Override
	public void literal(final String name, final String value) {
		list.add(new NamedValue(name, value));
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(final Object object) {
		return list.contains(object);
	}

	@Override
	public Iterator<NamedValue> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] array) {
		return list.toArray(array);
	}

	@Override
	public boolean add(final NamedValue element) {
		return list.add(element);
	}

	@Override
	public boolean remove(final Object object) {
		return list.remove(object);
	}

	@Override
	public boolean containsAll(final Collection<?> collection) {
		return list.containsAll(collection);
	}

	@Override
	public boolean addAll(final Collection<? extends NamedValue> collection) {

		return list.addAll(collection);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends NamedValue> collection) {

		return list.addAll(index, collection);
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {

		return list.removeAll(collection);
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {
	
		return list.retainAll(collection);
	}

	@Override
	public void clear() {
		list.clear();
		
	}

	@Override
	public NamedValue get(final int index) {

		return list.get(index);
	}

	@Override
	public NamedValue set(final int index, final NamedValue element) {
		return list.set(index, element);
	}

	@Override
	public void add(final int index, final NamedValue element) {
		list.add(index, element);
		
	}

	@Override
	public NamedValue remove(final int index) {

		return list.remove(index);
	}

	@Override
	public int indexOf(final Object object) {

		return list.indexOf(object);
	}

	@Override
	public int lastIndexOf(final Object object) {

		return list.lastIndexOf(object);
	}

	@Override
	public ListIterator<NamedValue> listIterator() {

		return list.listIterator();
		
	}

	@Override
	public ListIterator<NamedValue> listIterator(final int index) {

		return list.listIterator(index);
	}

	@Override
	public List<NamedValue> subList(final int fromIndex, final int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
