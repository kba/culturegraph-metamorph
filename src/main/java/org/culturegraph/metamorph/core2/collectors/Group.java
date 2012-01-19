package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.core2.Metamorph;
import org.culturegraph.metamorph.core2.NamedValueSource;

/**
 * Implementation of the <code>&lt;group&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Group extends AbstractCollect{


	public Group(final Metamorph metamorph) {
		super(metamorph);
	}
	
	private static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}


	@Override
	protected void receive(final String recName, final String recValue, final NamedValueSource source) {
		getNamedValueReceiver().receive(fallback(getName(), recName), fallback(getValue(), recValue), this, getRecordCount(), getEntityCount());
	}

	@Override
	protected boolean isComplete() {
		return false; //nothing
	}

	@Override
	protected void clear() {
		//nothing
	}

	@Override
	protected void emit() {
		//nothing
	}
}
