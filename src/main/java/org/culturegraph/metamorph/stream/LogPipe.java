package org.culturegraph.metamorph.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogPipe implements StreamReceiver, StreamSender{
	private static final Logger LOG = LoggerFactory.getLogger(LogPipe.class);
	
	private StreamReceiver receiver;
	
	
	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		this.receiver = streamReceiver;
	}

	@Override
	public void startRecord(final String identifier) {
		LOG.info("start record " + identifier);
		receiver.startRecord(identifier);
	}

	@Override
	public void endRecord() {
		LOG.info("end record");
		receiver.endRecord();
	}

	@Override
	public void startEntity(final String name) {
		LOG.info("start entity " + name);
		receiver.startEntity(name);
	}

	@Override
	public void endEntity() {
		LOG.info("end entity");
		receiver.endEntity();
		
	}

	@Override
	public void literal(final String name, final String value) {
		LOG.info("literal: " + name + "=" + value);
		receiver.literal(name, value);
	}

}
