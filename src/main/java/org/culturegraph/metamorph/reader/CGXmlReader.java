package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.CGXmlHandler;

/**
 * @author Christoph Böhme
 *
 */
public final class CGXmlReader extends XmlReaderBase {

	public CGXmlReader() {
		super(new CGXmlHandler());
	}

}
