package org.culturegraph.metamorph.stream;


public interface StreamSender {
	/**
	 * Sets the {@link StreamReceiver} which is used to process events during
	 * the parsing.
	 * 
	 * @param streamReceiver
	 */
	void setStreamReceiver(StreamReceiver streamReceiver);
}
