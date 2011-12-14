package org.culturegraph.metamorph.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.culturegraph.metamorph.core.Data.Mode;
import org.culturegraph.metamorph.functions.Function;
import org.culturegraph.metamorph.functions.FunctionFactory;
import org.culturegraph.metamorph.multimap.MultiMapProvider;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.stream.StreamSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
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

	private final String morphDef;

	public MetamorphBuilder(final String morphDef) {
		// TODO caching the definition would be cool!
		this.morphDef = morphDef;
	}

	public Metamorph build() {
		return build(morphDef);
	}

	public static void wire(final StreamSender sender, final Metamorph metamorph, final StreamReceiver receiver) {
		sender.setStreamReceiver(metamorph);
		metamorph.setStreamReceiver(receiver);
	}

	public static Metamorph build(final File file) {
		try {
			return build(new InputSource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(NOT_FOUND_ERROR, e);
		}
	}

	public static Metamorph build(final String morphDef) {
		final String morphDefPath = morphDef + ".xml";
		final InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(morphDefPath);
		if (inputStream == null) {
			return build(new File(morphDefPath));
			// throw new MetamorphDefinitionException(NOT_FOUND_ERROR + ": " +
			// morphDefPath);
		}
		return build(inputStream);
	}

	public static Metamorph build(final InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("'inputStream' must not be null");
		}
		return build(new InputSource(inputStream));
	}

	public static Metamorph build(final InputSource inputSource) {
		if (inputSource == null) {
			throw new IllegalArgumentException("'inputSource' must not be null");
		}
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

	private static final class MetamorphDefinitionHandler implements ContentHandler {

		private static final String GROUP_TAG = "group";
		private static final String DATA_TAG = "data";
		private static final String COLLECT_LITERAL_TAG = "collect-literal";
		private static final String COLLECT_ENTITY_TAG = "collect-entity";
		private static final String CHOOSE_LITERAL_TAG = "choose-literal";
		private static final String MAP_TAG = "map";
		private static final String ENTRY_TAG = "entry";
		private static final String NAME_ATTR = "name";
		private static final String VALUE_ATTR = "value";
		private static final String SOURCE_ATTR = "source";
		private static final String AS_ATTR = "as";
		private static final String TRUE = "true";
		// private static final String FALSE = "false";
		private static final String RESET_ATTR = "reset";
		private static final String SAME_ENTITY_ATTR = "sameEntity";
		private static final String FORCE_ON_END_ATTR = "forceOnEnd";
		private static final String DEFAULT_ATTR = "default";

		private static final Logger LOG = LoggerFactory.getLogger(MetamorphDefinitionHandler.class);
		private static final Object METAMORPH_TAG = "metamorph";
		private static final String MARKER_ATTR = "entityMarker";
		private static final String OCCURENCE_ATTR = "occurence";

		private String emitGroupName;
		private String emitGroupValue;

		private final Metamorph metamorph;

		private final FunctionFactory functionFactory = new FunctionFactory();

		private Data data;
		private Collect collect;
		private Map<String, String> map;

		public MetamorphDefinitionHandler(final Metamorph metamorph) {
			this.metamorph = metamorph;
		}

		@Override
		public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
				throws SAXException {
			if (DATA_TAG.equals(localName)) {
				registerDataSource(atts.getValue(SOURCE_ATTR), atts.getValue(NAME_ATTR), atts.getValue(VALUE_ATTR),
						atts.getValue(AS_ATTR), atts.getValue(OCCURENCE_ATTR));
			} else if (GROUP_TAG.equals(localName)) {
				emitGroupName = atts.getValue(NAME_ATTR);
				emitGroupValue = atts.getValue(VALUE_ATTR);

			} else if (COLLECT_ENTITY_TAG.equals(localName) || COLLECT_LITERAL_TAG.equals(localName) || CHOOSE_LITERAL_TAG.equals(localName)) {
				registerCollector(localName, atts);
				
			} else if (MAP_TAG.equals(localName)) {
				createMap(atts.getValue(NAME_ATTR), atts.getValue(DEFAULT_ATTR));

			} else if (functionFactory.getAvailableFunctions().contains(localName)) {
				registerFunction(localName, attributesToMap(atts));

			} else if (ENTRY_TAG.equals(localName)) {
				map.put(atts.getValue(NAME_ATTR), atts.getValue(VALUE_ATTR));

			} else if (METAMORPH_TAG.equals(localName)) {
				initMetamorph(atts.getValue(MARKER_ATTR));
			}
		}

		private void initMetamorph(final String marker) {
			if (null != marker) {
				metamorph.setEntityMarker(marker.charAt(0));
			}
		}

		private void registerCollector(final String tag, final Attributes atts) {
			final boolean reset = atts.getValue(RESET_ATTR) != null && TRUE.equals(atts.getValue(RESET_ATTR));
			final boolean sameEntity = atts.getValue(SAME_ENTITY_ATTR) != null
					&& TRUE.equals(atts.getValue(SAME_ENTITY_ATTR));

			if (COLLECT_ENTITY_TAG.equals(tag)) {
				collect = new CollectEntity(metamorph);
			} else if (COLLECT_LITERAL_TAG.equals(tag)) {
				collect = new CollectLiteral(metamorph);
			} else if (CHOOSE_LITERAL_TAG.equals(tag)) {
				collect = new ChooseLiteral(metamorph);
			}

			if (emitGroupName == null) {
				collect.setName(atts.getValue(NAME_ATTR));
			} else {
				collect.setName(emitGroupName);
			}

			if (emitGroupValue == null) {
				collect.setValue(atts.getValue(VALUE_ATTR));
			} else {
				collect.setValue(emitGroupValue);
			}
//			
//			collect.setName();
//			collect.setValue(atts.getValue(VALUE_ATTR));
//			
			
			collect.setReset(reset);
			collect.setSameEntity(sameEntity);

			if (atts.getValue(FORCE_ON_END_ATTR) != null) {
				metamorph.addEntityEndListener(collect, atts.getValue(FORCE_ON_END_ATTR));
			}
		}

		@Override
		public void endElement(final String uri, final String localName, final String qName) throws SAXException {
			if (DATA_TAG.equals(localName)) {
				data = null;
			} else if (COLLECT_ENTITY_TAG.equals(localName) || COLLECT_LITERAL_TAG.equals(localName)) {
				collect = null;
			} else if (GROUP_TAG.equals(localName)) {
				emitGroupName = null;
				emitGroupValue = null;
			} else if (MAP_TAG.equals(localName)) {
				map = null;
			}
		}

		/**
		 * @param name
		 * @param attributes
		 */
		private void registerFunction(final String name, final Map<String, String> attributes) {

			final Function function = functionFactory.newFunction(name, attributes);
			function.setMultiMapProvider(metamorph);

			if (collect instanceof ValueProcessor && data == null) {
				((ValueProcessor) collect).addFunction(function);
			} else if (data != null) {
				data.addFunction(function);
			}

			if (LOG.isTraceEnabled()) {
				LOG.trace("\tfunction " + function.getClass().getSimpleName());
			}
		}

		/**
		 * @param atts
		 * @return
		 */
		private Map<String, String> attributesToMap(final Attributes atts) {
			final Map<String, String> attributes = new HashMap<String, String>();
			for (int i = 0; i < atts.getLength(); ++i) {
				attributes.put(atts.getLocalName(i), atts.getValue(i));
			}
			return attributes;
		}

		/**
		 * @param mapName
		 *            of map
		 */
		private void createMap(final String mapName, final String defaultValue) {
			map = new HashMap<String, String>();
			if (defaultValue != null) {
				map.put(MultiMapProvider.DEFAULT_MAP_KEY, defaultValue);
			}
			metamorph.addMap(mapName, map);

			if (LOG.isDebugEnabled()) {
				LOG.debug("new map: " + mapName);
			}
		}

		/**
		 * @param occurence
		 * @param defaultName
		 */
		private void registerDataSource(final String source, final String name, final String value, final String mode,
				final String occurence) {

			data = new Data(source);

			if (occurence != null) {
				data.setOccurence(Integer.parseInt(occurence));
			}

			if (emitGroupName == null) {
				data.setName(name);
			} else {
				data.setName(emitGroupName);
			}

			if (emitGroupValue == null) {
				data.setValue(value);
			} else {
				data.setValue(emitGroupValue);
			}

			if (collect == null) {
				data.setDataReceiver(metamorph);
			} else {
				collect.addData(data);
			}
			
			final Data.Mode dataMode = Data.Mode.valueOf(mode.toUpperCase(Locale.US));
			if(dataMode==null){
				data.setMode(Mode.VALUE);
			}else{
				data.setMode(dataMode);
			}

			if(dataMode==Mode.COUNT){
				metamorph.addEntityEndListener(data, Metamorph.RECORD_KEYWORD);
			}
			
			metamorph.registerDataSource(data, source);

			if (LOG.isDebugEnabled()) {
				String receiver = "";
				if (data.getDataReceiver() instanceof Collect) {
					final Collect collect = (Collect) data.getDataReceiver();
					receiver = "-> " + collect.getClass().getSimpleName() + " " + collect.getName();
				}
				LOG.debug(source + " -> (" + data.getDefaultName() + ", " + data.getDefaultValue() + ") " + receiver);
			}
		}

		@Override
		public void endPrefixMapping(final String prefix) throws SAXException {
			// do nothing
		}

		@Override
		public void startDocument() throws SAXException {/* do nothing */
		}

		@Override
		public void endDocument() throws SAXException {/* do nothing */
		}

		@Override
		public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
			// do nothing
		}

		@Override
		public void setDocumentLocator(final Locator locator) {
			// do nothing
		}

		@Override
		public void skippedEntity(final String name) throws SAXException {
			// do nothing
		}

		@Override
		public void characters(final char[] cha, final int start, final int length) throws SAXException {
			// do nothing
		}

		@Override
		public void ignorableWhitespace(final char[] cha, final int start, final int length) throws SAXException {
			// do nothing
		}

		@Override
		public void processingInstruction(final String target, final String data) throws SAXException {
			// do nothing
		}
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

	public static final class MetamorphDefinitionException extends RuntimeException {

		private static final long serialVersionUID = -3130648074493084946L;

		/**
		 * @param message
		 */
		public MetamorphDefinitionException(final String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		public MetamorphDefinitionException(final Throwable cause) {
			super(cause);
		}

		/**
		 * @param message
		 * @param cause
		 */
		public MetamorphDefinitionException(final String message, final Throwable cause) {
			super(message, cause);
		}

	}
}
