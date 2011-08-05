package org.culturegraph.metamorph.util;

import org.culturegraph.metamorph.readers.RawRecordReader;

public interface ReaderFactory {
	RawRecordReader newReader();
}
