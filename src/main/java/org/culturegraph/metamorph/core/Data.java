package org.culturegraph.metamorph.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * @author Markus Michael Geipel
 */
final class Data extends DataProcessorImpl implements DataSender, DataReceiver {
	
	/**
	 * @author Markus Michael Geipel
	 * @status Experimental
	 */
	public enum Mode {
		AS_VALUE, AS_NAME
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(Data.class);
	
	private String name;
	private String value;
	private Mode mode = Mode.AS_VALUE;
	private DataReceiver dataReceiver;

	
	@Override
	public void data(final String recName, final String recValue, final int recordCount, final int entityCount) {
		assert dataReceiver != null;
		
		final String tempData = applyFunctions(recValue);
		if(tempData==null){
			return;
		}

		
		String currentName = name;
		String currentValue = value;
		
		if(Mode.AS_NAME.equals(mode) && currentName == null){
			currentName = tempData;
		}else if(currentValue == null){
			currentValue = tempData;
		}
		
		if(currentName==null || currentValue ==null){
			LOG.warn("missing defaults name="+ currentName + " value=" + currentValue);
		}else{
			if(LOG.isTraceEnabled()){
				LOG.trace("emiting literal "+ currentName + "=" + currentValue);
			}
			dataReceiver.data(currentName, currentValue, recordCount, entityCount);
		}
	}
	
	/**
	 * @param dataReceiver the dataReceiver to set
	 */
	public void setDataReceiver(final DataReceiver dataReceiver) {
		assert dataReceiver != null;
		this.dataReceiver = dataReceiver;
	}
	
	/**
	 * @return the dataReceiver
	 */
	public DataReceiver getDataReceiver() {
		return dataReceiver;
	}


	public void setMode(final Mode mode){
		this.mode = mode;
	}
	
	/**
	 * @param name the defaultName to set
	 */
	public void setName(final String name) {
		this.name = name;
	}


	/**
	 * @param value the defaultValue to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}
	
	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return name;
	}


	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return value;
	}



}
