/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.stream.receivers.EventStreamWriter.Event;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class EventStreamValidator implements StreamReceiver {

	private static final String CANNOT_CHANGE_OPTIONS = "Cannot change options during validation";
	private static final String VALIDATION_FAILED = "Validation failed. Please reset the validator";
	
	private static final String NO_RECORD_FOUND = "No record found: ";
	private static final String NO_ENTITY_FOUND = "No entity found: ";
	private static final String NO_LITERAL_FOUND = "No literal found: ";
	private static final String UNCONSUMED_RECORDS_FOUND = "Unconsumed records found";
	
	private static final class EventNode {
		private final Event event;
		private final EventNode parent;
		private final List<EventNode> children;
		
		private boolean consumed;
		
		public EventNode(final Event event, final EventNode parent) {
			this.event = event;
			this.parent = parent;
			// The null-event is used to indicate the stream-start:
			if (this.event == null || 
					this.event.getType() == Event.Type.START_RECORD ||
					this.event.getType() == Event.Type.START_ENTITY) {
				children = new LinkedList<EventNode>();
			} else {
				children = null;
			}
			
			consumed = false;
		}

		public Event getEvent() {
			return event;
		}

		public EventNode getParent() {
			return parent;
		}

		public List<EventNode> getChildren() {
			return children;
		}

		public boolean isConsumed() {
			return consumed;
		}

		public void setConsumed(final boolean consumed) {
			this.consumed = consumed;
		}
	}
	
	private EventNode eventStream;
	private Stack<List<EventNode>> stack = new Stack<List<EventNode>>();
	private boolean validating;
	private boolean validationFailed;

	private boolean strictRecordOrder;
	private boolean strictKeyOrder;
	private boolean strictValueOrder;
	
	private final WellFormednessChecker wellFormednessChecker = 
			new WellFormednessChecker();
	
	public EventStreamValidator(final List<Event> eventStream) {
		this.eventStream = new EventNode(null, null);
		foldEventStream(this.eventStream, eventStream.iterator());
		
		resetStream();
	}

	public boolean isStrictRecordOrder() {
		return strictRecordOrder;
	}

	public void setStrictRecordOrder(final boolean strictRecordOrder) {
		if (validating) {
			throw new IllegalStateException(CANNOT_CHANGE_OPTIONS);
		}
		
		this.strictRecordOrder = strictRecordOrder;
	}

	public boolean isStrictKeyOrder() {
		return strictKeyOrder;
	}

	public void setStrictKeyOrder(final boolean strictKeyOrder) {
		if (validating) {
			throw new IllegalStateException(CANNOT_CHANGE_OPTIONS);
		}
		
		this.strictKeyOrder = strictKeyOrder;
	}

	public boolean isStrictValueOrder() {
		return strictValueOrder;
	}

	public void setStrictValueOrder(final boolean strictValueOrder) {
		if (validating) {
			throw new IllegalStateException(CANNOT_CHANGE_OPTIONS);
		}
		
		this.strictValueOrder = strictValueOrder;
	}	
	
	public void resetStream() {
		wellFormednessChecker.resetStream();
		
		validating = false;
		validationFailed = false;
		
		stack.clear();
		stack.push(new LinkedList<EventNode>());
		stack.peek().add(eventStream);
	}
	
	public void endStream() {
		if (validationFailed) {
			throw new IllegalStateException(VALIDATION_FAILED);
		}

		wellFormednessChecker.endStream();
		
		validating = false;	
		
		stack.pop();
		
		if (isGroupConsumed(eventStream)) {
			eventStream.setConsumed(true);
		} else {
			validationFailed = true;
			throw new IllegalStateException(UNCONSUMED_RECORDS_FOUND);
		}
	}
	
	@Override
	public void startRecord(final String identifier) {
		if (validationFailed) {
			throw new IllegalStateException(VALIDATION_FAILED);
		}

		wellFormednessChecker.startRecord(identifier);
		
		validating = true;
		
		if (!openGroups(Event.Type.START_RECORD, identifier, strictRecordOrder, false)) {
			validationFailed = true;
			throw new IllegalStateException(NO_RECORD_FOUND + identifier);
		}
	}
	
	@Override
	public void endRecord() {
		if (validationFailed) {
			throw new IllegalStateException(VALIDATION_FAILED);
		}

		wellFormednessChecker.endRecord();
		
		if (!closeGroups()) {
			validationFailed = true;
			throw new IllegalStateException(NO_RECORD_FOUND + "No record matched the sequence of stream events");
		}
		
	}
	
	@Override
	public void startEntity(final String name) {
		if (validationFailed) {
			throw new IllegalStateException(VALIDATION_FAILED);
		}

		wellFormednessChecker.startEntity(name);
		
		if (!openGroups(Event.Type.START_ENTITY, name, strictKeyOrder, strictValueOrder)) {
			validationFailed = true;
			throw new IllegalStateException(NO_ENTITY_FOUND + name);
		}
	}
	
	@Override
	public void endEntity() {
		if (validationFailed) {
			throw new IllegalStateException(VALIDATION_FAILED);
		}

		wellFormednessChecker.endEntity();
		
		if (!closeGroups()) {
			validationFailed = true;
			throw new IllegalStateException(NO_ENTITY_FOUND + "No entity matched the sequence of stream events");
		}
	}
	
	@Override
	public void literal(final String name, final String value) {
		if (validationFailed) {
			throw new IllegalStateException(VALIDATION_FAILED);
		}
		
		wellFormednessChecker.literal(name, value);
		
		final List<EventNode> stackFrame = stack.peek();
		
		final Iterator<EventNode> it = stackFrame.iterator();
		while (it.hasNext()) {
			final EventNode g = it.next();
			if (!consumeLiteral(g, name, value)) {
				resetGroup(g);
				it.remove();
			}
		}
		
		if (stackFrame.size() == 0) {
			validationFailed = true;
			throw new IllegalStateException(NO_LITERAL_FOUND + name + "=" + value);
		}
	}
	
	private void foldEventStream(final EventNode parent, final Iterator<Event> eventStream) {
		while(eventStream.hasNext()) {
			final Event ev = eventStream.next();
			if (ev.getType() == Event.Type.LITERAL) {
				parent.getChildren().add(new EventNode(ev, parent));
			} else if (ev.getType() == Event.Type.START_RECORD || 
					ev.getType() == Event.Type.START_ENTITY) {
				final EventNode newNode = new EventNode(ev, parent);
				parent.getChildren().add(newNode);
				foldEventStream(newNode, eventStream);
			} else if (ev.getType() == Event.Type.END_RECORD ||
				ev.getType() == Event.Type.END_ENTITY) {
				return;
			}
		}
	}
	
	private boolean openGroups(final Event.Type type, final String name, 
			final boolean strictKeyOrder, final boolean strictValueOrder) {
		final List<EventNode> stackFrame = stack.peek();
		stack.push(new LinkedList<EventNode>());
		
		final Iterator<EventNode> it = stackFrame.iterator();
		while (it.hasNext()) {
			final EventNode g = it.next();
			if (!consumeGroups(g, type, name, strictKeyOrder, strictValueOrder)) {
				resetGroup(g);
				it.remove();
			}
		}
		
		return stackFrame.size() != 0;		
	}
	
	private boolean closeGroups() {
		EventNode lastMatchParent = null;
		final Iterator<EventNode> it = stack.pop().iterator();
		while (it.hasNext()) {
			final EventNode g = it.next();
			if (g.getParent() != lastMatchParent && isGroupConsumed(g)) {
				g.setConsumed(true);
				lastMatchParent = g.getParent();
			} else {
				resetGroup(g);
			}
		}
		
		return lastMatchParent != null;		
	}
	
	private boolean consumeGroups(final EventNode group, final Event.Type type, final String name, 
			final boolean strictKeyOrder, final boolean strictValueOrder) {
		boolean foundMatch = false;
		for (EventNode c: group.getChildren()) {
			if (!c.isConsumed()) {
				final Event ev = c.getEvent();
				if (compare(name, ev.getName())) {
					if (ev.getType() == type) {
						stack.peek().add(c);
						foundMatch = true;
					} else if (strictValueOrder) {
						break;
					}
				}
				if (strictKeyOrder) {
					break;
				}
			}
		}
		return foundMatch;
	}
	
	private boolean consumeLiteral(final EventNode group, final String name, final String value) {
		boolean foundMatch = false;
		for (EventNode c: group.getChildren()) {
			if (!c.isConsumed()) {
				final Event ev = c.getEvent();
				if (compare(name, ev.getName())) {
					if (ev.getType() == Event.Type.LITERAL && 
							compare(value, ev.getValue())) {
						c.setConsumed(true);
						foundMatch = true;
						break;
					} else if (strictValueOrder) {
						break;
					}
				} else if (strictKeyOrder) {
					break;
				}
			}
		}
		return foundMatch;		
	}
	
	
	private boolean isGroupConsumed(final EventNode group) {
		boolean consumed = true;
		for (EventNode c: group.getChildren()) {
			consumed = consumed && c.isConsumed();
		}
		return consumed;		
	}

	private void resetGroup(final EventNode group) {
		if (group.getChildren() != null) {
			for (EventNode c: group.getChildren()) {
				resetGroup(c);
				c.setConsumed(false);
			}
		}
	}
	
	private boolean compare(final String str1, final String str2) {
		if (str1 == null) {
			return str2 == null;
		}
		return str1.equals(str2); 
	}
}
