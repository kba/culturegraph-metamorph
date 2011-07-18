package org.culturegraph.metamorph.core;

/**
 * @author Markus Michael Geipel
 */
interface Collect extends EntityEndListener{

	/**
	 * @param data
	 */
	void addData(Data data);
	void setSameEntity(boolean sameEntity);
	void setMetamorph(Metamorph metamorph);
	void setReset(boolean reset);
	void setName(String name);
	void setValue(String value);
	String getName();
	String getValue();
}