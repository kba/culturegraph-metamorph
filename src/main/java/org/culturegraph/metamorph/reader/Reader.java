package org.culturegraph.metamorph.reader;

import java.io.InputStream;
import java.util.logging.StreamHandler;

import org.culturegraph.metastream.framework.Sender;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * Adds methods 'read' and 'getId' to {@link StreamSender}
 * @author Markus Michael Geipel
 */
public interface Reader extends Sender<StreamReceiver> {

	/**
	 * Start the parsing process. Assumes that an {@link InputStream} 
	 * and a {@link StreamHandler} were set 
	 * Otherwise an {@link IllegalStateException} is thrown.
	 * @throws IOException
	 */
	void read(final java.io.Reader reader);
	
	/**
	 * Reads a single record
	 * @param entry one record
	 */
	void read(final String entry);

}