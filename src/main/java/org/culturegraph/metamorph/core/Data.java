package org.culturegraph.metamorph.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * @author Markus Michael Geipel
 */
final class Data extends DataProcessorImpl implements DataSender {
	
	/**
	 * @author Markus Michael Geipel
	 * @status Experimental
	 */
	public enum Mode {
		AS_VALUE, AS_NAME
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(Data.class);
	
	
	private String defaultName;
	private String defaultValue;
	private Mode mode = Mode.AS_VALUE;
	private DataReceiver dataReceiver;

	
	
	public void data(final CharSequence data, final int recordCount, final int entityCount){
		assert dataReceiver != null;
		
		final String tempData = applyFunctions(data.toString());
		if(tempData==null){
			return;
		}
		
		String name = defaultName;
		String value = defaultValue;
		
		if(Mode.AS_NAME.equals(mode) && name == null){
			name = tempData;
		}else if(value == null){
			value = tempData;
		}
		
		if(name==null || value ==null){
			LOG.warn("missing defaults name="+ name + " value=" + value);
		}else{
			if(LOG.isTraceEnabled()){
				LOG.trace("emiting literal "+ name + "=" + value);
			}
			dataReceiver.data(name, value, this, recordCount, entityCount);
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
	 * @param defaultName the defaultName to set
	 */
	public void setDefaultName(final String defaultName) {
		this.defaultName = defaultName;
	}


	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}


	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
}
