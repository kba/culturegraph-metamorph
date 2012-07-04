package org.culturegraph.metamorph.readers;

import org.culturegraph.metastream.converter.bib.MabDecoder;

/**
 * @author Christoph Böhme
 */
public class MabReader extends ReaderBase {

	public MabReader() {
		super(new MabDecoder());
	}

}
