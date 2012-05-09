package org.culturegraph.metamorph.core.collectors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.ListMap;
import org.culturegraph.metamorph.types.NamedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Entity extends AbstractCollect {
	private static final Logger LOG = LoggerFactory.getLogger(Entity.class);
	
	private final ListMap<NamedValueSource, NamedValue> literalListMap = new ListMap<NamedValueSource, NamedValue>();
	private final List<NamedValueSource> sourceList = new ArrayList<NamedValueSource>();
	private final Set<NamedValueSource> sourcesLeft = new HashSet<NamedValueSource>();

	private final List<Entity> subEntityList = new ArrayList<Entity>();

	public Entity(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {
		final StreamReceiver streamReceiver = getMetamorph().getStreamReceiver();
		streamReceiver.startEntity(getName());

		for (NamedValueSource source : sourceList) {
			for (NamedValue literal : literalListMap.get(source)) {
				streamReceiver.literal(literal.getName(), literal.getValue());
			}
		}

		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		literalListMap.put(source, new NamedValue(name, value));
		sourcesLeft.remove(source);
	}

	@Override
	protected boolean isComplete() {
		return sourcesLeft.isEmpty();
	}

	@Override
	protected void clear() {
		sourcesLeft.addAll(sourceList);
		literalListMap.clear();
	}

	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		sourceList.add(namedValueSource);
		sourcesLeft.add(namedValueSource);
		if (namedValueSource instanceof Entity) {
			final Entity entity = (Entity) namedValueSource;
			subEntityList.add(entity);
			LOG.info("subentity added");
		}
	}
}
