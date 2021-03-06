package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.core.NamedValueSource;


/**
 * @author Markus Michael Geipel
 */
public abstract class AbstractStatefulFunction extends AbstractFunction {

	private int recordCount;
	private int entityCount;
	private NamedValueSource source;
	private String lastName;
	
	protected final int getRecordCount() {
		return recordCount;
	}

	protected final int getEntityCount() {
		return entityCount;
	}
	
	protected final NamedValueSource getNamedValueSource(){
		return source;
	}
	
	protected final String getLastName() {
		return lastName;
	}
	
	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		
		if(!sameRecord(recordCount)){
			reset();
			this.recordCount = recordCount;
		}
		if(entityClearNeeded(entityCount)){
			reset();
		}
		this.entityCount = entityCount;
		this.source = source;
		this.lastName = name;
		
		final String processedValue = process(value);
		if(processedValue==null){
			return;
		}
	
		getNamedValueReceiver().receive(name, processedValue , source, recordCount, entityCount);
	}
	


	private boolean entityClearNeeded(final int entityCount) {
		return doResetOnEntityChange() && this.entityCount!=entityCount;
	}

	

	private boolean sameRecord(final int recordCount) {
		return this.recordCount == recordCount;
	}

	protected abstract String process(String value);
	protected abstract void reset();
	protected abstract boolean doResetOnEntityChange();
}
