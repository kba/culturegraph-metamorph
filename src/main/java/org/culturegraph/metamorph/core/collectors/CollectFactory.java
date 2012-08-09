package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.util.ObjectFactory;
import org.culturegraph.util.ResourceUtil;

/**
 * Factory for all maps used by {@link Metamorph} and {@link MetamorphBuilder}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class CollectFactory extends ObjectFactory<Collect> {
	public static final String POPERTIES_LOCATION = "metamorph-collectors.properties";

	public CollectFactory() {
		super();
		loadClassesFromMap(ResourceUtil.loadProperties(POPERTIES_LOCATION), Collect.class);
	}
}
