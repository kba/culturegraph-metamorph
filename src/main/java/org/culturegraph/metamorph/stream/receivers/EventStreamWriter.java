/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.culturegraph.metamorph.stream.StreamReceiver;

public final class EventStreamWriter implements StreamReceiver {


	public static final class Event {
		public enum Type {
			START_RECORD, END_RECORD, START_ENTITY, END_ENTITY, LITERAL
		}
		
		private final Type type;
		private final String name;
		private final String value;
		
		public Event(final Type type) {
			this(type, null);
		}
		
		public Event(final Type type, final String name) {
			this(type, name, null);
		}
		
		public Event(final Type type, final String name, final String value) {
			this.type = type;
			this.name = name;
			this.value = value;
		}

		public Type getType() {
			return type;
		}
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(type);
			if (name != null) {
				builder.append("(" );
				builder.append(name);
				if (value != null) {
					builder.append("=");
					builder.append(value);
				}
				builder.append(")");
			}
			return builder.toString();
		}
	}
	
	private final List<Event> eventStream = new ArrayList<Event>();

	private final WellFormednessChecker wellFormednessChecker = 
			new WellFormednessChecker();
	

	
	public List<Event> getEventStream() {
		return Collections.unmodifiableList(eventStream);
	}

	public void resetStream() {
		wellFormednessChecker.resetStream();
		eventStream.clear();
	}
	
	public void endStream() {
		wellFormednessChecker.endStream();
	}
	
	@Override
	public void startRecord(final String identifier) {
		wellFormednessChecker.startRecord(identifier);
		eventStream.add(new Event(Event.Type.START_RECORD, identifier));
	}

	@Override
	public void endRecord() {
		wellFormednessChecker.endRecord();
		eventStream.add(new Event(Event.Type.END_RECORD));
	}

	@Override
	public void startEntity(final String name) {
		wellFormednessChecker.startEntity(name);
		eventStream.add(new Event(Event.Type.START_ENTITY, name));
	}

	@Override
	public void endEntity() {
		wellFormednessChecker.endEntity();
		eventStream.add(new Event(Event.Type.END_ENTITY));
	}

	@Override
	public void literal(final String name, final String value) {
		wellFormednessChecker.literal(name, value);
		eventStream.add(new Event(Event.Type.LITERAL, name, value));
	}
}