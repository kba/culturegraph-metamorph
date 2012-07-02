package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.AbstractNamedValuePipeHead;
import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * Common basis for {@link Entity}, {@link Combine} etc.
 * 
 * @author Markus Michael Geipel

 */
public abstract class AbstractCollect extends AbstractNamedValuePipeHead implements Collect {

//	private static final Logger LOG = LoggerFactory.getLogger(AbstractCollect.class);

	private int oldRecord;
	private int oldEntity;
	private boolean resetAfterEmit;
	private boolean sameEntity;
	private String name;
	private String value;
	private final Metamorph metamorph;
	private boolean waitForFlush;


	public AbstractCollect(final Metamorph metamorph) {
		super();
		this.metamorph = metamorph;
	}
	
	protected final Metamorph getMetamorph(){
		return metamorph;
	}

	protected final int getRecordCount() {
		return oldRecord;
	}

	protected final int getEntityCount() {
		return oldEntity;
	}

	@Override
	public final void setFlushWith(final String flushEntity) {
		waitForFlush = true;
		metamorph.addEntityEndListener(this, flushEntity);
	}


	
	@Override
	public final void setSameEntity(final boolean sameEntity) {
		this.sameEntity = sameEntity;
	}


	@Override
	public final void setReset(final boolean reset) {
		this.resetAfterEmit = reset;
	}


	
	@Override
	public final String getName() {
		return name;
	}

	
	@Override
	public final void setName(final String name) {
		this.name = name;
	}


	public final String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}


	private void updateCounts(final int currentRecord, final int currentEntity) {
		if (!isSameRecord(currentRecord)) {
			clear();
			oldRecord = currentRecord;
		}
		if (resetNeedFor(currentEntity)) {
			clear();
			oldEntity = currentEntity;
		}
	}

	private boolean resetNeedFor(final int currentEntity) {
		return sameEntity && oldEntity != currentEntity;
	}

	private boolean isSameRecord(final int currentRecord) {
		return currentRecord == oldRecord;
	}

	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		updateCounts(recordCount, entityCount);

		receive(name, value, source);

		if (!waitForFlush && isComplete()) {
			emit();
			if (resetAfterEmit) {
				clear();
			}
		}
	}


	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		// nothing
	}

	@Override
	public final void onEntityEnd(final String entityName, final int recordCount, final int entityCount) {
		if (isSameRecord(recordCount) && sameEntityConstraintSatisfied(entityCount)) {
			emit();
			if (resetAfterEmit) {
				clear();
			}
		}
	}
	
	private boolean sameEntityConstraintSatisfied(final int entityCount) {
		return !sameEntity || oldEntity == entityCount;
	}

	protected abstract void receive(final String name, final String value, final NamedValueSource source);
	protected abstract boolean isComplete();
	protected abstract void clear();
	protected abstract void emit();

	
}
