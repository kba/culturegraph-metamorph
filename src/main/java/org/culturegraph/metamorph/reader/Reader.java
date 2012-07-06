package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.framework.ObjectPipe;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * Adds methods 'read' and 'getId' to {@link StreamSender}
 * @author Markus Michael Geipel
 */
public interface Reader extends ObjectPipe<java.io.Reader, StreamReceiver>  {
	
	/**
	 * Reads a single record
	 * @param entry one record
	 */
	void read(final String entry);

}