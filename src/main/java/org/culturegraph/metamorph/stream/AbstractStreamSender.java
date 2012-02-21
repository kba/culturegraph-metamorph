package org.culturegraph.metamorph.stream;

/**
 * Base class for {@link StreamSender}s which implements the receiver handling.
 * 
 * @author Markus Michael Geipel
 *
 */
public abstract class AbstractStreamSender implements StreamSender {

	private StreamReceiver receiver;
	
	@Override
	public final <R extends StreamReceiver> R setReceiver(final R receiver) {
		this.receiver = receiver;
		return null;
	}

	public final StreamReceiver getReceiver() {
		return receiver;
	}

}
