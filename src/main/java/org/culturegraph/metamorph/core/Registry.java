package org.culturegraph.metamorph.core;

import java.util.List;

/**
 * used to access zero to n {@link Data} instances based on a {@link String} path. Used in {@link Metamorph}
 * 
 * @author Markus Michael Geipel
 *
 * @param <T>
 */
interface Registry<T> {

	/**
	 * add an instance of {@link Data} to a path.
	 * 
	 * @param path
	 * @param data
	 */
	void register(String path, T value);

	/**
	 * @param path
	 * @return matching {@link Data} instances. Should NEVER be <code>null</code>. If no matches found, an empty {@link List} is to be returned.
	 */
	List<T> get(String path);

}
