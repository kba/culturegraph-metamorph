package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metamorph.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final String INSTANTIATION_PROBLEM = " could not be instantiated";
	
		
	private static final Logger LOG = LoggerFactory.getLogger(DefaultReaderFactory.class);

	private final Map<String, Class<? extends Reader>> readerClasses = new HashMap<String, Class<? extends Reader>>();

	@SuppressWarnings("unchecked")
	// protected by "if (Reader.class.isAssignableFrom(clazz)) {"
	public DefaultReaderFactory() {
		super();
		String propertiesLocation = System.getProperty(PROPERTY_LOCATION_NAME);
		if(propertiesLocation==null){
			propertiesLocation = DEFAULT_PROPERTIES_LOCATION;
		}
		
		final ClassLoader loader = ReflectionUtil.getClassLoader();
		final InputStream inStream = loader.getResourceAsStream(propertiesLocation);
		final Properties properties = new Properties();

		if(inStream==null){
			throw new MetamorphException(propertiesLocation + "' not found");
		}
		try {
			properties.load(inStream);
		} catch (IOException e) {
			throw new MetamorphException("'"+ propertiesLocation + "' could not be loaded", e);
		} 

		for (Entry<Object, Object> entry : properties.entrySet()) {
			final String className = entry.getValue().toString();
			final String format = entry.getKey().toString();

			try {
				final Class<?> clazz = loader.loadClass(className);
				if (Reader.class.isAssignableFrom(clazz)) {
					readerClasses.put(format, (Class<? extends Reader>) clazz);
					LOG.debug("Reader for '" + format + "': " + className);
				} else {
					LOG.warn(className + " does not implement " + Reader.class.getName() + " registration with "
							+ DefaultReaderFactory.class.getSimpleName() + " failed.");
				}
			} catch (ClassNotFoundException e) {
				LOG.warn(className + " not found", e);
			}
		}
	}

	@Override
	public Reader newReader(final String format) {
		if (!isFormatSupported(format)) {
			throw new IllegalArgumentException("format " + format + " is not supported");
		}
		final Class<? extends Reader> clazz = readerClasses.get(format);
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (IllegalAccessException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		}
	}

	@Override
	public boolean isFormatSupported(final String format) {
		return readerClasses.containsKey(format);
	}

	@Override
	public Set<String> getSupportedFormats() {
		return readerClasses.keySet();
	}

}
