package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.MarcXmlHandler;

/**
 * @author Markus Michael Geipel
 * 
 */

public class MarcXmlReader extends XmlReaderBase {
	public MarcXmlReader() {
		super(new MarcXmlHandler());
	}
}
