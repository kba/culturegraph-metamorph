package org.culturegraph.metamorph.stream;

/**
 * Counts the number of records and fields read. Used mainly for test cases and
 * debugging.
 * 
 * @author Markus Michael Geipel
 */
public final class CountingStreamReceiver extends DefaultStreamReceiver {
	private int numRecords;
	private int numEntities;
	private int numLiterals;

	@Override
	public void startRecord(final String identifier) {
		++numRecords;
	}

	@Override
	public void startEntity(final String name) {
		++numEntities;
	}

	@Override
	public void literal(final String name, final String value) {
		++numLiterals;
	}

	/**
	 * @return the numRecords
	 */
	public int getNumRecords() {
		return numRecords;
	}

	/**
	 * @return the numEntities
	 */
	public int getNumEntities() {
		return numEntities;
	}

	/**
	 * @return the numLiterals
	 */
	public int getNumLiterals() {
		return numLiterals;
	}
	
	
}
