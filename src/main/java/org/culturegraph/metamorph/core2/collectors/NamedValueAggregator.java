package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.core2.NamedValueReceiver;
import org.culturegraph.metamorph.core2.NamedValueSource;

/**
 * 
 * @author Markus Michael Geipel
 */
public interface NamedValueAggregator extends NamedValueReceiver{
	void addNamedValueSource(NamedValueSource namedValueSource);
}
