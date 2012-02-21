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
public final class Tee implements StreamPipe {

	private final List<StreamReceiver> receivers = new ArrayList<StreamReceiver>();
	
	@Override
	public <R extends StreamReceiver> R setReceiver(final R receiver) {
		if(!receivers.contains(receiver)){ // inefficient for large lists. Maybe use a HashSet...
			addReceiver(receiver);
		}
		return receiver;
	}
	
	
	/**
	 * sets both receivers and returns the first
	 * @param receiver
	 * @param lateralReceiver
	 * @return the parameter 'receiver'
	 * 	 
	 */
	public <R extends StreamReceiver> R setReceivers(final R receiver, final StreamReceiver lateralReceiver) {
		setReceiver(receiver);
		setReceiver(lateralReceiver);
		return receiver;
	}
	
	/**
	 * adds receiver even if receiver is already added.
	 * @param receiver
	 * @return this
	 */
	public Tee addReceiver(final StreamReceiver receiver){
		receivers.add(receiver);
		return this;
	}
	
	
	public void removeReceiver(final StreamReceiver receiver){
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
