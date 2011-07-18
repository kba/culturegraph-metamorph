package org.culturegraph.metamorph.streamreceiver;

import java.io.PrintWriter;


/**
 * Simple {@link StreamReceiver} for debugging. Formats the received messages, counts
 * the records, and prints everything to the console.
 * 
 * @author Markus Michael Geipel
 */
public final class ConsoleWriter implements StreamReceiver {

	private static final String PREFIX = " ";
	private static final char INDENT = '\t';
	
	private final StringBuilder builder = new StringBuilder(PREFIX);
	private String indent = PREFIX;
	private final PrintWriter writer = new PrintWriter(System.out);
	private int count;
	
	@Override
	public void startRecord() {
		++count;
		writer.println("RECORD " + count);
	}

	@Override
	public void endRecord() {
		writer.println();
		writer.flush();
	}

	@Override
	public void startEntity(final String name) {
		writer.println(indent + "> "+ name);
		builder.append(INDENT);
		indent=builder.toString();
	}

	@Override
	public void endEntity() {
		builder.deleteCharAt(builder.length()-1);
		indent=builder.toString();
	}

	@Override
	public void literal(final String name, final String value) {
		writer.println(indent + name + " = " + value);
	}
}
