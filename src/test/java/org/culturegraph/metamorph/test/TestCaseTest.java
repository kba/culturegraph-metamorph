package org.culturegraph.metamorph.test;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public final class TestCaseTest {

	private static final String TEST_CASE_TAG = "test-case";
	private static final String INPUT_TAG = "input";
	private static final String TRANSFORMATION_TAG = "transformation";
	private static final String RESULT_TAG = "result";
	
	private static final String NAME_ATTR = "name";
	private static final String TYPE_ATTR = "type";
	private static final String SRC_ATTR = "src";
	
	private Document doc;
	
	@Before
	public void createDOMBuilder() throws ParserConfigurationException {
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();
	}
	
	@Test
	public void testNonEmptyInputWithSrc() throws IOException {
		final Element config = doc.createElement(TEST_CASE_TAG);
		config.setAttribute(NAME_ATTR, "test");
		
		Element element = doc.createElement(INPUT_TAG);
		element.setAttribute(TYPE_ATTR, "application/x-cgentity");
		element.setAttribute(SRC_ATTR, "test/simple-input.cge");
		element.appendChild(doc.createTextNode("This data should not be here"));
		config.appendChild(element);
		
		element = doc.createElement(TRANSFORMATION_TAG);
		element.setAttribute(SRC_ATTR, "test/simple-transformation.xml");
		config.appendChild(element);
		
		element = doc.createElement(RESULT_TAG);
		element.setAttribute(SRC_ATTR, "test/simple-result.xml");
		config.appendChild(element);
		
		final TestCase testCase = new TestCase(config);
		testCase.run();
	}
}
