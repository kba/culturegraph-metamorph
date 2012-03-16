package org.culturegraph.metamorph.core.collectors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.type.ListMap;
import org.culturegraph.metastream.type.NamedValue;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Entity extends AbstractCollect {

	private final ListMap<NamedValueSource, NamedValue> literalListMap = new ListMap<NamedValueSource, NamedValue>();
	
	//private final List<NamedValue> literals = new ArrayList<NamedValue>();
	private final List<NamedValueSource> sourceList = new ArrayList<NamedValueSource>();
	private final Set<NamedValueSource> sourcesLeft = new HashSet<NamedValueSource>();

	public Entity(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {
		final StreamReceiver streamReceiver = getMetamorph().getStreamReceiver();
		streamReceiver.startEntity(getName());
		
	
		for(NamedValueSource source:sourceList){
			for (NamedValue literal : literalListMap.get(source)) {
				streamReceiver.literal(literal.getName(), literal.getValue());
			}
		}
//			final String name = entry.getKey();
//			for (String value : entry.getValue()) {
//				streamReceiver.literal(name, value);
//			}
//		}
//		
//		for (NamedValue literal : literals) {
//			streamReceiver.literal(literal.getName(), literal.getValue());
//		}
		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		literalListMap.put(source, new NamedValue(name, value));
		//literals.add(new NamedValue(name, value));
		sourcesLeft.remove(source);
	}

	@Override
	protected boolean isComplete() {
		return sourcesLeft.isEmpty();
	}

	@Override
	protected void clear() {
		sourcesLeft.addAll(sourceList);
		//literals.clear();
		literalListMap.clear();
	}

	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		sourceList.add(namedValueSource);
		sourcesLeft.add(namedValueSource);
	}
}
