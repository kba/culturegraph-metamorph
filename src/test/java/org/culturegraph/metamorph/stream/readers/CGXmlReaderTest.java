/**
 * 
 */
package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.InputStream;

import org.culturegraph.metamorph.stream.receivers.CheckWriter;
import org.junit.Test;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public class CGXmlReaderTest {

	@Test
	public void testReadStringStreamReceiver() throws IOException {
		final CGXmlReader reader = new CGXmlReader();
		final CheckWriter writer = new CheckWriter();
		
		final InputStream input = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("cgxml_test.xml");
		
		reader.setReceiver(writer);
		reader.read(input);
		
		writer.startChecking();
		
		writer.startRecord("1");
			writer.literal("Name", "Thomas Mann");
			writer.startEntity("Address");
				writer.startEntity("Street");
					writer.literal("Street", "Hmm");
					writer.literal("Number", "3");
				writer.endEntity();
				writer.literal("City", "Lübeck");
				writer.literal("Postcode", null);
			writer.endEntity();
		writer.endRecord();
		writer.startRecord(null);
			writer.literal("Name", "Karl Marx");
			writer.startEntity("Address");
				writer.startEntity("Street");
					writer.literal("Street", "HmmHmm");
					writer.literal("Number", "4");
				writer.endEntity();
				writer.literal("City", "Lübeck");
				writer.literal("Postcode", "12343");
			writer.endEntity();
		writer.endRecord();
		
		writer.endChecking();
	}

}
