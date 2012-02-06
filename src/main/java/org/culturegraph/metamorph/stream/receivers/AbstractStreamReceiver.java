package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Empty implementation of {@link StreamReceiver}.
 * 
 * @author Markus Michael Geipel
 */
public abstract class AbstractStreamReceiver implements StreamReceiver {
	@Override
	public void endRecord(){/* do nothing */}

	@Override
	public void startEntity(final String name) {/* do nothing */}

	@Override
	public void endEntity(){/* do nothing */}
}
