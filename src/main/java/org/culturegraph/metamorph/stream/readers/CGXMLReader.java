/**
 * 
 */
package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metastream.converter.xml.CGXMLHandler;
import org.culturegraph.metastream.converter.xml.XMLDecoder;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 *
 */
public final class CGXMLReader implements Reader {

	private final XMLDecoder xmlDecoder = new XMLDecoder();
	private final CGXMLHandler cgXMLHandler = new CGXMLHandler();
	
	public CGXMLReader() {
		xmlDecoder.setReceiver(cgXMLHandler);
	}
	
	@Override
	public <R extends StreamReceiver> R setReceiver(final R receiver) {
		cgXMLHandler.setReceiver(receiver);
		return receiver;
	}
	
	@Override
	public void read(final java.io.Reader reader) {
		xmlDecoder.process(reader);
	}

	@Override
	public void read(final String entry) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		xmlDecoder.close();
	}

	@Override
	public String getId(final String record) {
		throw new UnsupportedOperationException();
	}

}
