package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.core2.Metamorph;
import org.culturegraph.metamorph.core2.NamedValueReceiver;
import org.culturegraph.metamorph.core2.NamedValueSource;
import org.culturegraph.metamorph.core2.functions.Function;
import org.culturegraph.metamorph.core2.functions.ValueProcessorImpl;

/**
 * Implementation of the <code>&lt;group&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Group extends AbstractCollect implements NamedValueSource{


	private final ValueProcessorImpl processor = new ValueProcessorImpl();
	private NamedValueReceiver namedValueReceiver;
	
	public Group(final Metamorph metamorph) {
		super(metamorph);
	}
	
	private static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}

	
	@Override
	public void addFunction(final Function function) {
		processor.addFunction(function);
	}

	@Override
	public void setNamedValueReceiver(final NamedValueReceiver dataReceiver) {
		this.namedValueReceiver = dataReceiver;
		
	}

	@Override
	protected void receive(final String recName, final String recValue) {
			assert namedValueReceiver != null;
			final String tempValue = processor.applyFunctions(recValue);
			if (tempValue == null) {
				return;
			}
			namedValueReceiver.receive(fallback(getName(), recName), fallback(getValue(), tempValue), getRecordCount(), getEntityCount());
	}

	@Override
	protected boolean isComplete() {
		return false;
	}

	@Override
	protected void clear() {
		//nothing
	}

	@Override
	protected void emit() {
		// TODO Auto-generated method stub
		
	}

}
