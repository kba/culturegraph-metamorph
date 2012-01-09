package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.ListMap;

/**
 * Collects the received results in a {@link ListMap}.
 * 
 * @author Markus Michael Geipel
 */
public final class ListMapWriter extends ListMap<String, String> implements StreamReceiver{

	@Override
	public void startRecord(final String identifier){
		clear();
		setId(identifier);
	}
	
	@Override
	public void literal(final String name, final String value) {
		put(name, value);
	}

	@Override
	public void endRecord() {
		// nothing to do
	}

	@Override
	public void startEntity(final String name) {
		 // nothing to do
	}

	@Override
	public void endEntity() {
		//  nothing to do
	}

}
