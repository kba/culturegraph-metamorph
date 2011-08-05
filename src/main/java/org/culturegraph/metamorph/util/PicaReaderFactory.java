package org.culturegraph.metamorph.util;

import org.culturegraph.metamorph.readers.PicaReader;
import org.culturegraph.metamorph.readers.RawRecordReader;

public final class PicaReaderFactory implements ReaderFactory {
	@Override
	public RawRecordReader newReader() {
		return new PicaReader();
	}
}
