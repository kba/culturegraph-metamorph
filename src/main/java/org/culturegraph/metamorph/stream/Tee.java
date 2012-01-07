/**
 * 
 */
package org.culturegraph.metamorph.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Replicates an event stream to an arbitrary number of {@link StreamReceiver}s. 
 * 
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class Tee implements StreamReceiver, StreamSender {

	private final List<StreamReceiver> receivers = new ArrayList<StreamReceiver>();
	
	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		if(!receivers.contains(streamReceiver)){ // inefficient for large lists. Maybe use a HashMap...
			addStreamReceiver(streamReceiver);
		}
	}
	
	
	public void setStreamReceivers(final StreamReceiver receiver1, final StreamReceiver receiver2) {
		setStreamReceiver(receiver1);
		setStreamReceiver(receiver2);
	}
	
	public void addStreamReceiver(final StreamReceiver receiver){
		receivers.add(receiver);
	}
	
	public void removeStreamReceiver(final StreamReceiver receiver){
		receivers.remove(receiver);
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#startRecord(java.lang.String)
	 */
	@Override
	public void startRecord(final String identifier) {
		for (StreamReceiver receiver : receivers) {
			receiver.startRecord(identifier);
		}

	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#endRecord()
	 */
	@Override
	public void endRecord() {
		for (StreamReceiver receiver : receivers) {
			receiver.endRecord();
		}
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#startEntity(java.lang.String)
	 */
	@Override
	public void startEntity(final String name) {
		for (StreamReceiver receiver : receivers) {
			receiver.startEntity(name);
		}
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#endEntity()
	 */
	@Override
	public void endEntity() {
		for (StreamReceiver receiver : receivers) {
			receiver.endEntity();
		}
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#literal(java.lang.String, java.lang.String)
	 */
	@Override
	public void literal(final String name, final String value) {
		for (StreamReceiver receiver : receivers) {
			receiver.literal(name, value);
		}
	}



}
