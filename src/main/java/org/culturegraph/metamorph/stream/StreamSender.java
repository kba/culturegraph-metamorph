package org.culturegraph.metamorph.stream;



public interface StreamSender {

	/**
	 * Sets the {@link StreamReceiver} which is used to process events during
	 * the parsing.
	 * @param streamReceiver
	 * @return the parameter streamReceiver
	 */
	<R extends StreamReceiver> R setReceiver(R streamReceiver);
}
