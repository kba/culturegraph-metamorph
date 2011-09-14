package org.culturegraph.metamorph.core;

import java.util.HashSet;
import java.util.Set;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.culturegraph.metamorph.types.Literal;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class CollectEntity extends AbstractCollect {

	private final Set<Literal> literals = new HashSet<Literal>();
	private StreamReceiver streamReceiver;

	@Override
	protected void emit() {
		streamReceiver.startEntity(getName());
		for (Literal literal : literals) {
			if (literal.getName() != null && literal.getValue() != null) {
				streamReceiver.literal(literal.getName(), literal.getValue());
			}
		}
		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value) {
		literals.add(new Literal(name, value));
	}

	@Override
	protected boolean isComplete() {
		return literals.size() == getDataCount();
	}

	@Override
	protected void clear() {
		literals.clear();
	}
	
	/**
	 * @param streamReceiver the {@link StreamReceiver} to set
	 */
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		this.streamReceiver = streamReceiver;
	}
}
