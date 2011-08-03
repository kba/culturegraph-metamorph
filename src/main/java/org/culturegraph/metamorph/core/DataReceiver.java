package org.culturegraph.metamorph.core;

/**
 * 
 * @author Markus Michael Geipel
 */
interface DataReceiver {
	void data(String name, String value, DataSender sender, int recordCount, int entityCount);
}
