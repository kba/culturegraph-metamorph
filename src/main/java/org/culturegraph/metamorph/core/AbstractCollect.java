package org.culturegraph.metamorph.core;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common basis for {@link CollectEntity} and {@link CollectLiteral}.
 * 
 * @author Markus Michael Geipel
 * @status Experimental
 */
abstract class AbstractCollect implements NamedValueReceiver, Collect {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCollect.class);

	private int oldRecord;
	private int oldEntity;
	private int dataCount;
	// private StreamReceiver streamReceiver;
	private boolean reset;
	private boolean sameEntity;
	private String name;
	private String value;
	private final Metamorph metamorph;
	private final Set<Data> dataSources = new HashSet<Data>();
	
	public AbstractCollect(final Metamorph metamorph) {
		this.metamorph = metamorph;
	}
	
	protected Metamorph getMetamorph(){
		return metamorph;
	}

	
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
	protected Set<Data> getDataSources() {
		return dataSources;
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
			oldRecord = newRecord;
			LOG.trace("reset as records differ");
		}
		if (sameEntity && oldEntity != newEntity) {
			clear();
			oldEntity = newEntity;
			LOG.trace("reset as entities differ");
		}
	}
	

	@Override
	public final void data(final String name, final String value, final int recordCount, final int entityCount) {
		updateCounts(recordCount, entityCount);

		receive(name, value);

		if (isComplete()) {
			emit();
			if (reset) {
				LOG.trace("reset because of emit");
				clear();
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
	public final void addData(final Data data) {
		++dataCount;
		data.setDataReceiver(this);
		dataSources.add(data);
		onAddData(data);
	}

	@Override
	public void onEntityEnd(final String name) {
		emit();
	}

	/**
	 * @param data
	 */
	protected void onAddData(final Data data) {/* as default do nothing */
	}
}
