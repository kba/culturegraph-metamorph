package org.culturegraph.metamorph.stream.readers;

import java.util.Set;


/**
 * Defines a Factory to create {@link Reader}s based on a given file/input format.
 * 
 * @author Markus Michael Geipel
 *
 */
public interface ReaderFactory {
	Reader newReader(final String format);
	boolean isFormatSupported(final String format);
	Set<String> getSupportedFormats();
}
