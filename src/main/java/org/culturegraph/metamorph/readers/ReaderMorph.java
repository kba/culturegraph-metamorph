package org.culturegraph.metamorph.readers;

import java.io.IOException;
import java.io.InputStream;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.core.MetamorphErrorHandler;
import org.culturegraph.metamorph.stream.StreamReceiver;

public final class ReaderMorph implements Reader {

	private final Reader reader;
	private final Metamorph metamorph;

	public ReaderMorph(final Reader reader, final Metamorph metamorph) {
		this.reader = reader;
		this.metamorph = metamorph;
	}
	
	public void setErrorHandler(final MetamorphErrorHandler errorHandler){
		metamorph.setErrorHandler(errorHandler);
	}
	
	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void read(final InputStream inputStream) throws IOException {
		reader.read(inputStream);

	}

	@Override
	public void read(final String entry) {
		reader.read(entry);

	}

	@Override
	public String getId(final String record) {
		return reader.getId(record);
	}

}
