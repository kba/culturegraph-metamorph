package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.GenericXmlHandler;


/**
 * reads XML Lido
 * @author Markus Michael Geipel
 * 
 */
public class LidoReader extends XMLReaderBase {
	public LidoReader() {
		super(new GenericXmlHandler("lido"));
	}
}
