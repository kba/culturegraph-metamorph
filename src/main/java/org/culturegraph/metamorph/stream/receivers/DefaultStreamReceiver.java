package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Empty implementation of {@link StreamReceiver}.
 * 
 * @author Markus Michael Geipel
 */
public class DefaultStreamReceiver implements StreamReceiver {

	@Override
	public void startRecord(final String identifier){/* do nothing */}

	@Override
	public void endRecord(){/* do nothing */}

	@Override
	public void startEntity(final String name) {/* do nothing */}

	@Override
	public void endEntity(){/* do nothing */}

	@Override
	public void literal(final String  name, final String  value) {/* do nothing */}

}
