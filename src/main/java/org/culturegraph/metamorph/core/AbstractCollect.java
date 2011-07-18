package org.culturegraph.metamorph.core;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
abstract class AbstractCollect implements DataReceiver, Collect {



	private static final Logger LOG = LoggerFactory.getLogger(AbstractCollect.class);

	private int oldRecord;
	private int oldEntity;
	private int dataCount;
	private Metamorph transformer;
	private boolean reset;
	private boolean sameEntity;
	private String name;
	private String value;

	private final Set<Data> dataSources = new HashSet<Data>();


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
	
	/**
	 * @return the transformer
	 */
	public final Metamorph getTransformer() {
		return transformer;
	}
	
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
	 * @param name the name to set
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
	 * @param value the value to set
	 */
	@Override
	public final void setValue(final String value) {
		this.value = value;
	}



	/**
	 * @param transformer the transformer to set
	 */
	public final void setMetamorph(final Metamorph transformer) {
		this.transformer = transformer;
	}

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
	public final void data(final Literal literal,
			final DataSender sender, final int recordCount,
			final int entityCount) {
		updateCounts(recordCount, entityCount);

		receive(literal);

		if(isComplete()){
			emit();
			if (reset) {
				LOG.trace("reset because of emit");
				clear();
			}
		}
	}

	/**
	 * @param literal
	 */
	protected abstract void receive(Literal literal);


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
	protected abstract void emit() ;
	
	
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
	};
	
	/**
	 * @param data
	 */
	protected void onAddData(final Data data) {/*as default do nothing*/}
}
