package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.NamedValueReceiver;
import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * 
 * @author Markus Michael Geipel
 */
public interface NamedValueAggregator extends NamedValueReceiver{
	void addNamedValueSource(NamedValueSource namedValueSource);
}
