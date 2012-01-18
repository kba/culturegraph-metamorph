package org.culturegraph.metamorph.core2.collectors;

import java.util.HashSet;
import java.util.Set;

import org.culturegraph.metamorph.core2.Metamorph;
import org.culturegraph.metamorph.core2.functions.Function;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.NamedValue;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class CollectEntity extends AbstractCollect {

	private final Set<NamedValue> literals = new HashSet<NamedValue>();



	public CollectEntity(final Metamorph metamorph) {
		super(metamorph);
	}
	
	@Override
	protected void emit() {
		final StreamReceiver streamReceiver = getMetamorph().getStreamReceiver();
		streamReceiver.startEntity(getName());
		for (NamedValue literal : literals) {
			if (literal.getName() != null && literal.getValue() != null) {
				streamReceiver.literal(literal.getName(), literal.getValue());
			}
		}
		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value) {
		literals.add(new NamedValue(name, value));
	}

	@Override
	protected boolean isComplete() {
		return literals.size() == getDataCount();
	}

	@Override
	protected void clear() {
		literals.clear();
	}

	@Override
	public void addFunction(final Function function) {
		throw new UnsupportedOperationException();
		
	}
}
