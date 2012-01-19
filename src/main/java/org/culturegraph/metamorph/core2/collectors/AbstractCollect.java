package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.core2.AbstractNamedValuePipe;
import org.culturegraph.metamorph.core2.EntityEndListener;
import org.culturegraph.metamorph.core2.Metamorph;
import org.culturegraph.metamorph.core2.NamedValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common basis for {@link Entity}, {@link Combine} etc.
 * 
 * @author Markus Michael Geipel

 */
public abstract class AbstractCollect extends AbstractNamedValuePipe implements  EntityEndListener, NamedValueAggregator {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCollect.class);

	private int oldRecord;
	private int oldEntity;
	private boolean alreadyEmitted;
	private boolean reset;
	private boolean sameEntity;
	private String name;
	private String value;
	private final Metamorph metamorph;


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


	public final void setFlushWith(final String flushEntity) {
		metamorph.addEntityEndListener(this, flushEntity);
	}

	/**
	 * @param sameEntity
	 *            the sameEntity to set
	 */
	
	public final void setSameEntity(final boolean sameEntity) {
		this.sameEntity = sameEntity;
	}

	
	public final void setReset(final boolean reset) {
		this.reset = reset;
	}





	/**
	 * @return the name
	 */
	
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
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


	private void updateCounts(final int newRecord, final int newEntity) {
		if (newRecord != oldRecord) {
			clear();
			alreadyEmitted = false;
			oldRecord = newRecord;
			LOG.trace("reset as records differ");
		}
		if (sameEntity && oldEntity != newEntity) {
			clear();
			alreadyEmitted = false;
			oldEntity = newEntity;
			LOG.trace("reset as entities differ");
		}
	}

	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		updateCounts(recordCount, entityCount);

		receive(name, value, source);

		if (isComplete()) {
			LOG.trace("Emitting on collect completion");
			emit();
			alreadyEmitted = true;
			if (reset) {
				LOG.trace("reset because of emit");
				clear();
				alreadyEmitted = false;
			}
		}
	}

	protected abstract void receive(final String name, final String value, final NamedValueSource source);

	/**
	 * @return
	 */
	protected abstract boolean isComplete();

	/**
	 * 
	 */
	protected abstract void clear();

	/**
	 * 
	 */
	protected abstract void emit();

	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		// nothing
		
	}

	@Override
	public final void onEntityEnd(final String entityName, final int recordCount, final int entityCount) {
		if (oldRecord == recordCount && !alreadyEmitted && (!sameEntity || oldEntity == entityCount)) {
			LOG.trace("Emitting on entity end");
			emit();
		}
	}

	
}
