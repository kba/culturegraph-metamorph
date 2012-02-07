package org.culturegraph.metamorph.core;

/**
 * Base class for {@link NamedValuePipe}s
 * @author Markus Michael Geipel
 *
 */
public abstract class AbstractNamedValuePipe implements NamedValuePipe{
	private NamedValueReceiver namedValueReceiver;

	
	protected final NamedValueReceiver getNamedValueReceiver() {
		return namedValueReceiver;
	}


	@Override
	public final <R extends NamedValueReceiver> R setNamedValueReceiver(final R receiver) {
		this.namedValueReceiver = receiver;
		return receiver;
	}
}
