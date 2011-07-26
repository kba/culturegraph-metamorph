package org.culturegraph.metamorph.readers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;
import org.marc4j.marc.VariableField;

/**
 * Parses a raw Marc stream using Marc4j. Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @see StreamReceiver
 */
public final class MarcReader2 implements RawRecordReader {

	
	
	private StreamReceiver streamReceiver;

	@SuppressWarnings("unchecked") // marc4j is not type safe!
	protected void processRecord(final Record record) {
		streamReceiver.startRecord();

		
		for(VariableField field : (List<VariableField>)record.getVariableFields()){
			streamReceiver.literal("name", "value");
		}
		
		
		streamReceiver.endRecord();
	}

	@Override
	public void read(final InputStream inputStream) throws IOException {
		final MarcStreamReader marcStreamReader = new MarcStreamReader(inputStream);
		while (marcStreamReader.hasNext()) {
			processRecord(marcStreamReader.next());
			
		}
	}
	
        

	@Override
	public void read(final String entry) {
		try {
			read(new ByteArrayInputStream(entry.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new MetamorphException("Encoding not found", e);
		} catch (IOException e) {
			throw new MetamorphException(e);
		}
	}

	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		this.streamReceiver = streamReceiver;

	}

	protected StreamReceiver getStreamReceiver() {
		return streamReceiver;
	}
}
