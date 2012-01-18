/**
 * 
 */
package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.culturegraph.metamorph.core.Data.Mode;
import org.culturegraph.metamorph.functions.Function;
import org.culturegraph.metamorph.util.StringUtil;

/**
 * Corresponds to the <code>&lt;choose-literal&gt;</code> tag.
 * 
 * @author Christoph BÃ¶hme <c.boehme@dnb.de>
 *
 */
final class ChooseLiteral extends AbstractCollect implements ValueProcessor, NamedValueSource{

	private String value;
	private String name;
	private int priority = Integer.MAX_VALUE;
	private final Map<String, Integer> priorities = new HashMap<String, Integer>();
	private final ValueProcessorImpl valueProcessor = new ValueProcessorImpl();
	private int nextPriority;
	private NamedValueReceiver namedValueReceiver;
	
	/**
	 * @param metamorph
	 */
	public ChooseLiteral() {
		super();
	}

	public ChooseLiteral(final NamedValueReceiver metamorph) {
		super();
		setNamedValueReceiver(metamorph);
	}

	/* (non-Javadoc)
	 * @see org.culturegraph.metamorph.core.AbstractCollect#emit()
	 */
	@Override
	protected void emit() {
		value = valueProcessor.applyFunctions(value);
		if (value == null) {
			return;
		}
		namedValueReceiver.receive(StringUtil.fallback(getName(), name), StringUtil.fallback(getValue(), value), getRecordCount(), getEntityCount());
		clear();
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
	protected void onAddData(final NamedValueSource namedValue) {
		if (namedValue.getName() == null) {
			namedValue.setName( String.valueOf(UUID.randomUUID()));
		}
		priorities.put(namedValue.getName(), Integer.valueOf(nextPriority));
		nextPriority += 1;
	}
	
	@Override
	public void addFunction(final Function function) {
		valueProcessor.addFunction(function);
	}

	@Override
	public void setNamedValueReceiver(final NamedValueReceiver namedValueReceiver) {
		this.namedValueReceiver = namedValueReceiver;
		
	}
}
