package org.culturegraph.metamorph.readers;

import org.culturegraph.metastream.converter.LineReader;
import org.culturegraph.metastream.framework.ObjectPipe;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class ReaderBase implements Reader {

	private final LineReader lineReader;
	private final ObjectPipe<String, StreamReceiver> decoder;
	
	public ReaderBase(final ObjectPipe<String, StreamReceiver> decoder) {
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
	public final void reset() {
		lineReader.reset();
	}
	
	@Override
	public final void closeResources() {
		lineReader.closeResources();
	}
	
}
