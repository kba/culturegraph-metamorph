package org.culturegraph.metamorph.functions;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;

/**
 * Interface for functions used in {@link Metamorph}
 * @author Markus Michael Geipel
 */
public interface Function {
	String process(String value);

	void setMultiMapProvider(SimpleMultiMap multiMapProvider);
}
