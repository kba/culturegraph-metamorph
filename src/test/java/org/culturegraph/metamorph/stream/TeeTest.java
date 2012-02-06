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
 * Tests if {@link Tee} is correctly piping all events.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class TeeTest {
	
	
	@Test
	public void testReadWriteRead() throws IOException {
		final Reader picaReader = new PicaReader();
		
		final StringWriter refereceStringWriter = new StringWriter();
		final DefaultWriter referenceWriter = new DefaultWriter(refereceStringWriter);
		
		picaReader.setReceiver(referenceWriter);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		referenceWriter.flush();
		
				
		final StringWriter finalStringWriter1 = new StringWriter();
		final DefaultWriter finalWriter1 = new DefaultWriter(finalStringWriter1);
		
		final StringWriter finalStringWriter2 = new StringWriter();
		final DefaultWriter finalWriter2 = new DefaultWriter(finalStringWriter2);
		
		
		picaReader.setReceiver(new Tee()).setReceivers(finalWriter1, finalWriter2);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		finalWriter1.flush();
		finalWriter2.flush();
		
		Assert.assertEquals(refereceStringWriter.toString(), finalStringWriter1.toString());
		Assert.assertEquals(refereceStringWriter.toString(), finalStringWriter2.toString());
	
	}
}
