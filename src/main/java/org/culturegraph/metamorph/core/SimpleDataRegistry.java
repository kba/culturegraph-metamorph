package org.culturegraph.metamorph.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link DataRegistry} with a {@link HashMap}.
 * 
 * @author Markus Michael Geipel
 * 
 */
final class SimpleDataRegistry implements DataRegistry {

	private final Map<String, List<Data>> map = new HashMap<String, List<Data>>();

	@Override
	public void register(final String path, final Data data) {
		List<Data> matchingData = map.get(path);
		if (matchingData == null) {
			matchingData = new ArrayList<Data>();
			map.put(path, matchingData);

		}
		matchingData.add(data);

	}

	@Override
	public List<Data> get(final String path) {
		final List<Data> matchingData = map.get(path);
		if (matchingData == null) {
			return Collections.emptyList();
		}
		return matchingData;
	}

}
