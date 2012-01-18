/**
 * 
 */
package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.InputStream;

import org.culturegraph.metamorph.stream.receivers.EventStreamValidator;
import org.culturegraph.metamorph.stream.receivers.EventStreamWriter;
import org.junit.Test;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class CGXmlReaderTest {

	@Test
	public void testReadStringStreamReceiver() throws IOException {
		final CGXmlReader reader = new CGXmlReader();
		final EventStreamWriter writer = new EventStreamWriter();
		
		final InputStream input = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("cgxml_test.xml");
		
		reader.setReceiver(writer);
		writer.startStream();
		reader.read(input);
		writer.endStream();
		
		final EventStreamValidator validator = new EventStreamValidator(writer.getEventStream(), true, true, true);
		
		validator.startStream();
			validator.startRecord("1");
				validator.literal("Name", "Thomas Mann");
				validator.startEntity("Address");
					validator.startEntity("Street");
						validator.literal("Street", "Alte Landstrasse");
						validator.literal("Number", "39");
					validator.endEntity();
					validator.literal("City", "Kilchberg");
					validator.literal("Postcode", null);
				validator.endEntity();
			validator.endRecord();
			validator.startRecord(null);
				validator.literal("Name", "Günter Grass");
				validator.startEntity("Address");
					validator.startEntity("Street");
						validator.literal("Street", "Glockengießerstraße");
						validator.literal("Number", "21");
					validator.endEntity();
					validator.literal("City", "Lübeck");
					validator.literal("Postcode", "23552");
				validator.endEntity();
			validator.endRecord();
		validator.endStream();
	}

}
