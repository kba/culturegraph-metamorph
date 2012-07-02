package org.culturegraph.metamorph.stream.readers;

import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Markus Michael Geipel
 * 
 */
public class XMLStreamReader extends AbstractXMLReader {
	
	private static final Pattern TABS = Pattern.compile("\t+");
	private final String recordTagName;
	private boolean inRecord;
	private StringBuilder valueBuffer = new StringBuilder();
	
	public XMLStreamReader(final String recordTagName) {
		super();
		this.recordTagName = recordTagName;
	}

	public XMLStreamReader() {
		super();
		this.recordTagName = System.getProperty("org.culturegraph.metamorph.xml.recordtag");
		if (recordTagName == null) {
			throw new MetamorphException("Missing name for the tag marking a record.");
		}
	}

	
	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
			throws SAXException {

		writeValue();

		valueBuffer = new StringBuilder();
		if (inRecord) {
			getReceiver().startEntity(localName);
		} else if (localName.equals(recordTagName)) {
			final String identifier = attributes.getValue("id");
			if (identifier == null) {
				getReceiver().startRecord(null);
			} else {
				getReceiver().startRecord(identifier);
			}
			inRecord = true;
		}
		writeAttributes(attributes);
	}

	private void writeValue() {
		final String value = valueBuffer.toString();
		if (!value.trim().isEmpty()) {
			getReceiver().literal("value", value.replace('\n', ' '));
		}

	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException {
		writeValue();
		valueBuffer = new StringBuilder();
		if (inRecord) {
			if (localName.equals(recordTagName)) {
				inRecord = false;
				getReceiver().endRecord();
			} else {
				getReceiver().endEntity();
			}
		}
	}

	@Override
	public final void characters(final char[] chars, final int start, final int length) throws SAXException {
			valueBuffer.append(TABS.matcher(new String(chars, start, length)).replaceAll(""));
	}


	private void writeAttributes(final Attributes attributes) {
		final int length = attributes.getLength();

		for (int i = 0; i < length; ++i) {
			final String name = attributes.getLocalName(i);
			final String value = attributes.getValue(i);
			getReceiver().literal(name, value);
		}
	}

}
