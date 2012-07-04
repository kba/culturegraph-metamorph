package org.culturegraph.metamorph.util.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;

/**
 * Provides {@link Metamorph} instances by key. For each {@link Thread} a
 * separate {@link Metamorph} instance is returned.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class ConcurrentMetamorphRegistry implements ReadOnlyRegistry<Metamorph> {
	
	private final Map<String, ThreadLocal<Metamorph>> threadLocalMorphMap = new HashMap<String, ThreadLocal<Metamorph>>();

	public ConcurrentMetamorphRegistry(final Map<String, String> morphMap) {
		for (final Entry<String, String> entry : morphMap.entrySet()) {
			final ThreadLocal<Metamorph> threadLocal = new ThreadLocal<Metamorph>() {
				@Override
				protected Metamorph initialValue() {
					return MetamorphBuilder.build(entry.getValue());
				}
			};
			this.threadLocalMorphMap.put(entry.getKey(), threadLocal);
		}
	}

	@Override
	public Metamorph get(final String key) {
		return threadLocalMorphMap.get(key).get();
	}

}
