package org.culturegraph.metamorph.stream.receivers;

import java.io.PrintWriter;
import java.io.Writer;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Simple {@link StreamReceiver} for debugging. Formats the received messages,
 * counts the records, and prints everything to the console.
 * 
 * @author Markus Michael Geipel
 */
public final class DefaultWriter implements StreamReceiver {

	private static final String PREFIX = " ";
	private static final char INDENT_CHAR = '\t';

	private final StringBuilder indentBuilder = new StringBuilder(PREFIX);
	private String indent = PREFIX;
	private final PrintWriter out;
	private int count;

	public DefaultWriter(final Writer writer) {
			out = new PrintWriter(writer);
	}
	
	@Override
	public void startRecord(final String identifier) {
		++count;
		out.println("RECORD " + count + ": " + identifier);
	}

	@Override
	public void endRecord() {
		out.println();
		out.flush();
	}

	@Override
	public void startEntity(final String name) {
		out.println(indent + "> " + name);
		indentBuilder.append(INDENT_CHAR);
		indent = indentBuilder.toString();
	}

	@Override
	public void endEntity() {
		indentBuilder.deleteCharAt(indentBuilder.length() - 1);
		indent = indentBuilder.toString();
	}

	@Override
	public void literal(final String name, final String value) {
		out.println(indent + name + " = " + value);
	}

	public void flush() {
		out.flush();
	}
}
