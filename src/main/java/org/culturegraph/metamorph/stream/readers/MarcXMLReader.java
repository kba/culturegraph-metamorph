package org.culturegraph.metamorph.stream.readers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Markus Michael Geipel
 * 
 */

//<marc:record>
//<marc:leader>01675nam a2200373 ib4500</marc:leader>
//<marc:controlfield tag="001">BV000023782</marc:controlfield>
//<marc:datafield tag="020" ind1=" " ind2=" "><marc:subfield code="a">3486510517</marc:subfield></marc:datafield>



public class MarcXMLReader extends AbstractXMLReader {

	private static final String SUBFIELD = "subfield";
	private static final String DATAFIELD = "datafield";
	private static final String CONTROLFIELD = "controlfield";
	private static final String RECORD = "record";
	private static final String LEADER = "leader";
	private String currentTag = "";
	private StringBuilder builder = new StringBuilder();



	@Override
	public final String getId(final String record) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
			throws SAXException {
			if(SUBFIELD.equals(localName)){
				currentTag = attributes.getValue("code");
			}else if(DATAFIELD.equals(localName)){
				getReceiver().startEntity(attributes.getValue("tag") + attributes.getValue("ind1") + attributes.getValue("ind2"));
			}else if(CONTROLFIELD.equals(localName)){
				currentTag = attributes.getValue(0);
			}else if(RECORD.equals(localName)){
				getReceiver().startRecord(null);
			}else if(LEADER.equals(localName)){
				currentTag = LEADER;
			}
	}



	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException {
		if(SUBFIELD.equals(localName)){
			getReceiver().literal(currentTag, builder.toString().trim());
			builder = new StringBuilder();
		}else if(DATAFIELD.equals(localName)){
			getReceiver().endEntity();
		}else if(CONTROLFIELD.equals(localName)){
			getReceiver().literal(currentTag, builder.toString().trim());
			builder = new StringBuilder();
		}else if(RECORD.equals(localName)){
			getReceiver().endRecord();
		}else if(LEADER.equals(localName)){
			getReceiver().literal(currentTag, builder.toString().trim());
			builder = new StringBuilder();
		}
	}

	@Override
	public final void characters(final char[] chars, final int start, final int length) throws SAXException {
		builder.append(chars, start, length);
	}
}
