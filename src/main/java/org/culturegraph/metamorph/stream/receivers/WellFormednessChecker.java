/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * A stream receiver that throws an exception if the 
 * stream event methods are called in an invalid order.
 * Additionally, the stream receiver checks that entity
 * and literal names are not null.
 * 
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class WellFormednessChecker implements StreamReceiver {
	
	private static final String NAME_MUST_NOT_BE_NULL = "name must not be null";
	
	private static final String NOT_IN_RECORD = "Not in record";
	private static final String NOT_IN_ENTITY = "Not in entity";
	private static final String IN_ENTITY = "In entity";
	private static final String IN_RECORD = "In record";
	
	private int nestingLevel;

	public WellFormednessChecker() {
		reset();
	}
	
	public void reset() {
		nestingLevel = 0;
	}
	
	public void startStream() {
		reset();
	}
	
	public void endStream() {
		if (nestingLevel > 0) {
			throw new IllegalStateException(IN_RECORD);
		}
	}
	
	@Override
	public void startRecord(final String identifier) {
		if (nestingLevel > 0) {
			throw new IllegalStateException(IN_RECORD);
		}
		nestingLevel += 1;
	}

	@Override
	public void endRecord() {
		if (nestingLevel < 1) { 
			throw new IllegalStateException(NOT_IN_RECORD);
		} else if (nestingLevel > 1) {
			throw new IllegalStateException(IN_ENTITY);			
		}
		nestingLevel -= 1;
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
	}

	@Override
	public void endEntity() {
		if (nestingLevel < 2) {
			throw new IllegalStateException(NOT_IN_ENTITY);
		}
		nestingLevel -= 1;
	}

	@Override
	public void literal(final String name, final String value) {
		if (name == null) {
			throw new IllegalArgumentException(NAME_MUST_NOT_BE_NULL);
		}
		if (nestingLevel < 1) {
			throw new IllegalStateException(NOT_IN_RECORD);
		}
	}

}
