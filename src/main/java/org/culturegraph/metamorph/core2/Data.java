package org.culturegraph.metamorph.core2;

import org.culturegraph.metamorph.core2.functions.ValueProcessorImpl;

/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class Data extends ValueProcessorImpl implements NamedValueReceiver, NamedValueSource{

	private String name;
	private String value;
	// private String meta;
	private final String source;

	private NamedValueReceiver dataReceiver;



	public Data(final String source) {
		super();
		this.source = source;
	}
	

	
	public String getSource(){
		return source;
	}

	private static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}

	@Override
	public void receive(final String recName, final String recValue, final int recordCount, final int entityCount) {
		assert dataReceiver != null;

		final String tempData = applyFunctions(recValue);
		if (tempData == null) {
			return;
		}
		
		dataReceiver.receive(fallback(name, recName), fallback(value, tempData), recordCount, entityCount);

	}


	/**
	 * @param dataReceiver
	 *            the dataReceiver to set
	 */
	@Override
	public void setNamedValueReceiver(final NamedValueReceiver dataReceiver) {
		assert dataReceiver != null;
		this.dataReceiver = dataReceiver;
	}

	/**
	 * @return the dataReceiver
	 */
	public NamedValueReceiver getDataReceiver() {
		return dataReceiver;
	}



	/**
	 * @param name
	 *            the defaultName to set
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param value
	 *            the defaultValue to set
	 */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the defaultName
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the defaultValue
	 */
	@Override
	public String getValue() {
		return value;
	}



}
