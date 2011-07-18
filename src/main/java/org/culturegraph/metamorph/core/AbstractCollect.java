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
	public final void data(final String name, final String value,
			final DataSender sender, final int recordCount,
			final int entityCount) {
		updateCounts(recordCount, entityCount);

		receive(new Literal(name, value));

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


	/**
	 * @author Markus Michael Geipel
	 * @status Experimental
	 */
	protected static final class Literal {
		private static final int MAGIC1 = 23;
		private static final int MAGIC2 = 31;
		private final String name;
		private final String value;
		private final int preCompHashCode;
		
		Literal(final String name, final String value){
			this.name = name;
			this.value = value;
			int result = MAGIC1;
			result = MAGIC2 * result + value.hashCode();
			result = MAGIC2 * result + name.hashCode();
			preCompHashCode = result;
		}
		
		/**
		 * @return the name
		 */
		protected String getName() {
			return name;
		}


		/**
		 * @return the value
		 */
		protected String getValue() {
			return value;
		}

		@Override
		public int hashCode() {
			return preCompHashCode;
		}
	
		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof Literal) {
				final Literal literal = (Literal) obj;
				return literal.preCompHashCode!=preCompHashCode && literal.name.equals(name) && literal.value.equals(value);
			}
			return false;
		}
	}
}
