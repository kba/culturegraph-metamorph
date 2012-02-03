package org.culturegraph.metamorph.stream;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.culturegraph.metamorph.DataFilePath;
import org.culturegraph.metamorph.stream.readers.CGEntityReader;
import org.culturegraph.metamorph.stream.readers.PicaReader;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.receivers.CGEntityWriter;
import org.culturegraph.metamorph.stream.receivers.DefaultWriter;
import org.junit.Test;

/**
 * Tests correctness of {@link CGEntityReader} and {@link CGEntityWriter}.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class CGEntityTest {
	
	
	@Test
	public void testReadWriteRead() throws IOException {
		final Reader picaReader = new PicaReader();
		final StringWriter refereceStringWriter = new StringWriter();
		final DefaultWriter referenceWriter = new DefaultWriter(refereceStringWriter);
		
		picaReader.setReceiver(referenceWriter);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		referenceWriter.flush();
		
		
		final StringWriter tempStringWriter = new StringWriter();
		final CGEntityWriter writer = new CGEntityWriter(tempStringWriter);
				
		picaReader.setReceiver(writer);
		picaReader.read(new FileReader(DataFilePath.PND_PICA));
		writer.flush();
		
		
		final StringWriter finalStringWriter = new StringWriter();
		final CGEntityReader reader = new CGEntityReader();
		final DefaultWriter finalWriter = new DefaultWriter(finalStringWriter);
		
		reader.setReceiver(finalWriter);
		reader.read(new StringReader(tempStringWriter.toString()));
		finalWriter.flush();
		
		Assert.assertEquals(refereceStringWriter.toString(), finalStringWriter.toString());
	
	}
}
