package org.culturegraph.metamorph.stream;

import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.stream.readers.PicaReader;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.receivers.CountingWriter;
import org.junit.Test;

/**
 * Tests {@link Splitter}.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class SplitterTest {

	private static final int NUM_TP_RECORDS = 3;
	private static final int NUM_TN_RECORDS = 7;

	@Test
	public void testCorrectTeeFunction() throws IOException {
		final Reader picaReader = new PicaReader();

		final Splitter splitter = new Splitter("morph/typeSplitter.xml");

		final CountingWriter countingWriterTp = new CountingWriter();
		final CountingWriter countingWriterTn = new CountingWriter();

		picaReader.setReceiver(splitter).setReceiver("Tn", countingWriterTn);

		splitter.setReceiver("Tp", countingWriterTp);

		picaReader.read(new FileReader(DataFilePath.PND_PICA));

		Assert.assertEquals(NUM_TN_RECORDS, countingWriterTn.getNumRecords());
		Assert.assertEquals(NUM_TP_RECORDS, countingWriterTp.getNumRecords());
	}
}
