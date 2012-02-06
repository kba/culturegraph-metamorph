package org.culturegraph.metamorph.stream;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import junit.framework.Assert;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.stream.readers.PicaReader;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.receivers.DefaultWriter;
import org.junit.Test;

/**
 * Tests if {@link LogPipe} is correctly piping all events.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class LogPipeTest {
	
	
	@Test
	public void testReadWriteRead() throws IOException {
		final Reader picaReader = new PicaReader();
		final StringWriter refereceStringWriter = new StringWriter();
		final DefaultWriter referenceWriter = new DefaultWriter(refereceStringWriter);
		
		picaReader.setReceiver(referenceWriter);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		referenceWriter.flush();
		
		
		final StringWriter finalStringWriter = new StringWriter();
		final DefaultWriter finalWriter = new DefaultWriter(finalStringWriter);
		
		picaReader.setReceiver(new LogPipe()).setReceiver(finalWriter);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		finalWriter.flush();
		
		Assert.assertEquals(refereceStringWriter.toString(), finalStringWriter.toString());
	
	}
}
