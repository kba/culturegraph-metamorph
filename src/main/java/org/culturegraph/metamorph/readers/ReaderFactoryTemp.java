package org.culturegraph.metamorph.readers;

import java.util.Set;


public interface ReaderFactoryTemp {
	Reader newReader(final String format);
	boolean isFormatSupported(final String format);
	Set<String> getSupportedFormats();
}
