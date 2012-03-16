/**
 * 
 */
package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metamorph.util.ObjectFactory;
import org.culturegraph.metamorph.util.ResourceUtil;

/**
 * Builds readers.
 * 
 * @author Christoph Böhme
 *
 */
public final class ReaderFactory extends ObjectFactory<Reader> {
	public static final String POPERTIES_LOCATION = "metamorph-readers.properties";

	public ReaderFactory() {
		super();
		loadClassesFromMap(ResourceUtil.loadProperties(POPERTIES_LOCATION), Reader.class);
	}

}
