package org.culturegraph.metamorph.core;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.collectors.Collect;
import org.culturegraph.metamorph.core.collectors.CollectFactory;
import org.culturegraph.metamorph.core.exceptions.MetamorphDefinitionException;
import org.culturegraph.metamorph.core.functions.Function;
import org.culturegraph.metamorph.core.functions.FunctionFactory;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.culturegraph.metamorph.util.ResourceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder {

	/**
	 * XML tags
	 */
	public static enum MMTAG {
		META, FUNCTIONS, RULES, MAPS, ENTITY, MAP, ENTRY, TEXT
	}

	/**
	 * XML attributes
	 */
	public static enum ATTRITBUTE {
		VERSION("version"), SOURCE("source"), OCCURENCE("occurence"), MODE("mode"), VALUE("value"), NAME("name"), CLASS(
				"class"), DEFAULT("default"), ENTITY_MARKER("entityMarker");
		private final String string;

		private ATTRITBUTE(final String string) {
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}

	// private static final Logger LOG =
	// LoggerFactory.getLogger(MetamorphBuilder.class);
	private static final String SCHEMA_FILE = "schema/metamorph.xsd";
	private static final int LOWEST_COMPATIBLE_VERSION = 1;
	private static final int CURRENT_VERSION = 1;
	private static final String DATA = "data";
	private static final String POSTPROCESS = "postprocess";
	private static final String FLUSH_WITH = "flushWith";

	// private final String morphDef;
	private FunctionFactory functions;
	private CollectFactory collects;
	private Metamorph metamorph;

	// public MetamorphBuilder(final String morphDef) {
	// this.morphDef = morphDef;
	// }

	private MetamorphBuilder() {
		// nothing to do
	}

	// public Metamorph build() {
	// return build(morphDef);
	// }

	public static Metamorph build(final String morphDef) {
		try {
			return build(ResourceUtil.getStream(morphDef));
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(e);
		}
	}

	public static Metamorph build(final InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("'inputStream' must not be null");
		}
		return new MetamorphBuilder().build(DomLoader.parse(SCHEMA_FILE, new InputSource(inputStream)));
	}

	public static Metamorph build(final Reader reader) {
		if (reader == null) {
			throw new IllegalArgumentException("'reader' must not be null");
		}
		return new MetamorphBuilder().build(DomLoader.parse(SCHEMA_FILE, new InputSource(reader)));
	}

	private MMTAG tagOf(final Node child) {
		if (child.getNodeType() == Node.TEXT_NODE) {
			wrongXMLLib();
		}
		return MMTAG.valueOf(child.getLocalName().toUpperCase());
	}

	private String getAttr(final Node node, final ATTRITBUTE attr) {
		final Node attribute = node.getAttributes().getNamedItem(attr.getString());

		if (attribute != null) {
			return attribute.getNodeValue();
		}

		return null;
	}

	private Map<String, String> attributesToMap(final Node node) {
		final Map<String, String> attributes = new HashMap<String, String>();
		final NamedNodeMap attrNode = node.getAttributes();

		for (int i = 0; i < attrNode.getLength(); ++i) {
			final Node itemNode = attrNode.item(i);
			attributes.put(itemNode.getLocalName(), itemNode.getNodeValue());
		}
		return attributes;
	}

	private Metamorph build(final Document doc) {
		final Element root = doc.getDocumentElement();

		final int version = Integer.parseInt(getAttr(root, ATTRITBUTE.VERSION));
		checkVersionCompatibility(version);

		functions = new FunctionFactory();
		collects = new CollectFactory();
		metamorph = new Metamorph();

		setEntityMarker(getAttr(root, ATTRITBUTE.ENTITY_MARKER));

		for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {

			switch (tagOf(child)) {
			case META:
				handleMeta(child);
				break;
			case FUNCTIONS:
				handleFunctionDefinitions(child);
				break;
			case RULES:
				for (Node ruleNode = child.getFirstChild(); ruleNode != null; ruleNode = ruleNode.getNextSibling()) {
					handleRule(ruleNode, null);
				}
				break;
			case MAPS:
				handleMaps(child);
				break;
			default:
				illegalChild(child, root);
			}
		}

		return metamorph;
	}

	private void setEntityMarker(final String entityMarker) {
		if (null != entityMarker && !entityMarker.isEmpty()) {
			metamorph.setEntityMarker(entityMarker.charAt(0));
		}
	}

	private void checkVersionCompatibility(final int version) {
		if (version < LOWEST_COMPATIBLE_VERSION || version > CURRENT_VERSION) {
			throw new MetamorphDefinitionException("Version " + version
					+ " of definition file not supported by metamorph version " + CURRENT_VERSION);
		}
	}

	private void illegalChild(final Node child, final Node root) {
		if (child.getNodeType() == Node.TEXT_NODE) {
			wrongXMLLib();
		}
		throw new MetamorphDefinitionException("Schema mismatch: illegal tag " + child.getLocalName() + " in node "
				+ root.getLocalName());
	}

	private void wrongXMLLib() {
		throw new IllegalStateException("Your XML library is handling 'setIgnoringElementContentWhitespace(true)' correctly. " +
				"DocumentBuilderFactory implementation: " + DomLoader.getDocumentBuilderFactoryImplName());
	}

	private void handleMaps(final Node node) {
		for (Node mapNode = node.getFirstChild(); mapNode != null; mapNode = mapNode.getNextSibling()) {
			final String mapName = getAttr(mapNode, ATTRITBUTE.NAME);
			final String mapDefault = getAttr(mapNode, ATTRITBUTE.DEFAULT);
			for (Node entryNode = mapNode.getFirstChild(); entryNode != null; entryNode = entryNode.getNextSibling()) {
				final String entryName = getAttr(entryNode, ATTRITBUTE.NAME);
				final String entryValue = getAttr(entryNode, ATTRITBUTE.VALUE);
				metamorph.putValue(mapName, entryName, entryValue);
			}
			if (mapDefault != null) {
				metamorph.putValue(mapName, SimpleMultiMap.DEFAULT_MAP_KEY, mapDefault);
			}
		}
	}

	private void handleRule(final Node node, final Collect parent) {

		final String nodeName = node.getLocalName();
		if (collects.getAvailableClasses().contains(nodeName)) {
			handleCollect(node, parent);

		} else if (DATA.equals(nodeName)) {
			handleData(node, parent);

		} else {
			illegalChild(node, node.getParentNode());
		}
	}

	private void handleCollect(final Node node, final Collect parent) {

		final Map<String, String> attributes = attributesToMap(node);

		// must be set after recursive calls to flush decendents before parent
		final String flushWith = attributes.remove(FLUSH_WITH);
		
		final Collect collect = collects.newInstance(node.getLocalName(), attributes, metamorph);

		NamedValuePipe lastFunction = collect;
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (POSTPROCESS.equals(child.getLocalName())) {
				lastFunction = handlePostprocess(child, collect);
			} else {
				handleRule(child, collect);
			}
		}

		if (parent == null) {
			lastFunction.setNamedValueReceiver(metamorph);
		} else {
			parent.addNamedValueSource(collect);
			lastFunction.setNamedValueReceiver(parent);
		}

		if (null != flushWith) {
			collect.setFlushWith(flushWith);
		}
	}

	private void handleData(final Node dataNode, final Collect parent) {

		final String source = getAttr(dataNode, ATTRITBUTE.SOURCE);
		final Data data = new Data(source);

		data.setName(getAttr(dataNode, ATTRITBUTE.NAME));
		data.setValue(getAttr(dataNode, ATTRITBUTE.VALUE));

		metamorph.registerData(data);

		final NamedValuePipe lastFunction = handlePostprocess(dataNode, data);

		if (parent == null) {
			lastFunction.setNamedValueReceiver(metamorph);
		} else {
			lastFunction.setNamedValueReceiver(parent);
			parent.addNamedValueSource(data);
		}

	}

	private NamedValuePipe handlePostprocess(final Node postprocessNode, final NamedValuePipe processor) {

		NamedValuePipe lastSource = processor;
		for (Node functionNode = postprocessNode.getFirstChild(); functionNode != null; functionNode = functionNode
				.getNextSibling()) {
			final Function function = functions.newInstance(functionNode.getLocalName(), attributesToMap(functionNode));
			function.setMultiMap(metamorph);
			function.setEntityEndIndicator(metamorph);

			// add key value entries...
			for (Node mapEntryNode = functionNode.getFirstChild(); mapEntryNode != null; mapEntryNode = mapEntryNode
					.getNextSibling()) {
				final String entryName = getAttr(mapEntryNode, ATTRITBUTE.NAME);
				final String entryValue = getAttr(mapEntryNode, ATTRITBUTE.VALUE);
				function.putValue(entryName, entryValue);
			}
			lastSource = lastSource.setNamedValueReceiver(function);

		}
		return lastSource;
	}

	@SuppressWarnings("unchecked")
	// protected by 'if (Function.class.isAssignableFrom(clazz))'
	private void handleFunctionDefinitions(final Node node) {
		for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			final Class<?> clazz;
			final String className = getAttr(childNode, ATTRITBUTE.CLASS);
			try {
				clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new MetamorphDefinitionException("Class '" + className + "' not found.", e);
			}
			if (Function.class.isAssignableFrom(clazz)) {
				functions.registerClass(getAttr(childNode, ATTRITBUTE.NAME), (Class<Function>) clazz);
			} else {
				throw new MetamorphDefinitionException(className + " does not implement interface 'Function'");
			}
		}
	}

	private void handleMeta(final Node node) {
		for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			metamorph.putValue(Metamorph.METADATA, childNode.getLocalName(), childNode.getTextContent());
		}
	}
}
