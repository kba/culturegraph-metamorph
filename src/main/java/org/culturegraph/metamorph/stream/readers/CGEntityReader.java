package org.culturegraph.metamorph.stream.readers;

import org.culturegraph.metastream.converter.CGEntityDecoder;

/**
 * @author Christoph Böhme
 */
public class CGEntityReader extends ReaderBase {

	public CGEntityReader() {
		super(new CGEntityDecoder());
	}

}
