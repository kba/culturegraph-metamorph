package org.culturegraph.metamorph.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.culturegraph.metamorph.stream.StreamReceiver;




/**
 * Parses a raw Picaplus stream (utf8 encoding assumed).
 * Events are handled by a {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @status Productive
 * @see StreamReceiver
 */
public abstract class AbstractReader implements Reader{

	private StreamReceiver streamReceiver;
	

	@Override
	public final void read(final InputStream inputStream) throws IOException {

		if(inputStream==null){
			throw new IllegalArgumentException("InputStream must be set");
		}
		
		if(streamReceiver==null){
			throw new IllegalStateException("StreamReceiver must be set");
		}
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, getCharset()));
		
		String line = reader.readLine();
		while (line != null) {
			if(!line.isEmpty()){
				processRecord(line);
			}
			line = reader.readLine();
		}
	}
	
	protected abstract Charset getCharset();
	

	@Override
	public final void read(final String entry){
		assert null!=entry && streamReceiver!=null;
		processRecord(entry);
	}

	protected abstract void processRecord(final String record);

	protected final StreamReceiver getStreamReceiver(){
		return streamReceiver;
	}
	
	@Override
	public final void setStreamReceiver(final StreamReceiver streamReceiver) {
		this.streamReceiver = streamReceiver;		
	}
}
