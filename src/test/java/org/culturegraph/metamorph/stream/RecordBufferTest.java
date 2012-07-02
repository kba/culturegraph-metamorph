package org.culturegraph.metamorph.stream;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import junit.framework.Assert;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.stream.readers.PicaReader;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.readers.RecordFormatException;
import org.culturegraph.metamorph.stream.receivers.DefaultWriter;
import org.junit.Test;

/**
 * Tests {@link EventBuffer}.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class RecordBufferTest {

	@Test
	public void testCorrectBuffering() throws IOException {
		// prepare reference
		final Reader picaReader = new PicaReader();
		final StringWriter refereceStringWriter = new StringWriter();
		final DefaultWriter referenceWriter = new DefaultWriter(refereceStringWriter);

		picaReader.setReceiver(referenceWriter);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		referenceWriter.flush();

		// prepare buffer

		final StringWriter finalStringWriter = new StringWriter();
		final DefaultWriter finalWriter = new DefaultWriter(finalStringWriter);

		final EventBuffer buffer = new EventBuffer();

		picaReader.setReceiver(buffer).setReceiver(finalWriter);

		// buffer
		picaReader.read(new FileReader(DataFilePath.PND_PICA));

		// replay

		buffer.replay();
		finalWriter.flush();
		
		// check result
		Assert.assertEquals(refereceStringWriter.toString(), finalStringWriter.toString());

	}
}
