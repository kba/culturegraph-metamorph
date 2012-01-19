package org.culturegraph.metamorph.core2.functions;

import org.culturegraph.metamorph.core2.NamedValueSource;

public abstract class AbstractSimpleStatelessFunction extends AbstractFunction{
	
	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		getNamedValueReceiver().receive(name, process(value), source, recordCount, entityCount);
	}
	protected abstract String process(String value);
}
