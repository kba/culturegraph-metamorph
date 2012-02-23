/**
 * 
 */
package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.culturegraph.metamorph.core.exceptions.ShouldNeverHappenException;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 * 
 */
public final class CGXmlReader implements Reader {

	/**
	 * TODO comment!
	 */
	private final class CGXmlHandler extends DefaultHandler {
		private static final String RECORD_TAG = "record";
		private static final String ENTITY_TAG = "entity";
		private static final String LITERAL_TAG = "literal";
		private static final String ID_ATTR = "id";
		private static final String NAME_ATTR = "name";
		private static final String VALUE_ATTR = "value";

		protected CGXmlHandler() {
			super();
		}

		@Override
		public void startElement(final String uri, final String localName, final String qName,
				final Attributes attributes) {
			if (RECORD_TAG.equals(localName)) {
				getReceiver().startRecord(attributes.getValue("", ID_ATTR));
			} else if (ENTITY_TAG.equals(localName)) {
				getReceiver().startEntity(attributes.getValue("", NAME_ATTR));
			} else if (LITERAL_TAG.equals(localName)) {
				getReceiver().literal(attributes.getValue("", NAME_ATTR), attributes.getValue("", VALUE_ATTR));
			}
		}

		@Override
		public void endElement(final String uri, final String localName, final String qName) {
			if (RECORD_TAG.equals(localName)) {
				getReceiver().endRecord();
			} else if (ENTITY_TAG.equals(localName)) {
				getReceiver().endEntity();
			}
		}
	}

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String SCHEMA_FILE = "schema/cgxml.xsd";

	private StreamReceiver receiver;

	@Override
	public <R extends StreamReceiver> R setReceiver(final R receiver) {
		this.receiver = receiver;
		return receiver;
	}

	public StreamReceiver getReceiver() {
		return this.receiver;
	}

	@Override
	public String getId(final String record) {
		throw new UnsupportedOperationException();
	}

	public void read(final InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream must be set");
		}
		read(new InputSource(inputStream));
	}

	@Override
	public void read(final String entry) {
		try {
			read(new StringReader(entry));
		} catch (IOException e) {
			throw new ShouldNeverHappenException(e);
		}
	}

	@Override
	public void read(final java.io.Reader reader) throws IOException {
		if (reader == null) {
			throw new IllegalArgumentException("Reader must be set");
		}
		read(new InputSource(reader));
	}

	public void read(final InputSource inputSource) throws IOException {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);

		final URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_FILE);

		try {
			final SAXParser saxParser = factory.newSAXParser();

			saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA_NS_URI);
			saxParser.setProperty(JAXP_SCHEMA_SOURCE, schemaUrl.toString());

			final XMLReader xmlReader = saxParser.getXMLReader();
			final CGXmlHandler handler = new CGXmlHandler();
	
			xmlReader.setContentHandler(handler);

			xmlReader.parse(inputSource);
		} catch (SAXException e) {
			throw new RecordFormatException(e);
		} catch (ParserConfigurationException e) {
			throw new RecordFormatException(e);
		}
	}
}
