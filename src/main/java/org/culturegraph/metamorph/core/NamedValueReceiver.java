package org.culturegraph.metamorph.core;

/**
 * 
 * @author Markus Michael Geipel
 */
public interface NamedValueReceiver {
	void receive(String name, String value, NamedValueSource source, int recordCount, int entityCount);
}
