package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * @author Markus Michael Geipel
 */
public final class SwitchNameValue extends AbstractFunction {

	@Override
	public void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		getNamedValueReceiver().receive(value, name, source, recordCount, entityCount);
		
	}


}
