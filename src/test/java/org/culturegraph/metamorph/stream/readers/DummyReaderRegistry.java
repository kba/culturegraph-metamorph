package org.culturegraph.metamorph.stream.readers;

import java.util.Collections;
import java.util.Set;

/**
 * dummy used by {@link ReaderFactoryTest}
 * 
 * @author Markus Michael Geipel
 *
 *
 */
public final class DummyReaderRegistry extends AbstractReaderFactory{

	@Override
	public Reader newReader(final String format) {
		return null;
	}

	@Override
	public boolean isFormatSupported(final String format) {
		return false;
	}

	@Override
	public Set<String> getSupportedFormats() {
		return Collections.emptySet();
	}

}
