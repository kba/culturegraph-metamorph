package org.culturegraph.metamorph.util;

import org.culturegraph.metamorph.readers.MabReader;
import org.culturegraph.metamorph.readers.RawRecordReader;

public final class MabReaderFactory implements ReaderFactory {

	@Override
	public RawRecordReader newReader() {
		return new MabReader();
	}

}
