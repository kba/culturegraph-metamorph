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

import org.culturegraph.metamorph.readers.RawRecordReader;
import org.culturegraph.metamorph.readers.StreamSender;
import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * @author Markus Michael Geipel
 * @status Experimental
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

	public static Metamorph build(final StreamSender reader, final InputSource inputSource,
			final StreamReceiver outputReceiver) {
		final Metamorph metamorph = build(inputSource, outputReceiver);
		reader.setStreamReceiver(metamorph);
		return metamorph;
	}

	public static Metamorph build(final StreamSender reader, final File file, final StreamReceiver outputReceiver) {
		try {
			return build(reader, new InputSource(new FileInputStream(file)), outputReceiver);
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(NOT_FOUND_ERROR, e);
		}
	}

	public static Metamorph build(final StreamSender reader, final InputStream inputStream,
			final StreamReceiver outputReceiver) {
		return build(reader, new InputSource(inputStream), outputReceiver);
	}

	public static Metamorph build(final InputSource inputSource, final StreamReceiver outputReceiver) {
		final Metamorph metamorph = new Metamorph();
		metamorph.setOutputStreamReceiver(outputReceiver);
		final MetamorphDefinitionHandler transformationContentHandler = new MetamorphDefinitionHandler(metamorph);
		loadDefinition(transformationContentHandler, inputSource);
		return metamorph;
	}

	public static Metamorph build(final File file, final StreamReceiver outputReceiver) {
		try {
			return build(new InputSource(new FileInputStream(file)), outputReceiver);
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(NOT_FOUND_ERROR, e);
		}
	}

	public static Metamorph build(final InputStream inputStream, final StreamReceiver outputReceiver) {
		return build(new InputSource(inputStream), outputReceiver);
	}

	private static void loadDefinition(final MetamorphDefinitionHandler transformationContentHandler,
			final InputSource inputSource) {
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
			xmlReader.setErrorHandler(new MetamorphBuilderErrorHandler());

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
	}

	private static final class MetamorphBuilderErrorHandler implements ErrorHandler {
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
