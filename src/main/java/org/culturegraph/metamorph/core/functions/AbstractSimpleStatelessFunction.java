package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * Baseclass for {@link Function}s returning only one or zero results and do not maintain a state.
 * 
 * @author Markus Michael Geipel
 *
 */
public abstract class AbstractSimpleStatelessFunction extends AbstractFunction{
	
	@Override
	public final void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		final String processedValue = process(value);
		if(processedValue==null){
			return;
		}
		getNamedValueReceiver().receive(name, processedValue , source, recordCount, entityCount);
	}
	protected abstract String process(String value);
}
