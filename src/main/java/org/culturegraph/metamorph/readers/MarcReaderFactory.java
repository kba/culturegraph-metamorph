package org.culturegraph.metamorph.readers;


public final class MarcReaderFactory implements ReaderFactory {

	@Override
	public RawRecordReader newReader() {
		return new MarcReader();
	}

}
