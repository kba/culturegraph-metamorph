/**
 * 
 */
package org.culturegraph.metamorph.core2.collectors;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core2.Metamorph;
import org.culturegraph.metamorph.core2.NamedValueSource;
import org.culturegraph.metamorph.util.StringUtil;

/**
 * Corresponds to the <code>&lt;choose-literal&gt;</code> tag.
 * 
 * @author Christoph Böhme <c.boehme@dnb.de>,  Markus Michael Geipel
 *
 */
public final class Choose extends AbstractCollect{

	private String value;
	private String name;
	private int priority = Integer.MAX_VALUE;
	private final Map<NamedValueSource, Integer> priorities = new HashMap<NamedValueSource, Integer>();
	private int nextPriority;

	
	/**
	 * @param metamorph
	 */
	public Choose(final Metamorph metamorph) {
		super(metamorph);
		setNamedValueReceiver(metamorph);
	}


	@Override
	protected void emit() {
		getNamedValueReceiver().receive(StringUtil.fallback(getName(), name), StringUtil.fallback(getValue(), value), this, getRecordCount(), getEntityCount());
		clear();
	}


	@Override
	protected boolean isComplete() {
		return false;
	}


	@Override
	protected void clear() {
		value = null;
		name = null;
		priority = Integer.MAX_VALUE;
	}


	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		final int sourcePriority = priorities.get(source).intValue();
		 
		if (sourcePriority < priority) {
			this.value = value;
			this.name = name;
			this.priority = sourcePriority;
		}
	}
	
	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		priorities.put(namedValueSource, Integer.valueOf(nextPriority));
		nextPriority += 1;
	}
}
