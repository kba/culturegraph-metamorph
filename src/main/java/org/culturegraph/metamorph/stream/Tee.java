/**
 * 
 */
package org.culturegraph.metamorph.stream;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public class Tee implements StreamReceiver {

	private StreamReceiver receiver1;
	private StreamReceiver receiver2;

	public void setStreamReceivers(StreamReceiver receiver1, StreamReceiver receiver2) {
		this.receiver1 = receiver1;
		this.receiver2 = receiver2;
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#startRecord(java.lang.String)
	 */
	@Override
	public void startRecord(String identifier) {
		receiver1.startRecord(identifier);
		receiver2.startRecord(identifier);
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#endRecord()
	 */
	@Override
	public void endRecord() {
		receiver1.endRecord();
		receiver2.endRecord();
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#startEntity(java.lang.String)
	 */
	@Override
	public void startEntity(String name) {
		receiver1.startEntity(name);
		receiver2.startEntity(name);
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#endEntity()
	 */
	@Override
	public void endEntity() {
		receiver1.endEntity();
		receiver2.endEntity();
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.stream.StreamReceiver#literal(java.lang.String, java.lang.String)
	 */
	@Override
	public void literal(String name, String value) {
		receiver1.literal(name, value);
		receiver2.literal(name, value);
	}

}
