package org.culturegraph.metamorph.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link StreamPipe} which buffers incomming records and replays them upon
 * request.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class RecordBuffer implements StreamPipe {

	// private final CGEntityWriter cgEntityWriter = new CGEntityWriter();
	// private final CGEntityReader cgEntityReader = new CGEntityReader();
	// private final StringWriter stringWriter = new StringWriter();

	/**
	 * Defines entity and literal message types.
	 */
	private enum MessageType {
		RECORD_START, RECORD_END, ENTITY_START, ENTITY_END, LITERAL
	}

	private final List<MessageType> typeBuffer = new ArrayList<RecordBuffer.MessageType>();
	private final List<String> valueBuffer = new ArrayList<String>();

	private boolean recordClosed = true;
	private StreamReceiver receiver;

	// RecordBuffer() {
	//	// cgEntityWriter.setPrintWriter(stringWriter);
	//}

	public void replay() {
		if (!recordClosed) {
			throw new IllegalStateException("Current record is not complete. Cannot reply inbetween records.");
		}

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

		// cgEntityReader.read(new StringReader(stringWriter.toString()));

		reset();
	}

	public void reset() {
		// final StringBuffer buffer = stringWriter.getBuffer();
		// buffer.delete(0, buffer.length());
		typeBuffer.clear();
		valueBuffer.clear();
	}

	@Override
	public void startRecord(final String identifier) {
		recordClosed = false;
		typeBuffer.add(MessageType.RECORD_START);
		valueBuffer.add(identifier);
	//	cgEntityWriter.startRecord(identifier);
	}

	@Override
	public void endRecord() {
		recordClosed = true;
		typeBuffer.add(MessageType.RECORD_END);
		//cgEntityWriter.endRecord();
	}

	@Override
	public void startEntity(final String name) {
		typeBuffer.add(MessageType.ENTITY_START);
		valueBuffer.add(name);
		//cgEntityWriter.startEntity(name);
	}

	@Override
	public void endEntity() {
		typeBuffer.add(MessageType.ENTITY_END);
		//cgEntityWriter.endEntity();

	}

	@Override
	public void literal(final String name, final String value) {
		typeBuffer.add(MessageType.LITERAL);
		valueBuffer.add(name);
		valueBuffer.add(value);
		//cgEntityWriter.literal(name, value);
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		this.receiver = streamReceiver;
		return streamReceiver;
	}

}
