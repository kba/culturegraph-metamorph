/**
 * 
 */
package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.util.StringUtil;

/**
 * Corresponds to the <code>&lt;choose-literal&gt;</code> tag.
 * 
 * @author Christoph BÃ¶hme <c.boehme@dnb.de>
 *
 */
final class ChooseLiteral extends AbstractCollect {

	private String value;
	private String name;
	private int priority = Integer.MAX_VALUE;
	private final Map<String, Integer> priorities = new HashMap<String, Integer>();
	private int nextPriority;
	
	/**
	 * @param metamorph
	 */
	public ChooseLiteral(final Metamorph metamorph) {
		super(metamorph);
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.core.AbstractCollect#emit()
	 */
	@Override
	protected void emit() {
		getMetamorph().data(StringUtil.fallback(getName(), name), StringUtil.fallback(getValue(), value), getRecordCount(), getEntityCount());
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.core.AbstractCollect#isComplete()
	 */
	@Override
	protected boolean isComplete() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.core.AbstractCollect#clear()
	 */
	@Override
	protected void clear() {
		value = null;
		name = null;
		priority = Integer.MAX_VALUE;
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.core.AbstractCollect#receive(java.lang.String, java.lang.String)
	 */
	@Override
	protected void receive(final String name, final String value) {
		final Integer thisPriority = priorities.get(name);
		
		if (thisPriority != null && thisPriority.intValue() <= priority) {
			this.value = value;
			this.name = name;
			this.priority = thisPriority.intValue();
		}
	}

	@Override
	protected void onAddData(final Data data) {
		priorities.put(data.getDefaultName(), Integer.valueOf(nextPriority));
		nextPriority += 1;
	}
}
