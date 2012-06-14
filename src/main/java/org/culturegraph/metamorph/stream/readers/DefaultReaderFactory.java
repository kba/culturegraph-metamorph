package org.culturegraph.metamorph.stream.readers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.util.ObjectFactory;
import org.culturegraph.metamorph.util.ResourceUtil;
import org.culturegraph.metamorph.util.Util;

/**
 * {@link AbstractReaderFactory} with pica, mab2 and marc21 {@link Reader}s
 * already registered.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class DefaultReaderFactory extends AbstractReaderFactory {
	public static final String PROPERTY_LOCATION_NAME = "metamorph.readers.properties";
	public static final String DEFAULT_PROPERTIES_LOCATION = "metamorph-readers.properties";

	private static final Map<String, String> ARGUMENTS = Collections.emptyMap();

	private final ObjectFactory<Reader> factory = new ObjectFactory<Reader>();


	public DefaultReaderFactory() {
		super();
		
		final String propertiesLocation = Util.fallback(System.getProperty(PROPERTY_LOCATION_NAME), DEFAULT_PROPERTIES_LOCATION);
		factory.loadClassesFromMap(ResourceUtil.loadProperties(propertiesLocation), Reader.class);
	}

	@Override
	public Reader newReader(final String format) {
		if (!isFormatSupported(format)) {
			throw new IllegalArgumentException("format " + format + " is not supported");
		}
		return factory.newInstance(format, ARGUMENTS);
	}

	@Override
	public boolean isFormatSupported(final String format) {
		return factory.containsKey(format);
	}

	@Override
	public Set<String> getSupportedFormats() {
		return factory.keySet();
	}
}
