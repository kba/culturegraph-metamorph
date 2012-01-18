package org.culturegraph.metamorph.core;

/**
 * 
 * @author Markus Michael Geipel
 */
interface NamedValueReceiver {
	
	
	void receive(String name, String value, int recordCount, int entityCount);
}
