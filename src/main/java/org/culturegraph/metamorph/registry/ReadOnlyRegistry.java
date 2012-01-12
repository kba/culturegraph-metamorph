package org.culturegraph.metamorph.registry;

/**
 * Interface for classes which provide access to instances of an arbitrary class <code>T</code> based on a {@link String} key.
 * 
 * 
 * @author Markus Michael Geipel
 *
 * @param <T> the type of the objects accessed by a {@link String} key
 */
public interface ReadOnlyRegistry<T> {
	/**
	 * @param key
	 * @return Object registered under key
	 */
	T get(String key);
}
