package org.culturegraph.metamorph.util;

import org.culturegraph.metamorph.readers.MarcReader;
import org.culturegraph.metamorph.readers.RawRecordReader;

public final class MarcReaderFactory implements ReaderFactory {

	@Override
	public RawRecordReader newReader() {
		return new MarcReader();
	}

}
