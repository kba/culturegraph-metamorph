package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.GenericXMLHandler;


/**
 * @author Markus Michael Geipel
 * 
 */
public class MetsModsReader extends XMLReaderBase {
	public MetsModsReader() {
		super(new GenericXMLHandler("mets"));
	}
}
