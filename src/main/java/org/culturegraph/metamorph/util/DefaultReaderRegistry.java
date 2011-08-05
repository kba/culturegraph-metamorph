package org.culturegraph.metamorph.util;

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
		this.addReaderFactory(ReaderRegistry.PICA, new PicaReaderFactory());
		this.addReaderFactory(ReaderRegistry.MAB2, new MabReaderFactory());
		this.addReaderFactory(ReaderRegistry.MARC21, new MarcReaderFactory());
	}
}
