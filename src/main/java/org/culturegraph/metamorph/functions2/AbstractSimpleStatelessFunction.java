package org.culturegraph.metamorph.functions2;

public abstract class AbstractSimpleStatelessFunction extends AbstractFunction{
	
	@Override
	public final void receive(final String value, final int recordCount, final int entityCount) {
		getValueReceiver().receive(process(value), recordCount, entityCount);
	}

	protected abstract String process(String value);
}
