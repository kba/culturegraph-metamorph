package org.culturegraph.metamorph.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Markus Michael Geipel
 * 
 * @param <K>
 *            key
 * @param <V>
 *            value
 */
public class ListMap<K, V> implements Map<K, List<V>>, Record {

	private String identifier;
	private final Map<K, List<V>> map;

	public ListMap() {
		super();
		map = new HashMap<K, List<V>>();
	}

	public ListMap(final Map<K, List<V>> map) {
		super();
		this.map = map;
	}

	protected final Map<K, List<V>> getMap() {
		return map;
	}

	@Override
	public final void clear() {
		map.clear();
		identifier = null;
	}

	public final List<V> removeKey(final K key) {
		return map.remove(key);
	}

	public final void clearKey(final K key) {
		final List<V> values = map.get(key);
		if (values != null) {
			values.clear();
		}
	}

	public final void clearAllKeys() {
		for (Entry<K, List<V>> entry : map.entrySet()) {
			entry.getValue().clear();
		}
	}

	@Override
	public final Set<Entry<K, List<V>>> entrySet() {
		return map.entrySet();
	}

	@Override
	public final Set<K> keySet() {
		return map.keySet();
	}

	public final void put(final K name, final V value) {

		List<V> values = map.get(name);
		if (values == null) {
			values = new ArrayList<V>();
			map.put(name, values);
		}

		values.add(value);
	}

	public final void putAll(final K name, final Collection<V> addValues) {

		List<V> values = map.get(name);
		if (values == null) {
			values = new ArrayList<V>();
			map.put(name, values);
		}

		values.addAll(addValues);
	}

	@Override
	public final List<V> get(final Object name) {
		final List<V> values = map.get(name);
		if (values == null) {
			return Collections.emptyList();
		}
		return values;
	}

	public final boolean existsKey(final K name) {
		return getFirst(name) != null;
	}

	public final V getFirst(final K name) {
		final List<V> values = map.get(name);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	@Override
	public final String toString() {
		return identifier + ": " + map.toString();
	}

	@Override
	public final void setId(final String identifier) {
		this.identifier = identifier;
	}

	@Override
	public final String getId() {
		return identifier;
	}

	@Override
	public final int size() {
		return map.size();
	}

	@Override
	public final boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public final boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	@Override
	public final boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	@Override
	public final List<V> put(final K key, final List<V> value) {
		return map.put(key, value);
	}

	@Override
	public final List<V> remove(final Object key) {
		return map.remove(key);
	}

	@Override
	public final void putAll(final Map<? extends K, ? extends List<V>> putMap) {
		map.putAll(putMap);

	}

	@Override
	public final Collection<List<V>> values() {
		return map.values();
	}
}
