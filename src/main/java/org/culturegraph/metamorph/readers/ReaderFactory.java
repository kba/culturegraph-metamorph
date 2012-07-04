/**
 * 
 */
package org.culturegraph.metamorph.readers;

import org.culturegraph.util.ObjectFactory;
import org.culturegraph.util.ResourceUtil;

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
