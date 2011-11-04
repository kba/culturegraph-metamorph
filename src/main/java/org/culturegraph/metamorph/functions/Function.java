package org.culturegraph.metamorph.functions;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MultiMapProvider;

/**
 * Interface for functions used in {@link Metamorph}
 * @author Markus Michael Geipel
 */
public interface Function {
	String process(String value);

	void setMultiMapProvider(MultiMapProvider multiMapProvider);
}
