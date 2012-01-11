package org.culturegraph.metamorph.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder2 {

	private static final Logger LOG = LoggerFactory.getLogger(MetamorphBuilder2.class);
	private static final String SCHEMA_FILE = "metamorph2.xsd";
	private static final ErrorHandler ERROR_HANDLER = new MetamorphDefinitionParserErrorHandler();

	private final String morphDef;

	public MetamorphBuilder2(final String morphDef) {
		this.morphDef = morphDef;
	}

	public Metamorph build() {
		return build(morphDef);
	}

	public static Metamorph build(final String morphDef) {
		if (morphDef == null) {
			throw new IllegalArgumentException("'morphDef' must not be null");
		}
		final String morphDefPath = morphDef + ".xml";
		final InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(morphDefPath);
		if (inputStream == null) {
			return build(new File(morphDefPath));
		}
		return build(inputStream);
	}

	public static Metamorph build(final File file) {
		if (file == null) {
			throw new IllegalArgumentException("'file' must not be null");
		}
		try {
			return build(getDocumentBuilder().parse(file));
		} catch (SAXException e) {
			throw new MetamorphDefinitionException(e);
		} catch (IOException e) {
			throw new MetamorphDefinitionException(e);
		}
	}

	public static Metamorph build(final InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("'inputStream' must not be null");
		}
		return build(new InputSource(inputStream));
	}

	public static Metamorph build(final InputSource inputSource) {
		try {
			return build(getDocumentBuilder().parse(inputSource));
		} catch (SAXException e) {
			throw new MetamorphDefinitionException(e);
		} catch (IOException e) {
			throw new MetamorphDefinitionException(e);
		}
	}

	private static DocumentBuilder getDocumentBuilder() {

		try {

			final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_FILE);
			if(schemaUrl==null){
				throw new MetamorphDefinitionException(SCHEMA_FILE + " not found!");
			}
			
			final Schema schema = schemaFactory.newSchema(schemaUrl);
			final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

			builderFactory.setIgnoringElementContentWhitespace(true);
			builderFactory.setIgnoringComments(true);
			builderFactory.setNamespaceAware(true);
			builderFactory.setCoalescing(true);
			
			//builderFactory.setValidating(true);
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

	private static Metamorph build(final Document doc) {
		final Element e = doc.getDocumentElement();
		
		
		LOG.info(e.getNodeName() + " " + e.getTagName()  + " " + e.getPrefix()  + " " + e.getBaseURI()  + " " +e.getNodeType());
		
		
		return null;
	}
	
	public static void main(final String[] args){
		MetamorphBuilder2.build("ingest.pica");
	}
}
