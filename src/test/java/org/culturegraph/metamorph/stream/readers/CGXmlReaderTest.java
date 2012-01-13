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
		
		writer.setStrictRecordOrder(true);
		writer.setStrictKeyOrder(true);
		writer.setStrictValueOrder(true);
		
		writer.startChecking();
		writer.startRecord("1");
			writer.literal("Name", "Thomas Mann");
			writer.startEntity("Address");
				writer.startEntity("Street");
					writer.literal("Street", "Alte Landstrasse");
					writer.literal("Number", "39");
				writer.endEntity();
				writer.literal("City", "Kilchberg");
				writer.literal("Postcode", null);
			writer.endEntity();
		writer.endRecord();
		writer.startRecord(null);
			writer.literal("Name", "Günter Grass");
			writer.startEntity("Address");
				writer.startEntity("Street");
					writer.literal("Street", "Glockengießerstraße");
					writer.literal("Number", "21");
				writer.endEntity();
				writer.literal("City", "Lübeck");
				writer.literal("Postcode", "23552");
			writer.endEntity();
		writer.endRecord();
		writer.endChecking();
	}

}
