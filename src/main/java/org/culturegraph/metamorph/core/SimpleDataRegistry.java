package org.culturegraph.metamorph.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link Registry} with a {@link HashMap}.
 * 
 * @author Markus Michael Geipel

 * @param <T>
 */
final class SimpleDataRegistry<T> implements Registry<T> {

	private final Map<String, List<T>> map = new HashMap<String, List<T>>();

	@Override
	public void register(final String path, final T value) {
		List<T> matchingData = map.get(path);
		if (matchingData == null) {
			matchingData = new ArrayList<T>();
			map.put(path, matchingData);

		}
		matchingData.add(value);

	}

	@Override
	public List<T> get(final String path) {
		final List<T> matchingData = map.get(path);
		if (matchingData == null) {
			return Collections.emptyList();
		}
		return matchingData;
	}

}
