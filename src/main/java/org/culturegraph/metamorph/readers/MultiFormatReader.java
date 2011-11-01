package org.culturegraph.metamorph.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * {@link MultiFormatReader} uses the {@link AbstractReaderFactory} to handle all registered input formats.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class MultiFormatReader implements Reader{

	private static final String ERROR_NO_FORMAT = "no format set";
	private static final String ERROR_RECEIVER_NULL = "'streamReceiver' must not be null";
	private Reader currentReader;
	private final Map<String, Reader> openReaders = new HashMap<String, Reader>();
	private final ReaderFactory readerFactory = AbstractReaderFactory.newInstance(); 
	private StreamReceiver streamReceiver;
	
	
	public void setFormat(final String format){
		currentReader = openReaders.get(format);
		if(null==currentReader){
			currentReader = readerFactory.newReader(format);
			currentReader.setStreamReceiver(streamReceiver);
			openReaders.put(format, currentReader);
		}
	}
	
	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		if(streamReceiver==null) {
			throw new IllegalArgumentException(ERROR_RECEIVER_NULL);
		}
		this.streamReceiver = streamReceiver;
		for(Reader reader: openReaders.values()){
			reader.setStreamReceiver(streamReceiver);
		}
	}

	@Override
	public void read(final InputStream inputStream) throws IOException {
		if(streamReceiver==null){
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}
		currentReader.read(inputStream);
	}

	@Override
	public void read(final String entry) {
		if(streamReceiver==null){
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}
		currentReader.read(entry);
	}

	@Override
	public String getId(final String record) {
		if(streamReceiver==null){
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}
		return currentReader.getId(record);
	}
}
