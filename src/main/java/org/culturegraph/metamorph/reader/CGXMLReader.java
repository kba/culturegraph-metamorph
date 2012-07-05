package org.culturegraph.metamorph.reader;

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