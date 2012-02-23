package org.culturegraph.metamorph.stream;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.culturegraph.metamorph.core.exceptions.ShouldNeverHappenException;
import org.culturegraph.metamorph.stream.readers.CGEntityReader;
import org.culturegraph.metamorph.stream.receivers.CGEntityWriter;

/**
 * {@link StreamPipe} which buffers incomming records and replays them upon request.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class RecordBuffer implements StreamPipe {

	private final CGEntityWriter cgEntityWriter = new CGEntityWriter();
	private final CGEntityReader cgEntityReader = new CGEntityReader();
	private final StringWriter stringWriter = new StringWriter();
	private boolean recordClosed = true;

	
	public RecordBuffer() {
		cgEntityWriter.setPrintWriter(stringWriter);
	}
	
	public void replay(){
		if(!recordClosed){
			throw new IllegalStateException("Current record is not complete. Cannot reply inbetween records.");
		}
		try {
			cgEntityReader.read(new StringReader(stringWriter.toString()));
		} catch (IOException e) {
			throw new ShouldNeverHappenException("IOException while reading a String.", e);
		}
		reset();
	}
	
	public void reset() {
		final StringBuffer buffer = stringWriter.getBuffer();
		buffer.delete(0, buffer.length());
	}
	
	@Override
	public String toString() {
		return stringWriter.toString();
	}

	@Override
	public void startRecord(final String identifier) {
		recordClosed = false;
		cgEntityWriter.startRecord(identifier);
	}

	@Override
	public void endRecord() {
		recordClosed = true;
		cgEntityWriter.endRecord();
	}

	@Override
	public void startEntity(final String name) {
		cgEntityWriter.startEntity(name);
	}

	@Override
	public void endEntity() {
		cgEntityWriter.endEntity();

	}

	@Override
	public void literal(final String name, final String value) {
		cgEntityWriter.literal(name, value);
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		cgEntityReader.setReceiver(streamReceiver);
		return streamReceiver;
	}

}
