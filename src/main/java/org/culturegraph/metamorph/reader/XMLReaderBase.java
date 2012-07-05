package org.culturegraph.metamorph.reader;

import java.io.StringReader;

import org.culturegraph.metastream.converter.xml.XmlDecoder;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.framework.XmlPipe;

/**
 * @author Christoph Böhme
 *
 */
public class XMLReaderBase implements Reader {
	
	private final XmlDecoder xmlDecoder = new XmlDecoder();
	private final XmlPipe<StreamReceiver> xmlReceiver;
	
	protected XMLReaderBase(final XmlPipe<StreamReceiver> xmlReceiver) {
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
	public final void resetStream() {
		xmlDecoder.resetStream();
	}

	@Override
	public final void closeStream() {
		xmlDecoder.closeStream();
	}

}
