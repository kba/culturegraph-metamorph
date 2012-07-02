package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metastream.converter.bib.MabDecoder;

/**
 * @author Christoph Böhme
 */
public class MabReader extends ReaderBase {

	public MabReader() {
		super(new MabDecoder());
	}

}
