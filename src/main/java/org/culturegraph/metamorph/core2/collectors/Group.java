package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.core2.NamedValueReceiver;
import org.culturegraph.metamorph.core2.NamedValueSource;
import org.culturegraph.metamorph.core2.ValueProcessor;
import org.culturegraph.metamorph.core2.ValueProcessorImpl;
import org.culturegraph.metamorph.core2.functions.Function;

/**
 * Implementation of the <code>&lt;group&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Group implements ValueProcessor, NamedValueSource, Collect{

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

	@Override
	public void onEntityEnd(String name, int recordCount, int entityCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSameEntity(boolean sameEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReset(boolean reset) {
		// TODO Auto-generated method stub
		
	}
}
