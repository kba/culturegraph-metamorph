package org.culturegraph.metamorph.stream;

import java.util.Collection;

/**
 * Interface for classe which collect data
 * 
 * @author Markus Michael Geipel
 *
 * @param <V> the type of data collected
 */
public interface Collector <V>{
	Collection<V> getCollection();
	void setCollection(Collection<V> collection);
}
