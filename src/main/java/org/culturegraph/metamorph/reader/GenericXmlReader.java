package org.culturegraph.metamorph.reader;

import org.culturegraph.metastream.converter.xml.GenericXmlHandler;


/**
 * @author Markus Michael Geipel
 * 
 */

public class GenericXmlReader extends XmlReaderBase {
	public GenericXmlReader(final String recordTag) {
		super(new GenericXmlHandler(recordTag));
	}
}
