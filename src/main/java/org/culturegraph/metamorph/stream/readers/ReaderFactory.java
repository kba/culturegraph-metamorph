package org.culturegraph.metamorph.stream.readers;

import java.util.Set;


public interface ReaderFactory {
	Reader newReader(final String format);
	boolean isFormatSupported(final String format);
	Set<String> getSupportedFormats();
}
