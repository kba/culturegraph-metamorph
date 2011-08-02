package org.culturegraph.metamorph.util;

import org.culturegraph.metamorph.readers.MabReader;
import org.culturegraph.metamorph.readers.MarcReader2;
import org.culturegraph.metamorph.readers.PicaReader;
import org.culturegraph.metamorph.readers.RawRecordReader;

/**
 * {@link ReaderRegistry} with pica, mab2 and marc21 {@link RawRecordReader}s already registered.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class DefaultReaderRegistry extends ReaderRegistry {

	public DefaultReaderRegistry() {
		super();
		this.addReader(ReaderRegistry.PICA, new PicaReader());
		this.addReader(ReaderRegistry.MAB2, new MabReader());
		this.addReader(ReaderRegistry.MARC21, new MarcReader2());
	}
}
