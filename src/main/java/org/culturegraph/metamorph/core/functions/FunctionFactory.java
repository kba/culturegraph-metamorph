package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.util.ObjectFactory;
import org.culturegraph.metamorph.util.ResourceUtil;

/**
 * Provides the functions for {@link Metamorph}. By the default it contains the
 * standard function set. New functions can be registered during runtime.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class FunctionFactory extends ObjectFactory<Function> {

	public static final String POPERTIES_LOCATION = "metamorph-functions.properties";

	public FunctionFactory() {
		super();
		loadClassesFromMap(ResourceUtil.loadProperties(POPERTIES_LOCATION), Function.class);
	}
}
