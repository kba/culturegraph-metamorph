package org.culturegraph.metamorph.pipe;

import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.pipe.Splitter;
import org.culturegraph.metastream.reader.PicaReader;
import org.culturegraph.metastream.reader.Reader;
import org.culturegraph.metastream.sink.Counter;
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

		final Counter countingWriterTp = new Counter();
		final Counter countingWriterTn = new Counter();

		picaReader.setReceiver(splitter).setReceiver("Tn", countingWriterTn);

		splitter.setReceiver("Tp", countingWriterTp);

		picaReader.process(new FileReader(DataFilePath.PND_PICA));

		Assert.assertEquals(NUM_TN_RECORDS, countingWriterTn.getNumRecords());
		Assert.assertEquals(NUM_TP_RECORDS, countingWriterTp.getNumRecords());
	}
}
