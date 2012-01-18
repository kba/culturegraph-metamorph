package org.culturegraph.metamorph.core2.collectors;

import java.util.HashSet;
import java.util.Set;

import org.culturegraph.metamorph.core2.NamedValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common basis for {@link CollectEntity} and {@link CollectLiteral}.
 * 
 * @author Markus Michael Geipel
 * @status Experimental
 */
public abstract class AbstractCollect implements  Collect {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCollect.class);

	private int oldRecord;
	private int oldEntity;
	private int dataCount;
	private boolean alreadyEmitted;
	private boolean reset;
	private boolean sameEntity;
	private String name;
	private String value;

	private final Set<NamedValueSource> namedValueSources = new HashSet<NamedValueSource>();
	

	
	protected final int getRecordCount() {
		return oldRecord;
	}

	protected final int getEntityCount() {
		return oldEntity;
	}
	
	/**
	 * @param sameEntity
	 *            the sameEntity to set
	 */
	@Override
	public final void setSameEntity(final boolean sameEntity) {
		this.sameEntity = sameEntity;
	}

	@Override
	public final void setReset(final boolean reset) {
		this.reset = reset;
	}

	/**
	 * @return the dataCount
	 */
	protected final int getDataCount() {
		return dataCount;
	}

	// /**
	// * @return the transformer
	// */
	// public final StreamReceiver getStreamReceiver() {
	// return streamReceiver;
	// }

	/**
	 * @return
	 */
	protected final Set<NamedValueSource> getNamedValueSources() {
		return namedValueSources;
	}

	/**
	 * @return the name
	 */
	@Override
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	@Override
	public final String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	@Override
	public final void setValue(final String value) {
		this.value = value;
	}

	// /**
	// * @param streamReceiver the transformer to set
	// */
	// public final void setStreamReceiver(final StreamReceiver streamReceiver)
	// {
	// this.streamReceiver = streamReceiver;
	// }

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
	public final void receive(final String name, final String value, final int recordCount, final int entityCount) {
		updateCounts(recordCount, entityCount);

		receive(name, value);

		if (isComplete()) {
			emit();
			alreadyEmitted = true;
			if (reset) {
				LOG.trace("reset because of emit");
				clear();
				alreadyEmitted= false;
			}
		}
	}


	protected abstract void receive(final String name, final String value);

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
	public final void addNamedValueSource(final NamedValueSource namedValueSource) {
		++dataCount;
		namedValueSource.setNamedValueReceiver(this);
		namedValueSources.add(namedValueSource);
		onAddData(namedValueSource);
		
	}
	


	@Override
	public final void onEntityEnd(final String entityName, final int recordCount, final int entityCount) {
		if(oldRecord==recordCount && !alreadyEmitted && (!sameEntity || oldEntity == entityCount)){
				emit();
		}
	}

	/**
	 * @param data
	 */
	protected void onAddData(final NamedValueSource namedValueSource) {/* as default do nothing */
	}
}
