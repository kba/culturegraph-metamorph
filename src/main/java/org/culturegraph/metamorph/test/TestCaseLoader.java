/**
 * 
 */
package org.culturegraph.metamorph.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.culturegraph.metamorph.util.ResourceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class TestCaseLoader {
	
	/**
	 * 
	 */
	private static final String FILE_NOT_FOUND = "Could not find test case file: ";

	private static final String SCHEMA_FILE = "metamorph-test.xsd";
	
	private static final String TEST_CASE_TAG = "test-case";
	
	private TestCaseLoader() {
		// No instances allowed
	}
	
	public static List<TestCase> load(final String testDef) {
		try {
			return load(ResourceUtil.getStream(testDef));
		} catch (FileNotFoundException e) {
			throw new TestConfigurationException(FILE_NOT_FOUND + testDef, e);
		}
	}
	
	public static List<TestCase> load(final File testDefFile) {
		try {
			return load(ResourceUtil.getStream(testDefFile));
		} catch (FileNotFoundException e) {
			throw new TestConfigurationException(FILE_NOT_FOUND + testDefFile, e);
		}
	}
	
	public static List<TestCase> load(final InputStream inputStream) {
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
			final Document doc = builder.parse(inputStream);
			
			final List<TestCase> testCases = new ArrayList<TestCase>();
			final NodeList testCaseNodes = doc.getElementsByTagName(TEST_CASE_TAG);
			for(int i=0; i < testCaseNodes.getLength(); ++i) {
				final Element testCaseElement = (Element) testCaseNodes.item(i);
				testCases.add(new TestCase(testCaseElement));
			}
			
			return testCases;
			
		} catch (ParserConfigurationException e) {
			throw new TestConfigurationException("Parser configuration failed", e);
		} catch (SAXException e) {
			throw new TestConfigurationException("Parser error", e);
		} catch (IOException e) {
			throw new TestConfigurationException("Error while reading file", e);
		}
	}
}

