package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.functions.Function;
import org.culturegraph.metamorph.functions.FunctionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Handles the SAX messages
 */
final class MetamorphDefinitionHandler implements ContentHandler {

	private static final String GROUP_TAG = "group";
	private static final String DATA_TAG = "data";
	private static final String COLLECT_LITERAL_TAG = "collect-literal";
	private static final String COLLECT_ENTITY_TAG = "collect-entity";
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
	private SimpleKeyValueStore keyValueStore;

	public MetamorphDefinitionHandler(final Metamorph metamorph) {
		this.metamorph = metamorph;
	}


	@Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
			throws SAXException {

		if (COLLECT_ENTITY_TAG.equals(localName) || COLLECT_LITERAL_TAG.equals(localName)) {
			registerCollector(localName, atts);

		} else if (GROUP_TAG.equals(localName)) {
			emitGroupName = atts.getValue(NAME_ATTR);
			emitGroupValue = atts.getValue(VALUE_ATTR);

		} else if (DATA_TAG.equals(localName)) {
			registerDataSource(atts.getValue(SOURCE_ATTR), atts.getValue(NAME_ATTR), atts.getValue(VALUE_ATTR),
					atts.getValue(AS_ATTR), atts.getValue(OCCURENCE_ATTR));

		} else if (MAP_TAG.equals(localName)) {
			createKeyValueStore(atts.getValue(NAME_ATTR), atts.getValue(DEFAULT_ATTR));

		} else if (functionFactory.getAvailableFunctions().contains(localName)) {
			registerFunction(localName, attributesToMap(atts));

		} else if (ENTRY_TAG.equals(localName)) {
			keyValueStore.put(atts.getValue(NAME_ATTR), atts.getValue(VALUE_ATTR));

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
		} else {
			collect = new CollectLiteral(metamorph);
		}

		collect.setName(atts.getValue(NAME_ATTR));
		collect.setValue(atts.getValue(VALUE_ATTR));
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
		} else if (MAP_TAG.equals(localName)){
			keyValueStore = null;
		}
	}

	/**
	 * @param name
	 * @param attributes
	 */
	private void registerFunction(final String name, final Map<String, String> attributes) {

		final Function function = functionFactory.newFunction(name, attributes);
		function.setKeyValueStoreAggregator(metamorph);

		if (collect instanceof DataProcessor && data == null) {
			((DataProcessor) collect).addFunction(function);
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
	 * @param name
	 *            of {@link KeyValueStore}
	 */
	private void createKeyValueStore(final String name, final String defaultValue) {
		keyValueStore = new SimpleKeyValueStore();
		keyValueStore.setDefaultValue(defaultValue);
		metamorph.addKeyValueStore(name, keyValueStore);

		if (LOG.isDebugEnabled()) {
			LOG.debug("new KeyValueStore: " + name);
		}
	}

	/**
	 * @param occurence
	 * @param defaultName
	 */
	private void registerDataSource(final String source, final String name, final String value, final String mode,
			final String occurence) {

		data = new Data();

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

		if (NAME_ATTR.equals(mode)) {
			data.setMode(Data.Mode.AS_NAME);
		} else {
			data.setMode(Data.Mode.AS_VALUE);
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