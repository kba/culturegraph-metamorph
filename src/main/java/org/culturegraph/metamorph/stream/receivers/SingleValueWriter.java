package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.stream.StreamReceiver;

public final class SingleValueWriter implements StreamReceiver {

	private String value = "";

	@Override
	public void startRecord(final String identifier) {
		this.setValue("");

	}

	@Override
	public void endRecord() {
		// nothing to do
	}

	@Override
	public void startEntity(final String name) {
		// nothing to do
	}

	@Override
	public void endEntity() {
		// nothing to do
	}

	@Override
	public void literal(final String name, final String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	private void setValue(final String value) {
		this.value = value;
	}

}
