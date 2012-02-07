package org.culturegraph.metamorph.core;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.culturegraph.metamorph.core.exceptions.MetamorphDefinitionException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Helper to load dom {@link Document}s in {@link MetamorphBuilder}.
 * 
 * @author Markus Michael Geipel
 *
 */
final class DomLoader {
	
	private static final ErrorHandler ERROR_HANDLER = new PrivateErrorHandler();
	
	private DomLoader() {
		// no instances
	}
	
	public static Document parse(final String schemaFile, final InputSource inputSource){
		try {
			return getDocumentBuilder(schemaFile).parse(inputSource);
		} catch (SAXException e) {
			throw new MetamorphDefinitionException(e);
		} catch (IOException e) {
			throw new MetamorphDefinitionException(e);
		}
	}
	
	private static DocumentBuilder getDocumentBuilder(final String schemaFile) {

		try {
			final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(schemaFile);
			if (schemaUrl == null) {
				throw new MetamorphDefinitionException("'" + schemaFile + "' not found!");
			}

			final Schema schema = schemaFactory.newSchema(schemaUrl);
			final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

			builderFactory.setIgnoringElementContentWhitespace(true);
			builderFactory.setIgnoringComments(true);
			builderFactory.setNamespaceAware(true);
			builderFactory.setCoalescing(true);
			builderFactory.setSchema(schema);

			final DocumentBuilder builder = builderFactory.newDocumentBuilder();
			builder.setErrorHandler(ERROR_HANDLER);

			return builder;

		} catch (ParserConfigurationException e) {
			throw new MetamorphDefinitionException(e);
		} catch (SAXException e) {
			throw new MetamorphDefinitionException(e);
		}
	}
	
	/**
	 * Error handler
	 */
	private static class PrivateErrorHandler implements ErrorHandler {
		private static final String PARSE_ERROR = "Error parsing xml: ";
		
		PrivateErrorHandler() {
			// to avoid synthetic accessor methods
		}

		@Override
		public void warning(final SAXParseException exception) throws SAXException {
			handle(exception);
		}

		@Override
		public void fatalError(final SAXParseException exception) throws SAXException {
			handle(exception);
		}

		@Override
		public void error(final SAXParseException exception) throws SAXException {
			handle(exception);
		}
		
		private void handle(final SAXParseException exception) {
			throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
		}
	}
}
