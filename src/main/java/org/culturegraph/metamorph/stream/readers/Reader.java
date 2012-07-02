package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.StreamHandler;

import org.culturegraph.metamorph.stream.StreamSender;





/**
 * Adds methods 'read' and 'getId' to {@link StreamSender}
 * @author Markus Michael Geipel
 */
public interface Reader extends StreamSender{

	/**
	 * Start the parsing process. Assumes that an {@link InputStream} 
	 * and a {@link StreamHandler} were set 
	 * Otherwise an {@link IllegalStateException} is thrown.
	 * @throws IOException
	 */
	void read(final java.io.Reader reader) throws IOException;
	
	/**
	 * Reads a single record
	 * @param entry one record
	 */
	void read(final String entry);


	
	/**
	 * Extracts the id from the raw record data. Used for database ingests.
	 * @param record raw record
	 * @return id (never <code>null</code>!)
	 * @throws MissingIdException
	 */
//	String getId(String record);

}