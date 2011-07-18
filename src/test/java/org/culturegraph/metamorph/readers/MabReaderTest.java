package org.culturegraph.metamorph.readers;

import java.io.FileInputStream;
import java.io.IOException;

import org.culturegraph.metamorph.TestFiles;
import org.culturegraph.metamorph.streamreceiver.CountingStreamReceiver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link MabReader}. So far only verifies that the correct number of records and fields is read.
 * @author Markus Michael Geipel
 * @see MabReader
 */
public final class MabReaderTest {
	
	private static final int NUM_RECORDS=10;
//	private static final int NUM_FIELDS=10;
	private static final int NUM_LITERALS=10;
	
	private final MabReader reader = new MabReader();
	private final CountingStreamReceiver countStreamReceiver =  new CountingStreamReceiver();

	
	
	@Test
	public void testRead() throws IOException {
		reader.read(new FileInputStream(TestFiles.TITLE_MAB));
		reader.setStreamReceiver(countStreamReceiver);
		Assert.assertEquals("Number of read records is incorrect", NUM_RECORDS, countStreamReceiver.getNumRecords());
		Assert.assertEquals("Number of read literals is incorrect", NUM_LITERALS, countStreamReceiver.getNumLiterals());
	}
}
