package org.culturegraph.metamorph.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.functions.Compose;
import org.culturegraph.metamorph.functions.Constant;
import org.culturegraph.metamorph.functions.Function;
import org.culturegraph.metamorph.functions.ISBN;
import org.culturegraph.metamorph.functions.Lookup;
import org.culturegraph.metamorph.functions.Regexp;
import org.culturegraph.metamorph.functions.Replace;
import org.culturegraph.metamorph.functions.Substring;
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
	// private static final String REPROCESS_ATTR = "reprocess";
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

	// private static final String TNS = "http://www.dnb.de/transformation";
	private String emitGroupName;
	private String emitGroupValue;

	private final Metamorph metamorph;
	private final Map<String, Class<? extends Function>> functions = new HashMap<String, Class<? extends Function>>();
	private final Map<Class<? extends Function>, Map<String, Method>> functionMethodMaps = new HashMap<Class<? extends Function>, Map<String, Method>>();

	// private boolean doReprocess;
	private Data data;
	private Collect collect;
	private SimpleKeyValueStore keyValueStore;

	public MetamorphDefinitionHandler(final Metamorph metamorph) {
		this.metamorph = metamorph;
		registerFunctionClass("constant", Constant.class);
		registerFunctionClass("regexp", Regexp.class);
		registerFunctionClass("substring", Substring.class);
		registerFunctionClass("compose", Compose.class);
		registerFunctionClass("lookup", Lookup.class);
		registerFunctionClass("replace", Replace.class);
		registerFunctionClass("isbn", ISBN.class);
		if (LOG.isDebugEnabled()) {
			LOG.debug("available functions: " + functions.keySet());
		}
	}

	public void registerFunctionClass(final String name, final Class<? extends Function> functionClass) {
		functions.put(name, functionClass);
		final Map<String, Method> methodMap = new HashMap<String, Method>();
		functionMethodMaps.put(functionClass, methodMap);
		for (Method method : functionClass.getMethods()) {
			final String methodName = method.getName().replace("set", "").toLowerCase();
			methodMap.put(methodName, method);
		}
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
					atts.getValue(AS_ATTR));

		} else if (MAP_TAG.equals(localName)) {
			createKeyValueStore(atts.getValue(NAME_ATTR), atts.getValue(DEFAULT_ATTR));

		} else if (functions.keySet().contains(localName)) {
			registerFunction(localName, attributesToMap(atts));

		} else if (ENTRY_TAG.equals(localName)) {
			keyValueStore.put(atts.getValue(NAME_ATTR), atts.getValue(VALUE_ATTR));

		} else if (METAMORPH_TAG.equals(localName)) {
			final String marker = atts.getValue(MARKER_ATTR);
			if (null != marker) {
				metamorph.setEntityMarker(marker.charAt(0));
			}
		}
	}

	private void registerCollector(final String tag, final Attributes atts) {
		final boolean reset = atts.getValue(RESET_ATTR) != null && TRUE.equals(atts.getValue(RESET_ATTR));
		final boolean sameEntity = atts.getValue(SAME_ENTITY_ATTR) != null
				&& TRUE.equals(atts.getValue(SAME_ENTITY_ATTR));

		if (COLLECT_ENTITY_TAG.equals(tag)) {
			collect = new CollectEntity();
		} else {
			collect = new CollectLiteral();
		}
		collect.setStreamReceiver(metamorph.getOutputStreamReceiver());
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
		if (COLLECT_ENTITY_TAG.equals(localName) || COLLECT_LITERAL_TAG.equals(localName)) {
			collect = null;
		} else if (GROUP_TAG.equals(localName)) {
			emitGroupName = null;
			emitGroupValue = null;
		}
	}

	/**
	 * @param name
	 * @param attributes
	 */
	private void registerFunction(final String name, final Map<String, String> attributes) {

		final Class<? extends Function> functionClass = functions.get(name);
		try {

			final Function function = functionClass.newInstance();
			for (Map.Entry<String, String> attribute : attributes.entrySet()) {
				final String methodName = attribute.getKey().toLowerCase();
				final Method method = functionMethodMaps.get(functionClass).get(methodName);
				method.invoke(function, attribute.getValue());
			}

			function.setKeyValueStoreAggregator(metamorph);

			if (collect instanceof DataProcessorImpl && data == null) {
				((DataProcessor) collect).addFunction(function);
			} else {
				data.addFunction(function);
			}

			if (LOG.isTraceEnabled()) {
				LOG.trace("\tfunction " + function.getClass().getSimpleName());
			}
		} catch (InstantiationException e) {
			throw new MetamorphDefinitionException("Funcion " + name + " could not be instantiated", e);
		} catch (IllegalAccessException e) {
			throw new MetamorphDefinitionException("Access conflict in function " + name, e);
		} catch (InvocationTargetException e) {
			throw new MetamorphDefinitionException("Method invocation failed on function " + name, e);
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
	 * @param defaultName
	 */
	private void registerDataSource(final String source, final String name, final String value, final String mode) {

		data = new Data();

		if (emitGroupName == null) {
			data.setDefaultName(name);
		} else {
			data.setDefaultName(emitGroupName);
		}

		if (emitGroupValue == null) {
			data.setDefaultValue(value);
		} else {
			data.setDefaultValue(emitGroupValue);
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
	public void endPrefixMapping(final String prefix) throws SAXException {/*
																			 * do
																			 * nothing
																			 */
	}

	@Override
	public void startDocument() throws SAXException {/* do nothing */
	}

	@Override
	public void endDocument() throws SAXException {/* do nothing */
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException {/*
																								 * do
																								 * nothing
																								 */
	}

	@Override
	public void setDocumentLocator(final Locator locator) {/* do nothing */
	}

	@Override
	public void skippedEntity(final String name) throws SAXException {/*
																	 * do
																	 * nothing
																	 */
	}

	@Override
	public void characters(final char[] cha, final int start, final int length) throws SAXException {/*
																									 * do
																									 * nothing
																									 */
	}

	@Override
	public void ignorableWhitespace(final char[] cha, final int start, final int length) throws SAXException {/*
																											 * do
																											 * nothing
																											 */
	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException {/*
																									 * do
																									 * nothing
																									 */
	}
}