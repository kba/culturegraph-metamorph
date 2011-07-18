package org.culturegraph.metamorph.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.springframework.util.Assert;
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

	private String definitionFile;

	private MetamorphDefinitionHandler transformationContentHandler;
	private StreamReceiver outputReceiver;

	
		
	/**
	 * @param outputReceiver 
	 */
	public void setOutputHandler(final StreamReceiver outputReceiver) {
		Assert.notNull("'outputHandler' must not be null");
		this.outputReceiver = outputReceiver;
	}


	/**
	 * @param definitionFile
	 */
	public void setDefinitionFile(final String definitionFile) {
		this.definitionFile = definitionFile;
	}
	
	public Metamorph build(){
		final Metamorph metamorph = new Metamorph();
		metamorph.setOutputStreamReceiver(outputReceiver);
		transformationContentHandler = new MetamorphDefinitionHandler(metamorph);
		loadDefinition();
		return metamorph;
	}

	private void loadDefinition(){
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
			xmlReader.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(final SAXParseException exception)
						throws SAXException {
					throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
				}

				@Override
				public void fatalError(final SAXParseException exception)
						throws SAXException {
					throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
				}

				@Override
				public void error(final SAXParseException exception)
						throws SAXException {
					throw new MetamorphDefinitionException(PARSE_ERROR+ exception.getMessage(), exception);
				}
			});

			// Pfad zur XML Datei
			final FileReader reader = new FileReader(definitionFile);
			final InputSource inputSource = new InputSource(reader);

			xmlReader.setContentHandler(transformationContentHandler);

			// Parsen wird gestartet
			xmlReader.parse(inputSource);
		} catch (ParserConfigurationException e) {
			throw new MetamorphDefinitionException(e);
		}catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(e);
		} catch (IOException e) {
			throw new MetamorphDefinitionException(e);
		} catch (SAXException e) {
			throw new MetamorphDefinitionException(e);
		}
	}
}
