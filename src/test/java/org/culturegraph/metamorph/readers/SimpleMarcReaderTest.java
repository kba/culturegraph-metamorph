package org.culturegraph.metamorph.readers;

import java.io.FileInputStream;
import java.io.IOException;

import org.culturegraph.metamorph.Files;
import org.culturegraph.metamorph.streamreceiver.CountingStreamReceiver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link MarcReader}. So far only verifies that the correct number of records and fields is read.
 * @author Markus Michael Geipel
 * @see MabReader
 */
public final class SimpleMarcReaderTest {
	
	private static final int NUM_RECORDS=10;
	private static final int NUM_LITERALS=745;
	
	private final MarcReader reader = new MarcReader();
	private final CountingStreamReceiver countStreamReceiver =  new CountingStreamReceiver();

	
	
	@Test
	public void testRead() throws IOException {
		reader.setStreamReceiver(countStreamReceiver);
		reader.read(new FileInputStream(Files.TITLE_MARC));
		
		Assert.assertEquals("Number of read records is incorrect", NUM_RECORDS, countStreamReceiver.getNumRecords());
		Assert.assertEquals("Number of read literals is incorrect", NUM_LITERALS, countStreamReceiver.getNumLiterals());
	}
}
