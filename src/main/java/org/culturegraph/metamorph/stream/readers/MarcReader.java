package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metastream.converter.bib.MarcDecoder;

/**
 * @author Christoph Böhme
 */
public class MarcReader extends ReaderBase {

	public MarcReader() {
		super(new MarcDecoder());
	}

}
