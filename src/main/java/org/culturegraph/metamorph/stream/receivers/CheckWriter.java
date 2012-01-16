/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import java.util.ArrayList;
import java.util.List;
import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * 
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class CheckWriter implements StreamReceiver {

	/*
	 * Error messages:
	 */
	private static final String NAME_MUST_NOT_BE_NULL = 
			"name must not be null";
	
	private static final String NO_RECORD_FOUND = "No record found";
	private static final String NO_ENTITY_FOUND = "No entity found";
	private static final String NO_LITERAL_FOUND = "No literal found";
	
	private static final String NOT_IN_RECORD = "Not in record";
	private static final String NOT_IN_ENTITY = "Not in entity";
	private static final String STILL_IN_ENTITY = "Still in entity";
	private static final String ALREADY_IN_RECORD = "Already in record";
	
	private static final String OPEN_RECORD_EXISTS = "Open record exists";
	private static final String UNCONSUMED_RECORDS_EXIST = 
			"Unconsumed records exist";
	
	private static final String NOT_IN_COLLECTING_MODE = "Not in collecting mode";
	private static final String NOT_IN_CHECKING_MODE = "Not in checking mode";
	private static final String NOT_IN_COLLECTING_OR_CHECKING_MODE = 
			"Not in collecting or checking mode";
	private static final String CANNOT_CHANGE_STRICT_ORDER_SETTING_WHILE_CHECKING = 
			"Cannot change strict-order setting while checking";

	private static class Event {
		public enum Type {
			START_RECORD, END_RECORD, START_ENTITY, END_ENTITY, LITERAL
		}
		
		public enum State {
			AVAILABLE, CONSUMED, ACTIVE_GROUP, SUSPENDED_GROUP
		}
		
		private final Type type;
		private final String name;
		private final String value;
		
		private State state;
		
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
			this.state = State.AVAILABLE;
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

		public State getState() {
			return state;
		}
		
		public void setState(final State s) {
			state = s;
		}

		@Override
		public String toString() {
			String str = type.toString();
			if (name != null) {
				str += ": " + name;
				if (value != null) {
					str += "=" + value;
				}
			}
			str += " (" + state.toString() + ")";
			return str;
		}
	}
	
	public static enum Mode {
		COLLECTING, CHECKING, DONE
	}
	
	private boolean strictRecordOrder;
	private boolean strictKeyOrder;
	private boolean strictValueOrder;
	
	private Mode mode;	
	private int nestingLevel;
	private final List<Event> events = new ArrayList<Event>();
	
	public CheckWriter() {
		reset();
	}
	
	public boolean isStrictRecordOrder() {
		return strictRecordOrder;
	}
	
	public void setStrictRecordOrder(final boolean strict) {
		if (mode == Mode.CHECKING) {
			throw new IllegalStateException(CANNOT_CHANGE_STRICT_ORDER_SETTING_WHILE_CHECKING);
		}
		
		strictRecordOrder = strict;
	}
	
	public boolean isStrictKeyOrder() {
		return strictKeyOrder;
	}
	
	public void setStrictKeyOrder(final boolean strict) {
		if (mode == Mode.CHECKING) {
			throw new IllegalStateException(CANNOT_CHANGE_STRICT_ORDER_SETTING_WHILE_CHECKING);
		}
		
		strictKeyOrder = strict;
	}
	
	public boolean isStrictValueOrder() {
		return strictValueOrder;
	}
	
	public void setStrictValueOrder(final boolean strict) {
		if (mode == Mode.CHECKING) {
			throw new IllegalStateException(CANNOT_CHANGE_STRICT_ORDER_SETTING_WHILE_CHECKING);
		}
		
		strictValueOrder = strict;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void reset() {
		mode = Mode.COLLECTING;
		nestingLevel = 0;
		events.clear();
	}
	
	public void startChecking() {
		if (mode != Mode.COLLECTING) {
			throw new IllegalStateException(NOT_IN_COLLECTING_MODE);
		}
		if (nestingLevel != 0) {
			throw new IllegalStateException(OPEN_RECORD_EXISTS);
		}
		
		mode = Mode.CHECKING;
		nestingLevel = 0;
		for (Event ev: events) {
			ev.setState(Event.State.AVAILABLE);
		}
	}
	
	public void endChecking() {
		if (mode != Mode.CHECKING) {
			throw new IllegalStateException(NOT_IN_CHECKING_MODE);
		}
		if (nestingLevel != 0) {
			throw new IllegalStateException(OPEN_RECORD_EXISTS);
		}
		
		mode = Mode.DONE;
		
		for (Event ev: events) {
			if (ev.getState() != Event.State.CONSUMED) {
				throw new IllegalStateException(UNCONSUMED_RECORDS_EXIST);
			}
		}
	}
	
	@Override
	public void startRecord(final String identifier) {
		if (nestingLevel != 0) {
			throw new IllegalStateException(ALREADY_IN_RECORD);
		}
		
		nestingLevel += 1;
		switch (mode) {
		case COLLECTING:
			events.add(new Event(Event.Type.START_RECORD, identifier));
			break;
		case CHECKING:
			checkStartRecord(identifier);
			break;
		case DONE:
			throw new IllegalStateException(NOT_IN_COLLECTING_OR_CHECKING_MODE);
		}
	}

	@Override
	public void endRecord() {
		if (nestingLevel == 0) { 
			throw new IllegalStateException(NOT_IN_RECORD);
		} else if (nestingLevel != 1) {
			throw new IllegalStateException(STILL_IN_ENTITY);			
		}
		
		nestingLevel -= 1;
		switch (mode) {
		case COLLECTING:
			events.add(new Event(Event.Type.END_RECORD));
			break;
		case CHECKING:
			checkEndRecord();
			break;
		case DONE:
			throw new IllegalStateException(NOT_IN_COLLECTING_OR_CHECKING_MODE);
		}
	}

	@Override
	public void startEntity(final String name) {
		if (name == null) {
			throw new IllegalArgumentException(NAME_MUST_NOT_BE_NULL);
		}
		if (nestingLevel < 1) {
			throw new IllegalStateException(NOT_IN_RECORD);
		}
		
		nestingLevel += 1;
		switch (mode) {
		case COLLECTING:
			events.add(new Event(Event.Type.START_ENTITY, name));
			break;
		case CHECKING:
			checkStartEntity(name);
			break;
		case DONE:
			throw new IllegalStateException(NOT_IN_COLLECTING_OR_CHECKING_MODE);
		}
	}

	@Override
	public void endEntity() {
		if (nestingLevel < 2) {
			throw new IllegalStateException(NOT_IN_ENTITY);
		}
		
		nestingLevel -= 1;
		switch (mode) {
		case COLLECTING:
			events.add(new Event(Event.Type.END_ENTITY));
			break;
		case CHECKING:
			checkEndEntity();
			break;
		case DONE:
			throw new IllegalStateException(NOT_IN_COLLECTING_OR_CHECKING_MODE);
		}
	}

	@Override
	public void literal(final String name, final String value) {
		if (name == null) {
			throw new IllegalArgumentException(NAME_MUST_NOT_BE_NULL);
		}
		if (nestingLevel < 1) {
			throw new IllegalStateException(NOT_IN_RECORD);
		}
		
		switch (mode) {
		case COLLECTING:
			events.add(new Event(Event.Type.LITERAL, name, value));
			break;
		case CHECKING:
			checkLiteral(name, value);
			break;
		case DONE:
			throw new IllegalStateException(NOT_IN_COLLECTING_OR_CHECKING_MODE);
		}
	}

	private void checkStartRecord(final String identifier) {
		boolean recordFound = false;
		for (Event ev: events) {
			if (ev.getType() == Event.Type.START_RECORD) {
				if (ev.getState() == Event.State.AVAILABLE) {
					if (compare(ev.getName(), identifier)) {
						ev.setState(Event.State.ACTIVE_GROUP);
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
	
	private void checkEndRecord() {
		Event activeGroup = null;
		boolean consumed = true;
		boolean recordFound = false;
		for (Event ev: events) {
			if (activeGroup != null) {
				switch(ev.getType()) {
				case START_ENTITY:
				case END_ENTITY:
				case LITERAL:
					consumed = consumed && (ev.getState() == Event.State.CONSUMED);
					break;		
				case END_RECORD:
					if (!recordFound && consumed) {
						activeGroup.setState(Event.State.CONSUMED);
						ev.setState(Event.State.CONSUMED);
						recordFound = true;							
					} else {
						setAvailable(activeGroup);					
					}
					activeGroup = null;
					break;
				case START_RECORD:  // Do nothing
				}
			} else {
				if (ev.getState() == Event.State.ACTIVE_GROUP) {
					activeGroup = ev;
					consumed = true;
				}
			}
		}
		
		if (!recordFound) {
			throw new IllegalStateException(NO_RECORD_FOUND);
		}		
	}
	
	private void checkStartEntity(final String name) {
		int level = 0;
		Event activeGroup = null;
		boolean foundInGroup = false;
		boolean strictFailed = false;
		boolean foundAnywhere = false;
		for (Event ev: events) {
			if (activeGroup != null) {
				switch(ev.getType()) {
				case LITERAL:
					if (level == 0 && ev.getState() == Event.State.AVAILABLE && !foundInGroup) {
						if (compare(ev.getName(), name)) {
							strictFailed = strictFailed || strictValueOrder;
						} else {
							strictFailed = strictFailed || strictKeyOrder;
						}
					}
					break;
				case START_ENTITY:
					if (level == 0 && ev.getState() == Event.State.AVAILABLE && !strictFailed) {
						if(compare(ev.getName(), name)) {
							ev.setState(Event.State.ACTIVE_GROUP);
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
							activeGroup.setState(Event.State.SUSPENDED_GROUP);
						} else {
							setAvailable(activeGroup);
						}
						activeGroup = null;
					}	
					break;
				case START_RECORD:  // Do nothing
				}	
			} else {
				if (ev.getState() == Event.State.ACTIVE_GROUP) {
					activeGroup = ev;
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
	
	private void checkEndEntity() {
		int level = 0;
		Event parentGroup = null;
		Event activeGroup = null;
		boolean consumed = true;
		boolean foundInGroup = false;
		boolean strictFailed = false;
		boolean foundAnywhere = false;
		for (Event ev: events) {
			if (activeGroup != null) {
				switch(ev.getType()) {
				case START_ENTITY:
					level += 1;
					//$FALL-THROUGH$, fallsthrough
				case LITERAL:
					consumed = consumed && (ev.getState() == Event.State.CONSUMED);
					break;
				case END_RECORD:
				case END_ENTITY:
					if (level > 0) {
						consumed = consumed && (ev.getState() == Event.State.CONSUMED);
						level -= 1;
					} else {
						if (!foundInGroup && !strictFailed) {
							if (consumed) {
								activeGroup.setState(Event.State.CONSUMED);
								ev.setState(Event.State.CONSUMED);
								foundInGroup = true;
								foundAnywhere = true;
							} else {
								setAvailable(activeGroup);
								strictFailed = strictFailed || strictValueOrder;
							}
						} else {
							setAvailable(activeGroup);
						}
						parentGroup.setState(Event.State.ACTIVE_GROUP);
						activeGroup = null;
					}	
					break;
				case START_RECORD:  // Do Nothing
				}
			} else {
				if (ev.getState() == Event.State.ACTIVE_GROUP) {
					activeGroup = ev;
					level = 0;
					consumed = true;
				} else if (ev.getState() == Event.State.SUSPENDED_GROUP) {
					parentGroup = ev;
					foundInGroup = false;
				}
			}
		}
		
		if (!foundAnywhere) {
			throw new IllegalStateException(NO_ENTITY_FOUND);
		}		
	}
	
	private void checkLiteral(final String name, final String value) {
		int level = 0;
		Event activeGroup = null;
		boolean foundInGroup = false;
		boolean strictFailed = false;
		boolean foundAnywhere = false;
		for (Event ev: events) {
			if (activeGroup != null) {
				switch(ev.getType()) {
				case START_ENTITY:
					if (level == 0 && ev.getState() == Event.State.AVAILABLE && !foundInGroup) {
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
					if (level == 0 && ev.getState() == Event.State.AVAILABLE && !foundInGroup) {
						if (compare(ev.getName(), name)) {
							if (compare(ev.getValue(), value)) {
								ev.setState(Event.State.CONSUMED);
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
				if (ev.getState() == Event.State.ACTIVE_GROUP) {
					activeGroup = ev;
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
	
	private void setAvailable(final Event parent) {
		int level = -1;	
		for (Event ev: events) {
			if (ev == parent || level >= 0) {
				ev.setState(Event.State.AVAILABLE);
				
				switch(ev.getType()) {
				case START_RECORD:
				case START_ENTITY:
					level += 1;
					break;
				case END_RECORD:
				case END_ENTITY:
					level -= 1;
					break;
				case LITERAL:  // Do nothing
				}
			}
		}
	}
}
