package org.culturegraph.metamorph.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder {

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String SCHEMA_FILE = "metamorph.xsd";
	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	private static final String PARSE_ERROR = "Error parsing transformation definition: ";
	private static final String NOT_FOUND_ERROR = "Definition file not found";

	private MetamorphBuilder() {/* no instances exist */
	}

	public static Metamorph build(final File file) {
		try {
			return build(new InputSource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(NOT_FOUND_ERROR, e);
		}
	}

	public static Metamorph build(final InputStream inputStream) {
		return build(new InputSource(inputStream));
	}


	public static Metamorph build(final InputSource inputSource) {
		final Metamorph metamorph = new Metamorph();
		final MetamorphDefinitionHandler transformationContentHandler = new MetamorphDefinitionHandler(metamorph);
		
		try {
			// XMLReader erzeugen
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);

			final SAXParser saxParser = factory.newSAXParser();
			saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

			final URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_FILE);
			saxParser.setProperty(JAXP_SCHEMA_SOURCE, schemaUrl.toString());

			final XMLReader xmlReader = saxParser.getXMLReader();
			final MetamorphBuilderErrorHandler handler = new MetamorphBuilderErrorHandler();
			xmlReader.setErrorHandler(handler);

			xmlReader.setContentHandler(transformationContentHandler);

			// Parsen wird gestartet
			xmlReader.parse(inputSource);
		} catch (ParserConfigurationException e) {
			throw new MetamorphDefinitionException(e);
		} catch (IOException e) {
			throw new MetamorphDefinitionException(e);
		} catch (SAXException e) {
			throw new MetamorphDefinitionException(e);
		}
		
		return metamorph;
	}

	private static final class MetamorphBuilderErrorHandler implements ErrorHandler {

		protected MetamorphBuilderErrorHandler() {
			// to avoid synthetic accessor methods
		}

		@Override
		public void warning(final SAXParseException exception) throws SAXException {
			throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
		}

		@Override
		public void fatalError(final SAXParseException exception) throws SAXException {
			throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
		}

		@Override
		public void error(final SAXParseException exception) throws SAXException {
			throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
		}
	}
}
