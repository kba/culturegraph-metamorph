package org.culturegraph.metamorph.stream.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Parses a raw Picaplus stream (utf8 encoding assumed). Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @see StreamReceiver
 */
public abstract class AbstractReader implements Reader {

	private StreamReceiver streamReceiver;



	@Override
	public final void read(final java.io.Reader reader)  throws IOException {
		if(reader==null){
			throw new IllegalArgumentException("'reader' must be set");
		}
		
		if(streamReceiver==null){
			throw new IllegalStateException("StreamReceiver must be set");
		}
		
		final BufferedReader bufferedReader = new BufferedReader(reader);
		
		String line = bufferedReader.readLine();
		while (line != null) {
			if(!line.isEmpty()){
				processRecord(line);
			}
			line = bufferedReader.readLine();
		}

		bufferedReader.close();
		
	}
	



//	public final void read(final InputStream inputStream) throws IOException {
//		if (inputStream == null) {
//			throw new IllegalArgumentException("InputStream must be set");
//		}
//		read(new InputStreamReader(inputStream, getCharset()));
//	}


	@Override
	public final void read(final String entry) {
		assert null != entry && streamReceiver != null;
		processRecord(Normalizer.normalize(entry, Form.NFC));
	}

	protected abstract void processRecord(final String record);

	protected final StreamReceiver getStreamReceiver() {
		return streamReceiver;
	}

	@Override
	public final <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		this.streamReceiver = streamReceiver;
		return streamReceiver;
	}
}