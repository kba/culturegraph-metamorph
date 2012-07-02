package org.culturegraph.metamorph.stream;

import java.util.ArrayList;
import java.util.List;

import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.framework.StreamReceiverPipe;

/**
 * {@link StreamPipe} which buffers incoming records and replays them upon
 * request.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class EventBuffer implements StreamReceiverPipe<StreamReceiver> {

	/**
	 * Defines entity and literal message types.
	 */
	private enum MessageType {
		RECORD_START, RECORD_END, ENTITY_START, ENTITY_END, LITERAL
	}

	private final List<MessageType> typeBuffer = new ArrayList<EventBuffer.MessageType>();
	private final List<String> valueBuffer = new ArrayList<String>();
	private StreamReceiver receiver;

	/**
	 * replays the buffered event. 
	 */
	public void replay() {

		int index = 0;
		for (MessageType type : typeBuffer) {
			switch (type) {
			case RECORD_START:
				receiver.startRecord(valueBuffer.get(index));
				++index;
				break;
			case RECORD_END:
				receiver.endRecord();
				break;	
				
			case ENTITY_START:
				receiver.startEntity(valueBuffer.get(index));
				++index;
				break;
			case ENTITY_END:
				receiver.endEntity();
				break;
			default:
				receiver.literal(valueBuffer.get(index), valueBuffer.get(index+1));
				index +=2;
				break;
			}
		}
	}

	@Override
	public void startRecord(final String identifier) {
		typeBuffer.add(MessageType.RECORD_START);
		valueBuffer.add(identifier);
	}

	@Override
	public void endRecord() {
		typeBuffer.add(MessageType.RECORD_END);
	}

	@Override
	public void startEntity(final String name) {
		typeBuffer.add(MessageType.ENTITY_START);
		valueBuffer.add(name);
	}

	@Override
	public void endEntity() {
		typeBuffer.add(MessageType.ENTITY_END);
	}

	@Override
	public void literal(final String name, final String value) {
		typeBuffer.add(MessageType.LITERAL);
		valueBuffer.add(name);
		valueBuffer.add(value);
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		this.receiver = streamReceiver;
		return streamReceiver;
	}

	@Override
	public void reset() {
		typeBuffer.clear();
		valueBuffer.clear();
		receiver.reset();
	}

	@Override
	public void closeResources() {
		receiver.closeResources();
	}

}
