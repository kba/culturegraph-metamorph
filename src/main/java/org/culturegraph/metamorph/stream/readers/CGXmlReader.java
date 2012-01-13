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
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class CGXmlReader implements Reader {
		
	private final class CGXmlHandler extends DefaultHandler {
		private static final String RECORD_TAG = "record";
		private static final String ENTITY_TAG = "entity";
		private static final String LITERAL_TAG = "literal";
		private static final String ID_ATTR = "id";
		private static final String NAME_ATTR = "name";
		private static final String VALUE_ATTR = "value";
	
		/**
		 * 
		 */
		public CGXmlHandler() {
			// Nothing to do
		}

		@Override
		public void startElement(final String uri, final String localName, 
				final String qName, final Attributes attributes) {
			if (RECORD_TAG.equals(localName)) {
				receiver.startRecord(attributes.getValue("", ID_ATTR));
			} else if (ENTITY_TAG.equals(localName)) {
				receiver.startEntity(attributes.getValue("", NAME_ATTR));
			} else if (LITERAL_TAG.equals(localName)) {
				receiver.literal(attributes.getValue("", NAME_ATTR), 
						attributes.getValue("", VALUE_ATTR));
			}
		}
		
		@Override 
		public void endElement(final String uri, final String localName, 
				final String qName) {
			if (RECORD_TAG.equals(localName)) {
				receiver.endRecord();
			} else if (ENTITY_TAG.equals(localName)) {
				receiver.endEntity();
			}
		}
		
		@Override
		public void error(SAXParseException e) {
			throw new RuntimeException(e);
		}
		
		@Override
		public void fatalError(SAXParseException e) {
			throw new RuntimeException(e);			
		}
		
		@Override
		public void warning(SAXParseException e) {
			throw new RuntimeException(e);			
		}
	}
	
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String SCHEMA_FILE = "cgxml.xsd";

	protected StreamReceiver receiver;

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
		if(inputStream == null){
			throw new IllegalArgumentException("InputStream must be set");
		}
		read(new InputSource(inputStream));
	}
	
	@Override
	public void read(final String entry) {
		try {
			read(new StringReader(entry));
		} catch (IOException e) {
			// We do not expect any errors when processing a string
		}
	}
	
	@Override
	public void read(final java.io.Reader reader) throws IOException {
		if(reader == null){
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

			CGXmlHandler handler = new CGXmlHandler();
			
			xmlReader.setErrorHandler(handler);
			xmlReader.setContentHandler(handler);

			xmlReader.parse(inputSource);
		} catch (SAXNotRecognizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
