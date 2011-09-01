package org.culturegraph.metamorph.readers;


public final class PicaReaderFactory implements ReaderFactory {
	@Override
	public RawRecordReader newReader() {
		return new PicaReader();
	}
}
