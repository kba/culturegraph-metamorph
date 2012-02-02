/**
 * 
 */
package org.culturegraph.metamorph.test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.stream.readers.CGXmlReader;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.receivers.EventStreamValidator;
import org.culturegraph.metamorph.stream.receivers.EventStreamWriter;
import org.culturegraph.metamorph.util.MimeTypeUtil;
import org.culturegraph.metamorph.util.ResourceUtil;
import org.culturegraph.metamorph.util.XMLUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class TestCase {
	
	private static final String NO_DATA_FOUND = 
			"Please specify either element content or a src attribute";
	private static final String CONTENT_MUST_BE_EMTPY = 
			"Element content must be empty when src attribute is used.";
	
	private static final String METAMORPH_NS = 
			"http://www.culturegraph.org/metamorph";
	private static final String METAMORPH_TEST_NS = 
			"http://www.culturegraph.org/metamorph-test";
	
	private static final String NAME_ATTR = "name";
	private static final String IGNORE_ATTR = "ignore";
	private static final String RESULT_TAG = "result";
	private static final String METAMORPH_TAG = "metamorph";
	private static final String INPUT_TAG = "input";
	private static final String SRC_ATTR = "src";
	private static final String TYPE_ATTR = "type";
	private static final String STRICT_RECORD_ORDER_ATTR = "strict-record-order";
	private static final String STRICT_KEY_ORDER_ATTR = "strict-key-order";
	private static final String STRICT_VALUE_ORDER_ATTR = "strict-value-order";

	private final Element config;
	
	private final Reader reader;
	private final Metamorph metamorph;
		
	public TestCase(final Element config) {
		this.config = config;
		reader = getReader();
		metamorph = getMetamorph();
	}

	public String getName() {
		return config.getAttribute(NAME_ATTR);
	}

	public boolean isIgnore() {
		return Boolean.parseBoolean(config.getAttribute(IGNORE_ATTR));
	}

	public void run() throws IOException {
		
		final EventStreamWriter resultStream = new EventStreamWriter();
		if (metamorph == null) {
			reader.setReceiver(resultStream);
		} else {
			reader.setReceiver(metamorph).setReceiver(resultStream);
		}
		
		resultStream.resetStream();
		reader.read(getInputData());
		resultStream.endStream();
		
		final EventStreamValidator validator = 
				new EventStreamValidator(resultStream.getEventStream());
		
		validator.setStrictRecordOrder(Boolean.parseBoolean(
				config.getAttribute(STRICT_RECORD_ORDER_ATTR)));
		validator.setStrictKeyOrder(Boolean.parseBoolean(
				config.getAttribute(STRICT_KEY_ORDER_ATTR)));
		validator.setStrictValueOrder(Boolean.parseBoolean(
				config.getAttribute(STRICT_VALUE_ORDER_ATTR)));
		
		final CGXmlReader resultReader = new CGXmlReader();
		resultReader.setReceiver(validator);
		
		validator.resetStream();
		resultReader.read(getExpectedResult());
		validator.endStream();	
	}

	
	private Reader getReader() {		
		final Element input = (Element) config.getElementsByTagName(INPUT_TAG).item(0);
		final String mimeType = input.getAttribute(TYPE_ATTR);
		return MimeTypeUtil.getReaderForMimeType(mimeType);
	}
	
	private Metamorph getMetamorph() {
		Metamorph metamorph = null;
		
		NodeList nodes = config.getElementsByTagNameNS(METAMORPH_TEST_NS, METAMORPH_TAG);
		if (nodes.getLength() != 0) {
			final Element element = (Element) nodes.item(0);
			metamorph = MetamorphBuilder.build(element.getAttribute(SRC_ATTR));
		}
		
		nodes = config.getElementsByTagNameNS(METAMORPH_NS, METAMORPH_TAG);
		if (nodes.getLength() != 0) {
			final Element morphDef = (Element) nodes.item(0);
			final String string = XMLUtil.nodeToString(morphDef);
			metamorph = MetamorphBuilder.build(new ByteArrayInputStream(string.getBytes()));
			//TODO: more elegance please
		}
		
		return metamorph;
	}
	
	private java.io.Reader getInputData() {
		final java.io.Reader reader;
		
		final Element input = (Element) config.getElementsByTagName(INPUT_TAG).item(0);
		final String inputType = input.getAttribute(TYPE_ATTR);
		if (input.hasAttribute(SRC_ATTR)) {
			final String src = input.getAttribute(SRC_ATTR);
			if (input.getFirstChild() != null) {
				throw new TestConfigurationException(CONTENT_MUST_BE_EMTPY);
			}
			try {
				reader = ResourceUtil.getReader(src);
			} catch (FileNotFoundException e) {
				throw new TestConfigurationException("Could not find input file: " + src, e);
			}
		} else {
			if (input.hasChildNodes()) {
				if (MimeTypeUtil.isXmlMimeType(inputType)) {
					reader = new StringReader(XMLUtil.nodeListToString(input.getChildNodes()));
				} else {
					reader = new StringReader(input.getTextContent());
				}
			} else {
				throw new TestConfigurationException(NO_DATA_FOUND);
			}
		}
		
		return reader;
		
	}
	
	private java.io.Reader getExpectedResult() {
		final java.io.Reader reader;
		final Element result = (Element) config.getElementsByTagName(RESULT_TAG).item(0);
		if (result.hasAttribute(SRC_ATTR)) {
			final String src = result.getAttribute(SRC_ATTR);
			if (result.getFirstChild() != null) {
				throw new TestConfigurationException(CONTENT_MUST_BE_EMTPY);
			}
			try {
				reader = ResourceUtil.getReader(src);
			} catch (FileNotFoundException e) {
				throw new TestConfigurationException("Could not find expected result: " + src, e);
			}
		} else {
			if (result.hasChildNodes()) {
				reader = new StringReader(XMLUtil.nodeListToString(result.getChildNodes()));
			} else {
				throw new TestConfigurationException(NO_DATA_FOUND);
			}
		}
		
		return reader;
	}
	
}
