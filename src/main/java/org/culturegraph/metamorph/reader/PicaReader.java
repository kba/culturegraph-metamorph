package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.bib.PicaDecoder;

/**
 * @author Christoph Böhme
 */
public class PicaReader extends ReaderBase {

	public PicaReader() {
		super(new PicaDecoder());
	}

}
