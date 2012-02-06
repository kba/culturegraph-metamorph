/**
 * 
 */
package org.culturegraph.metamorph.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 * 
 */
public final class ResourceUtil {

	private ResourceUtil() {
		// No instances allowed
	}

	/**
	 * first attempts to open resource with name 'name'. On fail attempts to open file.
	 * 
	 * @param name
	 * @return
	 * @throws FileNotFoundException if all attempts fail
	 */
	public static InputStream getStream(final String name) throws FileNotFoundException {
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		final InputStream stream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(name);
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
}
