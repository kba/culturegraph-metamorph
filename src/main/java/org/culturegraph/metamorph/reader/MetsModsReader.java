package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.GenericXmlHandler;


/**
 * @author Markus Michael Geipel
 * 
 */
public class MetsModsReader extends XmlReaderBase {
	public MetsModsReader() {
		super(new GenericXmlHandler("mets"));
	}
}
