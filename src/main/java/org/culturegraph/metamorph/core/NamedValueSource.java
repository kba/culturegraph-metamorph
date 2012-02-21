package org.culturegraph.metamorph.core;

/**
 * Base interface for all classes in {@link Metamorph} which emit name-value-pairs
 * 
 * @author Markus Michael Geipel
 *
 */
public interface NamedValueSource {
	<R extends NamedValueReceiver> R setNamedValueReceiver(R receiver);
}
