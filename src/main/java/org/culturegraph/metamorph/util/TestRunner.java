/**
 * 
 */
package org.culturegraph.metamorph.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class TestRunner {
	
	private static final String SCHEMA_FILE = "metamorph-test.xsd";

	private static final String TEST_CASE_TAG = "test-case";
	private static final String OUTPUT_TAG = "output";
	private static final String METAMORPH_TAG = "metamorph";
	private static final String INPUT_TAG = "input";
	private static final String SRC_ATTR = "src";
	private static final String TYPE_ATTR = "type";

	private final Document doc;
	
	private TestRunner(final Document doc) {
		this.doc = doc;
	}

	public static void runTests(final String testDef) {
		final InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(testDef);
		if (inputStream == null) {
			runTests(new File(testDef));
		}
		runTests(new InputSource(inputStream));
	}
	
	public static void runTests(final File testDefFile) {
		try {
			runTests(new InputSource(new FileInputStream(testDefFile)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void runTests(final InputStream inputStream) {
		runTests(new InputSource(inputStream));
	}
	
	public static void runTests(final InputSource inputSource) {
		try {
			final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_FILE);
			final Schema schema = schemaFactory.newSchema(schemaUrl);
			
			final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringElementContentWhitespace(true);
			builderFactory.setIgnoringComments(true);
			builderFactory.setNamespaceAware(true);
			builderFactory.setCoalescing(true);
			builderFactory.setSchema(schema);
			
			final DocumentBuilder builder = builderFactory.newDocumentBuilder();
			
			final TestRunner runner = new TestRunner(builder.parse(inputSource));
			runner.run();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void run() {
		final NodeList testCases = doc.getElementsByTagName(TEST_CASE_TAG);
		for(int i=0; i < testCases.getLength(); ++i) {
			final Element testCase = (Element) testCases.item(i);
			runTestCase(testCase);
		}
	}
	
	private void runTestCase(final Element testCase) {
		final Element input = (Element) testCase.getElementsByTagName(INPUT_TAG);
		final Element metamorph = (Element) testCase.getElementsByTagName(METAMORPH_TAG);
		final Element output = (Element) testCase.getElementsByTagName(OUTPUT_TAG);
		
		if (input.hasAttribute(SRC_ATTR)) {
			// Open input file
		} else {
			// Read input from element content
		}
		
		if (metamorph.hasAttribute(SRC_ATTR)) {
			// Open metamorph file
		} else {
			// Read metamorph from element content
		}
		
		if (output.hasAttribute(SRC_ATTR)) {
			// Open output file
		} else {
			// Read output from element content
		}
	}
}
