package org.culturegraph.metamorph.core.maps;

import java.util.Map;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.util.ObjectFactory;
import org.culturegraph.util.ResourceUtil;

/**
 * Factory for all collectors used by {@link Metamorph} and {@link MetamorphBuilder}
 * 
 * @author Markus Michael Geipel
 *
 */
@SuppressWarnings("rawtypes")
public final class MapFactory extends ObjectFactory<Map> {
	public static final String POPERTIES_LOCATION = "metamorph-maps.properties";

	public MapFactory() {
		super();
		loadClassesFromMap(ResourceUtil.loadProperties(POPERTIES_LOCATION), Map.class);
	}
}
