package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Markus Michael Geipel
 * 
 */
public class XMLStreamReader implements Reader, ContentHandler {
	
	private static final Pattern TABS = Pattern.compile("\t+");
	private final String recordTagName;
	private StreamReceiver receiver;
	private boolean inRecord;
	private StringBuilder valueBuffer = new StringBuilder();
	
	public XMLStreamReader(final String recordTagName) {
		this.recordTagName = recordTagName;
	}

	public XMLStreamReader() {
		this.recordTagName = System.getProperty("org.culturegraph.metamorph.xml.recordtag");
		if (recordTagName == null) {
			throw new MetamorphException("Missing name for the tag marking a record.");
		}
	}

	@Override
	public final <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		this.receiver = streamReceiver;
		return streamReceiver;
	}

	@Override
	public final void read(final java.io.Reader reader) {
		final XMLReader xmlReader;
		try {
			xmlReader = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			throw new MetamorphException("XMLReader could not be created.", e);
		}
		final InputSource inputSource = new InputSource(reader);
		xmlReader.setContentHandler(this);
		try {
			xmlReader.parse(inputSource);
		} catch (IOException e) {
			throw new MetamorphException(e);
		} catch (SAXException e) {
			throw new MetamorphException(e);
		}
	}

	@Override
	public final void read(final String entry) {
		read(new StringReader(entry));
	}

	@Override
	public final String getId(final String record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final void setDocumentLocator(final Locator locator) {
		// nothing to do
	}

	@Override
	public final void startDocument() throws SAXException {
		// nothing to do
	}

	@Override
	public final void endDocument() throws SAXException {
		// nothing to do
	}

	@Override
	public final void startPrefixMapping(final String prefix, final String uri) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public final void endPrefixMapping(final String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
			throws SAXException {

		writeValue();

		valueBuffer = new StringBuilder();
		if (inRecord) {
			receiver.startEntity(localName);
		} else if (localName.equals(recordTagName)) {
			final String identifier = attributes.getValue("id");
			if (identifier == null) {
				receiver.startRecord(null);
			} else {
				receiver.startRecord(identifier);
			}
			inRecord = true;
		}
		writeAttributes(attributes);
	}

	private void writeValue() {
		final String value = valueBuffer.toString();
		if (!value.trim().isEmpty()) {
			receiver.literal("value", value.replace('\n', ' '));
		}

	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException {
		writeValue();
		valueBuffer = new StringBuilder();
		if (inRecord) {
			if (localName.equals(recordTagName)) {
				inRecord = false;
				receiver.endRecord();
			} else {
				receiver.endEntity();
			}
		}
	}

	@Override
	public final void characters(final char[] chars, final int start, final int length) throws SAXException {
			valueBuffer.append(TABS.matcher(new String(chars, start, length)).replaceAll(""));
	}

	@Override
	public final void ignorableWhitespace(final char[] chars, final int start, final int length) throws SAXException {
		// nothing to do

	}

	@Override
	public final void processingInstruction(final String target, final String data) throws SAXException {
		// nothing to do

	}

	@Override
	public final void skippedEntity(final String name) throws SAXException {
		// nothing to do

	}

	private void writeAttributes(final Attributes attributes) {
		final int length = attributes.getLength();

		for (int i = 0; i < length; ++i) {
			final String name = attributes.getLocalName(i);
			final String value = attributes.getValue(i);
			receiver.literal(name, value);
		}
	}

	@Override
	public final void reset() {
		receiver.reset();
	}

	@Override
	public final void closeResources() {
		receiver.closeResources();
	}

}
