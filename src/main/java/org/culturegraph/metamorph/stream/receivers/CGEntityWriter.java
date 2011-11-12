package org.culturegraph.metamorph.stream.receivers;

import java.io.PrintWriter;

import org.culturegraph.metamorph.stream.CGEntity;
import org.culturegraph.metamorph.stream.StreamReceiver;

public final class CGEntityWriter implements StreamReceiver {

	private final PrintWriter out;

	public CGEntityWriter(final java.io.Writer writer) {
		out = new PrintWriter(writer);
	}
	
	@Override
	public void startRecord(final String identifier) {
		out.append(identifier);
		out.append(CGEntity.FIELD_DELIMITER);
	}

	@Override
	public void endRecord() {
		out.println();
	}

	@Override
	public void startEntity(final String name) {
		out.append(CGEntity.ENTITY_START_MARKER);
		out.append(name);
		out.append(CGEntity.FIELD_DELIMITER);
	}

	@Override
	public void endEntity() {
		out.append(CGEntity.ENTITY_END_MARKER);
		out.append(CGEntity.FIELD_DELIMITER);
	}

	@Override
	public void literal(final String name, final String value) {
		out.append(CGEntity.LITERAL_MARKER);
		out.append(name);
		out.append(CGEntity.SUB_DELIMITER);
		out.append(value.replace(CGEntity.NEWLINE, CGEntity.NEWLINE_ESC));
		out.append(CGEntity.FIELD_DELIMITER);
	}

	public void flush() {
		out.flush();
	}
}