package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.functions.Function;

/**
 * Implementation of the <code>&lt;group&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class Group implements ValueProcessor, NamedValueAggregator, NamedValueSource{

	private String name;
	private String value;
	private NamedValueReceiver namedValueReceiver;
	private final ValueProcessorImpl processor = new ValueProcessorImpl();
	
	private static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}

	@Override
	public void receive(final String recName, final String recValue, final int recordCount, final int entityCount) {
		assert namedValueReceiver != null;

		final String tempValue = processor.applyFunctions(recValue);
		if (tempValue == null) {
			return;
		}
		namedValueReceiver.receive(fallback(name, recName), fallback(tempValue, recValue), recordCount, entityCount);
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
	 * @return the Name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the Value
	 */
	@Override
	public String getValue() {
		return value;
	}

	
	@Override
	public void setNamedValueReceiver(final NamedValueReceiver namedValueReceiver) {
		this.namedValueReceiver = namedValueReceiver;
	}

	@Override
	public void addFunction(final Function function) {
		processor.addFunction(function);
	}

	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		namedValueSource.setNamedValueReceiver(this);
	}
}
