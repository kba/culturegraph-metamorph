package org.culturegraph.metamorph.core2.functions;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core2.NamedValuePipe;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;

/**
 * Interface for functions used in {@link Metamorph}
 * @author Markus Michael Geipel
 */
public interface Function extends NamedValuePipe{

	void putValue(String key, String value);
	void setMultiMap(SimpleMultiMap multiMapProvider);
}
