package org.culturegraph.metamorph.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.StreamHandler;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;




/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public interface RawRecordReader extends StreamSender{

	/**
	 * Start the parsing process. Assumes that an {@link InputStream} 
	 * and a {@link StreamHandler} were set 
	 * Otherwise an {@link IllegalStateException} is thrown.
	 * @throws IOException
	 */
	void read(final InputStream inputStream) throws IOException;

	void read(final String entry);


	
	/**
	 * Extracts the id from the raw record data. Used for database ingests.
	 * @param record raw record
	 * @return
	 */
	String getId(String record);

}