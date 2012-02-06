package org.culturegraph.metamorph.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Leaves the event stream untouched but logs it to the info log. The {@link StreamReceiver} may be <code>null</code>. 
 * In this case {@link LogPipe} behaves as a sink, just logging. 
 * 
 * @author Markus Michael Geipel
 *
 */
public final class LogPipe implements StreamPipe {
	private static final Logger LOG = LoggerFactory.getLogger(LogPipe.class);

	private StreamReceiver receiver;

	@Override
	public <R extends StreamReceiver> R  setReceiver(final R streamReceiver) {
		this.receiver = streamReceiver;
		return streamReceiver;
	}

	@Override
	public void startRecord(final String identifier) {
	
		LOG.debug("start record " + identifier);
		if (null != receiver) {
			receiver.startRecord(identifier);
		}
	}

	@Override
	public void endRecord() {

		LOG.debug("end record");
		if (null != receiver) {
			receiver.endRecord();
		}
	}

	@Override
	public void startEntity(final String name) {

		LOG.debug("start entity " + name);
		if (null != receiver) {
			receiver.startEntity(name);
		}
	}

	@Override
	public void endEntity() {
		LOG.debug("end entity");
		if (null != receiver) {
			receiver.endEntity();
		}

	}

	@Override
	public void literal(final String name, final String value) {
		LOG.debug("literal: " + name + "=" + value);
		if (null != receiver) {
			receiver.literal(name, value);
		}
	}

}
