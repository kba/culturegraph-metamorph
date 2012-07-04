package org.culturegraph.metamorph.readers;

import org.culturegraph.metastream.converter.xml.CGXMLHandler;

/**
 * @author Christoph Böhme
 *
 */
public final class CGXMLReader extends XMLReaderBase {

	public CGXMLReader() {
		super(new CGXMLHandler());
	}

}
