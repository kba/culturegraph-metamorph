/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import java.util.ArrayList;
import java.util.List;

import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.stream.receivers.EventStreamWriter.Event;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class EventStreamValidator implements StreamReceiver {

	private static final String NO_RECORD_FOUND = "No record found";
	private static final String NO_ENTITY_FOUND = "No entity found";
	private static final String NO_LITERAL_FOUND = "No literal found";
	private static final String UNCONSUMED_RECORDS_FOUND = "Unconsumed records found";

	private enum State {
		AVAILABLE, CONSUMED, ACTIVE_GROUP, SUSPENDED_GROUP
	}
	
	private class EventState {
		private final Event event;
		private State state;
		
		public EventState(final Event event, final State state) {
			this.event = event;
			this.state = state;
		}
		
		public Event getEvent() {
			return event;
		}
		
		public State getState() {
			return state;
		}
		
		public void setState(final State state) {
			this.state = state;
		}
	}
	
	private final boolean strictRecordOrder;
	private final boolean strictKeyOrder;
	private final boolean strictValueOrder;
	
	private final List<EventState> eventStream = new ArrayList<EventState>();
	
	private final WellFormednessChecker wellFormednessChecker = 
			new WellFormednessChecker();
	
	public EventStreamValidator(final List<Event> eventStream) {
		this(eventStream, false);
	}
	
	public EventStreamValidator(final List<Event> eventStream, final boolean strictRecordOrder) {
		this(eventStream, strictRecordOrder, false);
	}
	
	public EventStreamValidator(final List<Event> eventStream, final boolean strictRecordOrder,
			final boolean strictKeyOrder) {
		this(eventStream, strictRecordOrder, strictKeyOrder, false);
	}
	
	public EventStreamValidator(final List<Event> eventStream, final boolean strictRecordOrder, 
			final boolean strictKeyOrder, final boolean strictValueOrder) {
		this.strictRecordOrder = strictRecordOrder;
		this.strictKeyOrder = strictKeyOrder;
		this.strictValueOrder = strictValueOrder;

		for (Event e: eventStream) {
			this.eventStream.add(new EventState(e, State.AVAILABLE));
		}
	}
	
	public void startStream() {
		wellFormednessChecker.startStream();
	}
	
	public void endStream() {
		wellFormednessChecker.endStream();
		
		for (EventState es: eventStream) {
			if (es.getState() != State.CONSUMED) {
				throw new IllegalStateException(UNCONSUMED_RECORDS_FOUND);
			}
		}
	}
	
	@Override
	public void startRecord(final String identifier) {
		wellFormednessChecker.startRecord(identifier);
		
		boolean recordFound = false;
		for (EventState es: eventStream) {
			final Event ev = es.getEvent();
			if (ev.getType() == Event.Type.START_RECORD) {
				if (es.getState() == State.AVAILABLE) {
					if (compare(ev.getName(), identifier)) {
						es.setState(State.ACTIVE_GROUP);
						recordFound = true;
					}
					if (strictRecordOrder) {
						break;
					}
				}
			}
		}
		if (!recordFound) {
			throw new IllegalStateException(NO_RECORD_FOUND);
		}
	}

	@Override
	public void endRecord() {
		wellFormednessChecker.endRecord();

		EventState activeGroup = null;
		boolean consumed = true;
		boolean recordFound = false;
		for (EventState es: eventStream) {
			if (activeGroup != null) {
				switch(es.getEvent().getType()) {
				case START_ENTITY:
				case END_ENTITY:
				case LITERAL:
					consumed = consumed && (es.getState() == State.CONSUMED);
					break;		
				case END_RECORD:
					if (!recordFound && consumed) {
						activeGroup.setState(State.CONSUMED);
						es.setState(State.CONSUMED);
						recordFound = true;							
					} else {
						setAvailable(activeGroup);					
					}
					activeGroup = null;
					break;
				case START_RECORD:  // Do nothing
				}
			} else {
				if (es.getState() == State.ACTIVE_GROUP) {
					activeGroup = es;
					consumed = true;
				}
			}
		}
		
		if (!recordFound) {
			throw new IllegalStateException(NO_RECORD_FOUND);
		}		
	}

	@Override
	public void startEntity(final String name) {
		wellFormednessChecker.startEntity(name);

		int level = 0;
		EventState activeGroup = null;
		boolean foundInGroup = false;
		boolean strictFailed = false;
		boolean foundAnywhere = false;
		for (EventState es: eventStream) {
			if (activeGroup != null) {
				final Event ev = es.getEvent();
				switch(ev.getType()) {
				case LITERAL:
					if (level == 0 && es.getState() == State.AVAILABLE && !foundInGroup) {
						if (compare(ev.getName(), name)) {
							strictFailed = strictFailed || strictValueOrder;
						} else {
							strictFailed = strictFailed || strictKeyOrder;
						}
					}
					break;
				case START_ENTITY:
					if (level == 0 && es.getState() == State.AVAILABLE && !strictFailed) {
						if(compare(ev.getName(), name)) {
							es.setState(State.ACTIVE_GROUP);
							foundInGroup = true;
							foundAnywhere = true;
						} else {
							if (!foundInGroup) {
								strictFailed = strictFailed || strictKeyOrder;
							}
						}
					}
					level += 1;
					break;
				case END_RECORD:
				case END_ENTITY:
					if (level > 0) {
						level -= 1;
					} else {
						if (foundInGroup) {
							activeGroup.setState(State.SUSPENDED_GROUP);
						} else {
							setAvailable(activeGroup);
						}
						activeGroup = null;
					}	
					break;
				case START_RECORD:  // Do nothing
				}	
			} else {
				if (es.getState() == State.ACTIVE_GROUP) {
					activeGroup = es;
					level = 0;
					foundInGroup = false;
					strictFailed = false;
				}
			}
		}
		
		if (!foundAnywhere) {
			throw new IllegalStateException(NO_ENTITY_FOUND);
		}		
	}

	@Override
	public void endEntity() {
		wellFormednessChecker.endEntity();

		int level = 0;
		EventState parentGroup = null;
		EventState activeGroup = null;
		boolean consumed = true;
		boolean foundInGroup = false;
		boolean strictFailed = false;
		boolean foundAnywhere = false;
		for (EventState es: eventStream) {
			if (activeGroup != null) {
				switch(es.getEvent().getType()) {
				case START_ENTITY:
					level += 1;
					//$FALL-THROUGH$, fallsthrough
				case LITERAL:
					consumed = consumed && (es.getState() == State.CONSUMED);
					break;
				case END_RECORD:
				case END_ENTITY:
					if (level > 0) {
						consumed = consumed && (es.getState() == State.CONSUMED);
						level -= 1;
					} else {
						if (!foundInGroup && !strictFailed) {
							if (consumed) {
								activeGroup.setState(State.CONSUMED);
								es.setState(State.CONSUMED);
								foundInGroup = true;
								foundAnywhere = true;
							} else {
								setAvailable(activeGroup);
								strictFailed = strictFailed || strictValueOrder;
							}
						} else {
							setAvailable(activeGroup);
						}
						parentGroup.setState(State.ACTIVE_GROUP);
						activeGroup = null;
					}	
					break;
				case START_RECORD:  // Do Nothing
				}
			} else {
				if (es.getState() == State.ACTIVE_GROUP) {
					activeGroup = es;
					level = 0;
					consumed = true;
				} else if (es.getState() == State.SUSPENDED_GROUP) {
					parentGroup = es;
					foundInGroup = false;
				}
			}
		}
		
		if (!foundAnywhere) {
			throw new IllegalStateException(NO_ENTITY_FOUND);
		}		
	}

	@Override
	public void literal(final String name, final String value) {
		wellFormednessChecker.literal(name, value);

		int level = 0;
		EventState activeGroup = null;
		boolean foundInGroup = false;
		boolean strictFailed = false;
		boolean foundAnywhere = false;
		for (EventState es: eventStream) {
			final Event ev = es.getEvent();
			if (activeGroup != null) {
				switch(ev.getType()) {
				case START_ENTITY:
					if (level == 0 && es.getState() == State.AVAILABLE && !foundInGroup) {
						if(compare(ev.getName(), name)) {
							strictFailed = strictFailed || strictValueOrder;
						} else {
							strictFailed = strictFailed || strictKeyOrder;
						}
					}
					level += 1;  
					break;
				case END_RECORD:
				case END_ENTITY:
					if (level > 0) {
						level -= 1;
					} else {
						if (!foundInGroup || strictFailed) {
							setAvailable(activeGroup);					
						}
						activeGroup = null;
					}
					break;
				case LITERAL:					
					if (level == 0 && es.getState() == State.AVAILABLE && !foundInGroup) {
						if (compare(ev.getName(), name)) {
							if (compare(ev.getValue(), value)) {
								es.setState(State.CONSUMED);
								foundInGroup = true;
								foundAnywhere = true;
							} else {
								strictFailed = strictFailed || strictValueOrder;
							}
						} else {
							strictFailed = strictFailed || strictKeyOrder;
						}
					}
					break;
				case START_RECORD:  // Do nothing
				}
			} else {
				if (es.getState() == State.ACTIVE_GROUP) {
					activeGroup = es;
					level = 0;
					foundInGroup = false;
					strictFailed = false;
				}
			}
		}
		
		if (!foundAnywhere) {
			throw new IllegalStateException(NO_LITERAL_FOUND);
		}		
	}

	private boolean compare(final String str1, final String str2) {
		if (str1 == null) {
			return str2 == null;
		}
		return str1.equals(str2); 
	}
	
	private void setAvailable(final EventState parent) {
		int level = -1;	
		for (EventState es: eventStream) {
			if (es == parent || level >= 0) {
				es.setState(State.AVAILABLE);
				
				switch(es.getEvent().getType()) {
				case START_RECORD:
				case START_ENTITY:
					level += 1;
					break;
				case END_RECORD:
				case END_ENTITY:
					level -= 1;
					break;
				case LITERAL:
					break;
				default:
					throw new NullPointerException("state must not be null");
				}
			}
		}
	}	
}
