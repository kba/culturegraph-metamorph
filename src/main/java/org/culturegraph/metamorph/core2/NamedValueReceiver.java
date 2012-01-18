package org.culturegraph.metamorph.core2;

/**
 * 
 * @author Markus Michael Geipel
 */
public interface NamedValueReceiver {
	
	
	void receive(String name, String value, int recordCount, int entityCount);
}
