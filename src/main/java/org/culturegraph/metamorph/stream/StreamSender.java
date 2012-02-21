package org.culturegraph.metamorph.stream;



/**
 * 
 * Interface for classes sending data as event streams to {@link StreamReceiver}s
 * @author Markus Michael Geipel
 * @see StreamReceiver
 *
 */
public interface StreamSender {

	/**
	 * Sets the downstream {@link StreamReceiver}.
	 * @param streamReceiver
	 * @return the parameter streamReceiver
	 */
	<R extends StreamReceiver> R setReceiver(R streamReceiver);
}
