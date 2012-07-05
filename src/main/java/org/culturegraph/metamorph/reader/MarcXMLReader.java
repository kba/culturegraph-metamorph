package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.MarcXMLHandler;

/**
 * @author Markus Michael Geipel
 * 
 */

public class MarcXMLReader extends XMLReaderBase {
	public MarcXMLReader() {
		super(new MarcXMLHandler());
	}
}
