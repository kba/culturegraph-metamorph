package org.culturegraph.metamorph.stream;

/**
 * Empty implementation of {@link StreamReceiver}.
 * 
 * @author Markus Michael Geipel
 */
public class DefaultStreamReceiver implements StreamReceiver {

	@Override
	public void startRecord(){/* do nothing */}

	@Override
	public void endRecord(){/* do nothing */}

	@Override
	public void startEntity(final String name) {/* do nothing */}

	@Override
	public void endEntity(){/* do nothing */}

	@Override
	public void literal(final String  name, final String  value) {/* do nothing */}

}
