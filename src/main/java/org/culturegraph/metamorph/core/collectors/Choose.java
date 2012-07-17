/**
 * 
 */
package org.culturegraph.metamorph.core.collectors;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.util.Util;

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
//	private String defaultValue;

	
	/**
	 * @param metamorph
	 */
	public Choose(final Metamorph metamorph) {
		super(metamorph);
		setNamedValueReceiver(metamorph);
	}
	
	//public void setDefaultValue(final String defaultValue) {
	//	this.defaultValue = defaultValue;
//	}


	@Override
	protected void emit() {
		if(!isEmpty()){
			getNamedValueReceiver().receive(Util.fallback(getName(), name), Util.fallback(getValue(), value), this, getRecordCount(), getEntityCount());
		}
		clear();
	}


	private boolean isEmpty() {
		return name==null;
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
