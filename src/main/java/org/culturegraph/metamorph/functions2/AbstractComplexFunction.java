package org.culturegraph.metamorph.functions2;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public abstract class AbstractComplexFunction extends AbstractFunction {

	private int recordCount;
	private int entityCount;
	
	
	protected final int getRecordCount() {
		return recordCount;
	}

	protected final int getEntityCount() {
		return entityCount;
	}


}
