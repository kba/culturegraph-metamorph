package org.culturegraph.metamorph.core.collectors;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.ListMap;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Entity extends AbstractCollect {

	private final ListMap<String, String> literalListMap = new ListMap<String, String>();
	
	//private final List<NamedValue> literals = new ArrayList<NamedValue>();
	private final Set<NamedValueSource> sources = new HashSet<NamedValueSource>();
	private final Set<NamedValueSource> sourcesLeft = new HashSet<NamedValueSource>();

	public Entity(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {
		final StreamReceiver streamReceiver = getMetamorph().getStreamReceiver();
		streamReceiver.startEntity(getName());
		
		for(Entry<String, List<String>> entry:literalListMap.entrySet()){
			final String name = entry.getKey();
			for (String value : entry.getValue()) {
				streamReceiver.literal(name, value);
			}
		}
//		
//		for (NamedValue literal : literals) {
//			streamReceiver.literal(literal.getName(), literal.getValue());
//		}
		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		literalListMap.put(name, value);
		//literals.add(new NamedValue(name, value));
		sourcesLeft.remove(source);
	}

	@Override
	protected boolean isComplete() {
		return sourcesLeft.isEmpty();
	}

	@Override
	protected void clear() {
		sourcesLeft.addAll(sources);
		//literals.clear();
		literalListMap.clear();
	}

	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		sources.add(namedValueSource);
		sourcesLeft.add(namedValueSource);
	}
}
