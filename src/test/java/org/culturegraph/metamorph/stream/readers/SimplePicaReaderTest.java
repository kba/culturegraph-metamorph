package org.culturegraph.metamorph.stream.readers;

import java.io.FileReader;
import java.io.IOException;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.stream.receivers.CountingWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link PicaReader}. So far only verifies that the correct number of
 * records and fields is read.
 * 
 * @author Markus Michael Geipel
 * @see PicaReader
 */
public final class SimplePicaReaderTest {

	private static final int NUM_RECORDS = 11;
	private static final int NUM_LITERALS = 289;

	private final Reader reader = new PicaReader();
	private final CountingWriter countStreamReceiver = new CountingWriter();

	@Test(expected=MissingIdException.class)
	public void testCorruptRead() throws IOException {
		reader.setReceiver(countStreamReceiver);
		reader.read(new FileReader(DataFilePath.PND_PICA));
		reader.read("!THIS IS A CORRUPT RECORD!");
	}
	
	@Test
	public void testRead() throws IOException {
		reader.setReceiver(countStreamReceiver);
		reader.read(new FileReader(DataFilePath.PND_PICA));

		/*
		 * record contains empty fields (should be skipped)
		 */
		reader.read("\u001e\u001e003@ \u001f012235" + "\u001e\u001e");
		
		Assert.assertEquals("Number of records is incorrect", NUM_RECORDS,
				countStreamReceiver.getNumRecords());
		Assert.assertEquals("Number of literals is incorrect", NUM_LITERALS,
				countStreamReceiver.getNumLiterals());

	}
}
