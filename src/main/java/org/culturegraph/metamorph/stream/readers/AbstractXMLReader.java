package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.StringReader;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metastream.framework.StreamReceiver;
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
public abstract class AbstractXMLReader implements Reader, ContentHandler {

	private StreamReceiver receiver;

	protected final StreamReceiver getReceiver() {
		return receiver;
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
	public void setDocumentLocator(final Locator locator) {
		// nothing to do
	}

	@Override
	public void startDocument() throws SAXException {
		// nothing to do
	}

	@Override
	public void endDocument() throws SAXException {
		// nothing to do
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
		// nothing to do

	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException {
		// nothing to do

	}

	@Override
	public void characters(final char[] chars, final int start, final int length) throws SAXException {
		// nothing to do
	}

	@Override
	public void ignorableWhitespace(final char[] chars, final int start, final int length) throws SAXException {
		// nothing to do

	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException {
		// nothing to do

	}

	@Override
	public void skippedEntity(final String name) throws SAXException {
		// nothing to do

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
