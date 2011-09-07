package org.culturegraph.metamorph.core;


/**
 * Interface for classes which collect pieces of data. 
 * 
 * @author Markus Michael Geipel
 */
interface Collect extends EntityEndListener{

	/**
	 * @param data
	 */
	void addData(Data data);
	void setSameEntity(boolean sameEntity);
//	void setStreamReceiver(StreamReceiver streamReceiver);
	void setReset(boolean reset);
	void setName(String name);
	void setValue(String value);
	String getName();
	String getValue();
}