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
//	private static final Logger LOG = LoggerFactory.getLogger(Entity.class);

	//public static final String ENTITY_NAME = "_entity_name";

	private final ListMap<NamedValueSource, NamedValue> literalListMap = new ListMap<NamedValueSource, NamedValue>();
	private final List<NamedValueSource> sourceList = new ArrayList<NamedValueSource>();
	private final Set<NamedValueSource> sourcesLeft = new HashSet<NamedValueSource>();


	public Entity(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {
		if (isRootEntity()) {
			//root starts the write
			writeCollectedNamedValues();
		} else {
			// must be an entity as entities can not be nested in other collectors
			final Entity parentEntity = (Entity) getNamedValueReceiver(); 
			//nested entities signal their readiness to parent by sending null
			parentEntity.receive(null, null, this, getRecordCount(), getEntityCount());
		}
	}
	
	private boolean isRootEntity() {
		return  getNamedValueReceiver() == getMetamorph();
	}
	
//	private boolean hasSomethingToWrite(){
//		
//	}

	private void writeCollectedNamedValues(){
//		if(literalListMap.isEmpty()){
//			return;
//		}
		
		final StreamReceiver streamReceiver = getMetamorph().getStreamReceiver();
		streamReceiver.startEntity(getName());
		
		for (NamedValueSource source : sourceList) {
			if (source instanceof Entity) {
				final Entity nestedEntity = (Entity) source;
				nestedEntity.writeCollectedNamedValues();
			}
			for (NamedValue literal : literalListMap.get(source)) {
				streamReceiver.literal(literal.getName(), literal.getValue());
			}
		}

		streamReceiver.endEntity();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		if (name != null) { // do not record ready signals from nested entities
			literalListMap.put(source, new NamedValue(name, value));
		}
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

	}
}
