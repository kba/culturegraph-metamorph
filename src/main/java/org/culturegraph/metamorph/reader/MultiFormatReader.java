package org.culturegraph.metamorph.reader;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metastream.annotation.Description;
import org.culturegraph.metastream.annotation.In;
import org.culturegraph.metastream.annotation.Out;
import org.culturegraph.metastream.framework.StreamReceiver;

/**
 * {@link MultiFormatReader} uses the {@link AbstractReaderFactory} to handle
 * all registered input formats.
 * 
 * @author Markus Michael Geipel
 */

@Description("Reads different formts. Format given in brackets.")
@In(java.io.Reader.class)
@Out(StreamReceiver.class)

public final class MultiFormatReader implements Reader{
	private static final ReaderFactory READER_FACTORY = new ReaderFactory();
	
	private static final String ERROR_NO_FORMAT = "no format set";
	private static final String ERROR_RECEIVER_NULL = "'streamReceiver' must not be null";
	private Reader currentReader;
	private final Map<String, Reader> openReaders = new HashMap<String, Reader>();
	
	private StreamReceiver streamReceiver;
	private String currentFormat;

	public MultiFormatReader() {
		//nothing
	}
	
	public MultiFormatReader(final String format) {
		setFormat(format);
	}


	public String getFormat() {
		return currentFormat;
	}

	public void setFormat(final String format) {
		if (format == null) {
			throw new IllegalArgumentException("'format' must not be null");
		}

		currentReader = openReaders.get(format);
		currentFormat = format;

		if (null == currentReader) {
			currentReader = READER_FACTORY.newInstance(format);
			openReaders.put(format, currentReader);

			if (streamReceiver != null) {
				currentReader.setReceiver(streamReceiver);
			}
		}

	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		if (streamReceiver == null) {
			throw new IllegalArgumentException(ERROR_RECEIVER_NULL);
		}

		this.streamReceiver = streamReceiver;

			for (Reader reader : openReaders.values()) {
				reader.setReceiver(streamReceiver);
			}
		

		return streamReceiver;
	}

	@Override
	public void read(final String entry) {
		if (streamReceiver == null) {
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}
		currentReader.read(entry);
	}


	@Override
	public void process(final java.io.Reader reader) {
		currentReader.process(reader);

	}

	@Override
	public void resetStream() {
		currentReader.resetStream();
	}

	@Override
	public void closeStream() {
		currentReader.closeStream();
	}
}
