package org.culturegraph.metamorph.readers;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;

public interface StreamSender {
	/**
	 * Sets the {@link StreamReceiver} which is used to process events during
	 * the parsing.
	 * 
	 * @param streamReceiver
	 */
	void setStreamReceiver(StreamReceiver streamReceiver);
}
