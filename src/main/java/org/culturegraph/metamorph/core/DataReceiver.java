package org.culturegraph.metamorph.core;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
interface DataReceiver {
	void data(Literal literal, DataSender sender, int recordCount, int entityCount);
}
