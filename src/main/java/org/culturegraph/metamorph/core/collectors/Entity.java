package org.culturegraph.metamorph.core.collectors;

import java.util.HashSet;
import java.util.Set;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.NamedValue;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Entity extends AbstractCollect {

	private final Set<NamedValue> literals = new HashSet<NamedValue>();
	private final Set<NamedValueSource> sources = new HashSet<NamedValueSource>();
	private final Set<NamedValueSource> sourcesLeft = new HashSet<NamedValueSource>();

	public Entity(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {
		final StreamReceiver streamReceiver = getMetamorph().getStreamReceiver();
		streamReceiver.startEntity(getName());
		for (NamedValue literal : literals) {
			streamReceiver.literal(literal.getName(), literal.getValue());
		}
		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		literals.add(new NamedValue(name, value));
		sourcesLeft.remove(source);
	}

	@Override
	protected boolean isComplete() {
		return sourcesLeft.isEmpty();
	}

	@Override
	protected void clear() {
		sourcesLeft.addAll(sources);
		literals.clear();
	}

	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		sources.add(namedValueSource);
		sourcesLeft.add(namedValueSource);
	}
}
