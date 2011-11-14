package org.culturegraph.metamorph.stream.readers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.stream.receivers.CountingWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link MarcReader}. So far only verifies that the correct number of records and fields is read.
 * @author Markus Michael Geipel
 * @see MabReader
 */
public final class SimpleMarcReaderTest {
	
	private static final int NUM_RECORDS=10;
	private static final int NUM_LITERALS=765;
	
	private final MarcReader reader = new MarcReader();
	private final CountingWriter countStreamReceiver =  new CountingWriter();

		
	@Test
	public void testRead() throws IOException {
		reader.setStreamReceiver(countStreamReceiver);
		reader.read(new FileInputStream(DataFilePath.TITLE_MARC));
		
		Assert.assertEquals("Number of read records is incorrect", NUM_RECORDS, countStreamReceiver.getNumRecords());
		Assert.assertEquals("Number of read literals is incorrect", NUM_LITERALS, countStreamReceiver.getNumLiterals());
	}
	
	@Test
	public void testGetId() throws IOException {
		final FileInputStream inputStream = new FileInputStream(DataFilePath.TITLE_MARC);
		final BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream));
		
		String line = breader.readLine();
		while (line != null) {
			if(!line.isEmpty()){
				Assert.assertNotNull(reader.getId(line));
			}
			line = breader.readLine();
		}
		breader.close();
	}
}
