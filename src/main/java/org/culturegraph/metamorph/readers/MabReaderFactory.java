package org.culturegraph.metamorph.readers;


public final class MabReaderFactory implements ReaderFactory {

	@Override
	public RawRecordReader newReader() {
		return new MabReader();
	}

}
