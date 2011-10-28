package org.culturegraph.metamorph.readers;

import java.util.Collections;
import java.util.Set;

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
