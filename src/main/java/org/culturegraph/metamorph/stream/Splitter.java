package org.culturegraph.metamorph.stream;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.stream.receivers.SingleValueWriter;

/**
 * @author Markus Michael Geipel
 *
 */
public final class Splitter implements StreamPipe {

	private final RecordBuffer buffer = new RecordBuffer();
	private final SingleValueWriter singleValueWriter = new SingleValueWriter();
	private final Map<String, StreamReceiver> receiverMap = new HashMap<String, StreamReceiver>();
	private final Metamorph metamorph;
	
	public Splitter(final String morphDef) {
		metamorph = MetamorphBuilder.build(morphDef);
		metamorph.setReceiver(singleValueWriter);
	}
	
	public Splitter(final Metamorph metamorph) {
		this.metamorph = metamorph;
		metamorph.setReceiver(singleValueWriter);
	}
	
	private void dispatch(){
		final String key = singleValueWriter.getValue();
		final StreamReceiver receiver = receiverMap.get(key);
		
		if(null==receiver){
			buffer.reset();
			return;
		}
		
		buffer.setReceiver(receiver);
		buffer.replay();
	}
	
	@Override
	public void startRecord(final String identifier) {
		buffer.startRecord(identifier);
		metamorph.startRecord(identifier);
	
	}

	@Override
	public void endRecord() {
		buffer.endRecord();
		metamorph.endRecord();
		dispatch();
	}

	@Override
	public void startEntity(final String name) {
		buffer.startEntity(name);
		metamorph.startEntity(name);
	}

	@Override
	public void endEntity() {
		buffer.endEntity();
		metamorph.endEntity();
	}

	@Override
	public void literal(final String name, final String value) {
		buffer.literal(name, value);
		metamorph.literal(name, value);
	}

	public <R extends StreamReceiver> R setReceiver(final String key, final R streamReceiver) {
		receiverMap.put(key, streamReceiver);
		return streamReceiver;
	}
	
	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		receiverMap.put("", streamReceiver);
		return streamReceiver;
	}
}
