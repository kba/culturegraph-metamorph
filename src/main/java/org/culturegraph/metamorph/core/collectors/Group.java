package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metamorph.util.StringUtil;

/**
 * Implementation of the <code>&lt;group&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Group extends AbstractCollect{


	public Group(final Metamorph metamorph) {
		super(metamorph);
	}
	
	@Override
	protected void receive(final String recName, final String recValue, final NamedValueSource source) {
		getNamedValueReceiver().receive(StringUtil.fallback(getName(), recName), StringUtil.fallback(getValue(), recValue), this, getRecordCount(), getEntityCount());
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
