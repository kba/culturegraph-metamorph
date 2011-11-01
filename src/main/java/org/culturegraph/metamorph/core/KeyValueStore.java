package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.functions.Function;


/**
 * Interface used by {@link Function}, {@link Lookup} in particular.
 * @see Lookup
 * @author Markus Michael Geipel
 */
public interface KeyValueStore {
	String get(String key);
}
