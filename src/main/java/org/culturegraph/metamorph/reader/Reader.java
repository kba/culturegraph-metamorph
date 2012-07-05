package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.framework.ObjectReceiver;
import org.culturegraph.metastream.framework.Sender;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * Adds methods 'read' and 'getId' to {@link StreamSender}
 * @author Markus Michael Geipel
 */
public interface Reader extends Sender<StreamReceiver>, ObjectReceiver<java.io.Reader> {
	
	/**
	 * Reads a single record
	 * @param entry one record
	 */
	void read(final String entry);

}