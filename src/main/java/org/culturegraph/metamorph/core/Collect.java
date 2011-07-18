package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;

/**
 * @author Markus Michael Geipel
 */
interface Collect extends EntityEndListener{

	/**
	 * @param data
	 */
	void addData(Data data);
	void setSameEntity(boolean sameEntity);
	void setStreamReceiver(StreamReceiver streamReceiver);
	void setReset(boolean reset);
	void setName(String name);
	void setValue(String value);
	String getName();
	String getValue();
}