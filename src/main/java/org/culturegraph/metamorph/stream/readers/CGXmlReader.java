/**
 * 
 */
package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public class CGXmlReader implements Reader {
	
	private static final String RECORD_TAG = "record";
	private static final String ENTITY_TAG = "entity";
	private static final String LITERAL_TAG = "literal";
	private static final String ID_ATTR = "id";
	private static final String NAME_ATTR = "name";
	private static final String VALUE_ATTR = "value";

	private StreamReceiver receiver;

	@Override
	public final <R extends StreamReceiver> R  setReceiver(final R receiver) {
		this.receiver = receiver;
		return receiver;
	}

	@Override
	public String getId(String record) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void read(final InputStream inputStream) throws IOException {
		if(inputStream==null){
			throw new IllegalArgumentException("InputStream must be set");
		}
		read(new InputStreamReader(inputStream, "UTF-8"));		
	}
	
	@Override
	public void read(final String entry) {
		try {
			read(new StringReader(entry));
		} catch (IOException e) {
			// We do not expect any errors when processing a string
		}
	}
		
	// TODO: Add schema validation
	@Override
	public void read(final java.io.Reader reader) throws IOException {
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		// factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
		
		try {
			final XMLStreamReader stream = factory.createXMLStreamReader(reader);
			
			while (stream.hasNext()) {
				final int type = stream.next();
				if (type == XMLStreamConstants.START_ELEMENT) {
					if (RECORD_TAG.equals(stream.getLocalName())) {
						receiver.startRecord(stream.getAttributeValue(null, ID_ATTR));
					} else if (ENTITY_TAG.equals(stream.getLocalName())) {
						receiver.startEntity(stream.getAttributeValue(null, NAME_ATTR));
					} else if (LITERAL_TAG.equals(stream.getLocalName())) {
						receiver.literal(stream.getAttributeValue(null, NAME_ATTR), 
								stream.getAttributeValue(null, VALUE_ATTR));
					}
				} else if (type == XMLStreamConstants.END_ELEMENT) {
					if (RECORD_TAG.equals(stream.getLocalName())) {
						receiver.endRecord();
					} else if (ENTITY_TAG.equals(stream.getLocalName())) {
						receiver.endEntity();
					}
				}
			}
			stream.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
