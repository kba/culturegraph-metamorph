package org.culturegraph.metamorph.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.StreamHandler;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;




/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public interface RawRecordReader {

	/**
	 * Start the parsing process. Assumes that an {@link InputStream} 
	 * and a {@link StreamHandler} were set 
	 * Otherwise an {@link IllegalStateException} is thrown.
	 * @throws IOException
	 */
	void read(final InputStream inputStream) throws IOException;

	void read(final String entry);

	/**
	 * Sets the {@link StreamReceiver} which is used to process events during the parsing.
	 * @param recordHandler
	 */
	void setStreamReceiver(final StreamReceiver recordHandler);

}