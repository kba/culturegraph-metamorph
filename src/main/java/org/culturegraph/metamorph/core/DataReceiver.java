package org.culturegraph.metamorph.core;

/**
 * 
 * @author Markus Michael Geipel
 */
interface DataReceiver {
	void data(String name, String value, int recordCount, int entityCount);
}
