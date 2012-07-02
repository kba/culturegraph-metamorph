package org.culturegraph.metamorph.util.string;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

/**
 * A {@link Map} with byte as key. Used for set matching, tries etc. <br>
 * <strong>Important:</strong> It is optimized for size in memory. No extra information for fast entry/keySet/values iteration etc. is held.
 * 
 * @author Markus Michael Geipel
 * 
 * @param <V>
 */
final class ByteMap<V> implements Map<Byte, V> {

	private static final int INITIAL_CAPACITY = 1;
	private Entry<V>[] table;

	private int size;

	public ByteMap() {
		table = new Entry[INITIAL_CAPACITY];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(final Object key) {
		throw new NotImplementedException();
	}

	@Override
	public boolean containsValue(final Object value) {
		throw new NotImplementedException();
	}

	@Override
	public V get(final Object key) {
		if (key instanceof Byte) {
			final Byte beite = (Byte) key;
			return get(beite.byteValue());
		}
		return null;
	}

	public V get(final byte key) {
		
		Entry<V> entry = table[(key-Byte.MIN_VALUE) % table.length];
		while (entry != null) {
			if (entry.getKeyByte() == key) {
				return entry.getValue();
			}
			entry = entry.getNext();
		}
		return null;
	}

	@Override
	public V put(final Byte key, final V value) {
		put(key.byteValue(), value);
		return null;
	}

	/**
	 * @param key
	 * @param value
	 * @return the parameter value
	 */
	public void put(final byte key, final V value) {
		if (size == table.length) {
			expand();
		}
		put(table, key, value);
		++size;
	}

	public void put(final Entry<V>[] table, final byte key, final V value) {
		final Entry<V> newEntry = new Entry<V>(key, value);

		Entry<V> entry = table[(key-Byte.MIN_VALUE) % table.length];

		if (entry == null) {
			table[(key-Byte.MIN_VALUE) % table.length] = newEntry;
		} else {
			while (entry.getNext() != null) {
				if (entry.getKeyByte() == key) {
					throw new IllegalStateException("Key '" + key + "' already used");
				}
				entry = entry.getNext();
			}
			entry.setNext(newEntry);
		}
	}

	private void expand() {
		final int newSize = 2 * table.length;
		final Entry<V>[] newTable = new Entry[newSize];

		for (Entry<V> entry : table) {
			Entry<V> temp = entry;
			while (temp != null) {
				put(newTable, temp.getKeyByte(), temp.getValue());
				temp = temp.getNext();
			}
		}

		table = newTable;
	}

	@Override
	public V remove(final Object key) {
		throw new NotImplementedException();
	}

	@Override
	public void putAll(final Map<? extends Byte, ? extends V> map) {
		throw new NotImplementedException();
	}

	@Override
	public void clear() {
		throw new NotImplementedException();
	}

	@Override
	public Set<Byte> keySet() {
		final Set<Byte> keys = new HashSet<Byte>();
		for (int i = 0; i < table.length; ++i) {
			Entry<V> entry = table[i];
			while (entry != null) {
				keys.add(entry.getKey());
				entry = entry.getNext();
			}
		}
		return keys;
	}

	@Override
	public Collection<V> values() {
		final Set<V> values = new HashSet<V>();
		for (int i = 0; i < table.length; ++i) {
			Entry<V> entry = table[i];
			while (entry != null) {
				values.add(entry.getValue());
				entry = entry.getNext();
			}
		}
		return values;
	}

	@Override
	public Set<java.util.Map.Entry<Byte, V>> entrySet() {
		final Set<java.util.Map.Entry<Byte, V>>  entries = new HashSet<java.util.Map.Entry<Byte, V>> ();
		for (int i = 0; i < table.length; ++i) {
			Entry<V> entry = table[i];
			while (entry != null) {
				entries.add(entry);
				entry = entry.getNext();
			}
		}
		return entries;
	}

	/**
	 * 
	 * @param <V>
	 */
	private static class Entry<V> implements Map.Entry<Byte, V> {
		private final byte key;
		private V value;
		private Entry<V> next;

		Entry(final byte key, final V value) {
			this.key = key;
			this.value = value;
		}

		public Entry<V> getNext() {
			return next;
		}

		public byte getKeyByte() {
			return key;
		}

		public void setNext(final Entry<V> next) {
			this.next = next;
		}

		@Override
		public Byte getKey() {
			return Byte.valueOf(key);
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(final V value) {
			final V old = this.value;
			this.value = value;
			return old;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}
	}
}
