package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.MarcXmlHandler;

/**
 * @author Markus Michael Geipel
 * 
 */

public class MarcXMLReader extends XMLReaderBase {
	public MarcXMLReader() {
		super(new MarcXmlHandler());
	}
}
