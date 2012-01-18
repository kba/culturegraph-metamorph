package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.core2.functions.ValueProcessor;
import org.culturegraph.metamorph.core2.EntityEndListener;


/**
 * Interface for classes which collect pieces of data. 
 * 
 * @author Markus Michael Geipel
 */
public interface Collect extends EntityEndListener, NamedValueAggregator, ValueProcessor{

	/**
	 * @param data
	 */
	
	void setSameEntity(boolean sameEntity);
	void setReset(boolean reset);
	void setName(String name);
	void setValue(String value);
	void setFlushWith(String entity);
	String getName();
	String getValue();
}