package org.culturegraph.metamorph.util.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;

/**
 * 
 * Note that {@link Metamorph} instances are not threadsafe. Each {@link Thread} needs its own {@link Metamorph} instance.
 * For a concurrent version of {@link MetamorphRegistry} see {@link ConcurrentMetamorphRegistry}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class MetamorphRegistry implements ReadOnlyRegistry<Metamorph> {
	
	
	// morphMap is not concurrent as it is not changed again. For a ReadWriteRegistry a ConcurrentHashMap would be needed!
	private final Map<String, Metamorph> morphMap = new HashMap<String, Metamorph>();
	
	/**
	 * Creates a {@link MetamorphRegistry} based on a {@link Map}. The map is untyped to allow the use of {@link Properties} as argument.
	 * @param morphMap
	 */
	public MetamorphRegistry(final Map<?, ?> morphMap) {
		for(Entry<?, ?> entry:morphMap.entrySet()){
			final Metamorph metamorph = MetamorphBuilder.build(entry.getValue().toString());
			this.morphMap.put(entry.getKey().toString(), metamorph);
		}
	}

	@Override
	public Metamorph get(final String key) {
		return morphMap.get(key);
	}
}
