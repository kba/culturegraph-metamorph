package org.culturegraph.metamorph.core;

/**
 * Base class for {@link NamedValuePipe}s
 * @author Markus Michael Geipel
 *
 */
public abstract class AbstractNamedValuePipeHead implements NamedValuePipeHead{
	private NamedValueReceiver namedValueReceiver;
	private NamedValuePipe last = this;
	
	protected final NamedValueReceiver getNamedValueReceiver() {
		return namedValueReceiver;
	}

	@Override
	public final <R extends NamedValueReceiver> R setNamedValueReceiver(final R receiver) {
		this.namedValueReceiver = receiver;
		return receiver;
	}
	
	@Override
	public final void appendPipe(final NamedValuePipe namedValuePipe) {
		if(last==null){
			throw new IllegalStateException("NamedValuePipe already finalyzed.");
		}
		last = last.setNamedValueReceiver(namedValuePipe);
	}
	
	@Override
	public final void endPipe(final NamedValueReceiver lastReceiver) {
		last.setNamedValueReceiver(lastReceiver);
		last = null;
	}
}
