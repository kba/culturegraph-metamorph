package org.culturegraph.metamorph.pipe;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metastream.annotation.Description;
import org.culturegraph.metastream.annotation.In;
import org.culturegraph.metastream.annotation.Out;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.framework.StreamPipe;
import org.culturegraph.metastream.pipe.StreamBuffer;
import org.culturegraph.metastream.sink.SingleValue;

/**
 * @author Markus Michael Geipel
 *
 */
@Description("Splits a stream based on a morph definition")
@In(StreamReceiver.class)
@Out(StreamReceiver.class)
public final class Splitter implements StreamPipe<StreamReceiver> {

	private final StreamBuffer buffer = new StreamBuffer();
	private final SingleValue singleValue = new SingleValue();
	private final Map<String, StreamReceiver> receiverMap = new HashMap<String, StreamReceiver>();
	private final Metamorph metamorph;
	
	public Splitter(final String morphDef) {
		metamorph = MetamorphBuilder.build(morphDef);
		metamorph.setReceiver(singleValue);
	}
	
	public Splitter(final Metamorph metamorph) {
		this.metamorph = metamorph;
		metamorph.setReceiver(singleValue);
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R receiver) {
		receiverMap.put("", receiver);
		return receiver;
	}

	public <R extends StreamReceiver> R setReceiver(final String key, final R receiver) {
		receiverMap.put(key, receiver);
		return receiver;
	}
	
	private void dispatch(){
		final String key = singleValue.getValue();
		final StreamReceiver receiver = receiverMap.get(key);
		
		if(null != receiver){
			buffer.setReceiver(receiver);
			buffer.replay();
		}
		buffer.clear();
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

	@Override
	public void resetStream() {
		buffer.clear();
		metamorph.resetStream();
		for (StreamReceiver receiver: receiverMap.values()) {
			receiver.resetStream();
		}
	}
	
	@Override
	public void closeStream() {
		buffer.clear();
		metamorph.closeStream();
		for (StreamReceiver receiver: receiverMap.values()) {
			receiver.closeStream();
		}
	}
}
