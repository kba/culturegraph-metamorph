package org.culturegraph.metamorph.reader;

import java.io.StringReader;

import org.culturegraph.metastream.converter.xml.XMLDecoder;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.framework.XMLPipe;

/**
 * @author Christoph Böhme
 *
 */
public class XMLReaderBase implements Reader {
	
	private final XMLDecoder xmlDecoder = new XMLDecoder();
	private final XMLPipe<StreamReceiver> xmlReceiver;
	
	protected XMLReaderBase(final XMLPipe<StreamReceiver> xmlReceiver) {
		this.xmlReceiver = xmlReceiver;
		xmlDecoder.setReceiver(this.xmlReceiver);
	}
	
	@Override
	public final <R extends StreamReceiver> R setReceiver(final R receiver) {
		xmlReceiver.setReceiver(receiver);
		return receiver;
	}
	
	@Override
	public final void process(final java.io.Reader reader) {
		xmlDecoder.process(reader);
	}

	@Override
	public final void read(final String entry) {
		xmlDecoder.process(new StringReader(entry));
	}

	@Override
	public final void reset() {
		xmlDecoder.reset();
	}

	@Override
	public final void closeResources() {
		xmlDecoder.closeResources();
	}

}
