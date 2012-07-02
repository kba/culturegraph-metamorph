package org.culturegraph.metamorph.stream;

/**
 * @author Markus Michael Geipel
 *
 */
public final class IdChangePipe implements StreamPipe {

	private final EventBuffer eventBuffer = new EventBuffer();
	private StreamReceiver receiver;
	private String currentIdentifier;
	private int depth;
	
	@Override
	public void startRecord(final String identifier) {
		currentIdentifier = identifier;
		depth=0;
	}

	@Override
	public void endRecord() {
		receiver.startRecord(currentIdentifier);
		eventBuffer.replay();
		receiver.endRecord();

	}

	@Override
	public void startEntity(final String name) {
		eventBuffer.startEntity(name);
		++depth;
	}

	@Override
	public void endEntity() {
		eventBuffer.endEntity();
		--depth;

	}

	@Override
	public void literal(final String name, final String value) {
		if(depth==0 && StreamReceiver.ID_NAME.equals(name)){
			currentIdentifier = value;
		}else{
			eventBuffer.literal(name, value);
		}
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R receiver) {
		this.receiver = receiver;
		eventBuffer.setReceiver(receiver);
		return receiver;
	}
}
