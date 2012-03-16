package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metastream.converter.LineReader;
import org.culturegraph.metastream.converter.StringDecoder;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class ReaderBase implements Reader {

	private final LineReader lineReader;
	private final StringDecoder decoder;
	
	protected ReaderBase(final StringDecoder decoder) {
		super();
		
		this.decoder = decoder;
		lineReader = new LineReader();
		lineReader.setReceiver(this.decoder);
	}

	@Override
	public final <R extends StreamReceiver> R setReceiver(final R receiver) {
		decoder.setReceiver(receiver);
		return receiver;
	}
	
	@Override
	public final void read(final java.io.Reader reader) {
		lineReader.process(reader);
	}

	@Override
	public final void read(final String entry) {
		decoder.process(entry);
	}

	@Override
	public final void close() {
		lineReader.close();
	}

	@Override
	public final String getId(final String record) {
		return decoder.getId(record);
	}
	
}
