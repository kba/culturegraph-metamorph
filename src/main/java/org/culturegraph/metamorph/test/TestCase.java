/**
 * 
 */
package org.culturegraph.metamorph.test;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Collections;

import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.readers.ReaderFactory;
import org.culturegraph.metamorph.util.ResourceUtil;
import org.culturegraph.metamorph.util.XMLUtil;
import org.culturegraph.metastream.converter.xml.CGXMLHandler;
import org.culturegraph.metastream.converter.xml.XMLDecoder;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.framework.StreamReceiverPipe;
import org.culturegraph.metastream.sink.EventList;
import org.culturegraph.metastream.util.StreamValidator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class TestCase {
	
	private static final String NO_DATA_FOUND = 
			"Please specify either element content or a src attribute";
	
	private static final String NAME_ATTR = "name";
	private static final String IGNORE_ATTR = "ignore";
	private static final String RESULT_TAG = "result";
	private static final String TRANSFORMATION_TAG = "transformation";
	private static final String INPUT_TAG = "input";
	private static final String SRC_ATTR = "src";
	private static final String TYPE_ATTR = "type";
	private static final String STRICT_RECORD_ORDER_ATTR = "strict-record-order";
	private static final String STRICT_KEY_ORDER_ATTR = "strict-key-order";
	private static final String STRICT_VALUE_ORDER_ATTR = "strict-value-order";
	
	private static final ReaderFactory READER_FACTORY = new ReaderFactory();
	
	private final Element config;
	
	private final Reader reader;
	private final StreamReceiverPipe<StreamReceiver> transformation;
		
	public TestCase(final Element config) {
		this.config = config;
		reader = getReader();
		transformation = getTransformation();
	}

	public String getName() {
		return config.getAttribute(NAME_ATTR);
	}

	public boolean isIgnore() {
		return Boolean.parseBoolean(config.getAttribute(IGNORE_ATTR));
	}

	public void run() {
		
		final EventList resultStream = new EventList();
		if (transformation == null) {
			reader.setReceiver(resultStream);
		} else {
			reader.setReceiver(transformation).setReceiver(resultStream);
		}
		
		reader.read(getInputData());
		reader.close();
		
		final StreamValidator validator = 
				new StreamValidator(resultStream.getEvents());
		
		final Element result = (Element) config.getElementsByTagName(RESULT_TAG).item(0);
		validator.setStrictRecordOrder(Boolean.parseBoolean(
				result.getAttribute(STRICT_RECORD_ORDER_ATTR)));
		validator.setStrictKeyOrder(Boolean.parseBoolean(
				result.getAttribute(STRICT_KEY_ORDER_ATTR)));
		validator.setStrictValueOrder(Boolean.parseBoolean(
				result.getAttribute(STRICT_VALUE_ORDER_ATTR)));
		
		final XMLDecoder decoder = new XMLDecoder();
		decoder.setReceiver(new CGXMLHandler()).setReceiver(validator);
		
		decoder.process(getExpectedResult());
		validator.close();	
	}
	
	private Reader getReader() {		
		final Element input = (Element) config.getElementsByTagName(INPUT_TAG).item(0);
		final String mimeType = input.getAttribute(TYPE_ATTR);
		return READER_FACTORY.newInstance(mimeType, Collections.<String, String>emptyMap());
	}
	
	private StreamReceiverPipe<StreamReceiver> getTransformation() {
		final NodeList nodes = config.getElementsByTagName(TRANSFORMATION_TAG);
		if (nodes.getLength() == 0) {
			return null;			
		}
		final Element transformation = (Element) nodes.item(0);
		
		final java.io.Reader ioReader;
		if (transformation.hasAttribute(SRC_ATTR)) {
			ioReader = getDataFromSource(transformation.getAttribute(SRC_ATTR));
		} else {
			ioReader = getDataEmbedded(transformation);
		}

		return MetamorphBuilder.build(ioReader);
	}
	
	private java.io.Reader getInputData() {
		final Element input = (Element) config.getElementsByTagName(INPUT_TAG).item(0);
		
		if (input.hasAttribute(SRC_ATTR)) {
			return getDataFromSource(input.getAttribute(SRC_ATTR));
		}
		return getDataEmbedded(input);
	}
	
	private java.io.Reader getExpectedResult() {
		final Element result = (Element) config.getElementsByTagName(RESULT_TAG).item(0);
		if (result.hasAttribute(SRC_ATTR)) {
			return getDataFromSource(result.getAttribute(SRC_ATTR));
		}		
		return getDataEmbedded(result);
	}
	
	private java.io.Reader getDataFromSource(final String src) {
		try {
			return ResourceUtil.getReader(src);
		} catch (FileNotFoundException e) {
			throw new TestConfigurationException("Could not find input file: " + src, e);
		}		
	}
	
	private java.io.Reader getDataEmbedded(final Element input) {
		final String inputType = input.getAttribute(TYPE_ATTR);
		if (input.hasChildNodes()) {
			if (XMLUtil.isXmlMimeType(inputType)) {
				return new StringReader(XMLUtil.nodeListToString(input.getChildNodes()));
			}
			return new StringReader(input.getTextContent());
		}
			
		throw new TestConfigurationException(NO_DATA_FOUND);
	}
	
	
	
}
