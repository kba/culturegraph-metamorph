/**
 * 
 */
package org.culturegraph.metamorph.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>, Markus Michael Geipel
 * 
 */
public final class ResourceUtil {

	private ResourceUtil() {
		// No instances allowed
	}

	/**
	 * first attempts to open resource with name 'name'. On fail attempts to
	 * open file.
	 * 
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 *             if all attempts fail
	 */
	public static InputStream getStream(final String name) throws FileNotFoundException {
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (stream == null) {
			return getStream(new File(name));
		}
		return stream;
	}

	public static InputStream getStream(final File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public static Reader getReader(final String name) throws FileNotFoundException {
		return new InputStreamReader(getStream(name));
	}

	public static Reader getReader(final File file) throws FileNotFoundException {
		return new InputStreamReader(getStream(file));
	}

	public static Properties loadProperties(final String location) {
		final Properties properties;
		try {
			properties = new Properties();
			properties.load(getStream(location));
		} catch (IOException e) {
			throw new MetamorphException("'" + location + "' could not be loaded", e);
		}
		return properties;
	}
}
