package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metastream.converter.xml.XMLDecoder;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.framework.XMLToStreamConverter;

/**
 * @author Christoph Böhme
 *
 */
public class XMLReaderBase implements Reader {
	
	private final XMLDecoder xmlDecoder = new XMLDecoder();
	private final XMLToStreamConverter xmlReceiver;
	
	protected XMLReaderBase(final XMLToStreamConverter xmlReceiver) {
		this.xmlReceiver = xmlReceiver;
		xmlDecoder.setReceiver(this.xmlReceiver);
	}
	
	@Override
	public final <R extends StreamReceiver> R setReceiver(final R receiver) {
		xmlReceiver.setReceiver(receiver);
		return receiver;
	}
	
	@Override
	public final void read(final java.io.Reader reader) {
		xmlDecoder.process(reader);
	}

	@Override
	public final void read(final String entry) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void close() {
		xmlDecoder.close();
	}

	@Override
	public final String getId(final String record) {
		throw new UnsupportedOperationException();
	}

}
