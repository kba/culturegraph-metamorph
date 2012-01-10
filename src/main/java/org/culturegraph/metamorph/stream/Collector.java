package org.culturegraph.metamorph.stream;

import java.util.Collection;

public interface Collector <V>{
	Collection<V> getCollection();
	void setCollection(Collection<V> collection);
}
