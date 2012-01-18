package org.culturegraph.metamorph.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.culturegraph.metamorph.core.Data.Mode;
import org.culturegraph.metamorph.functions.Function;
import org.culturegraph.metamorph.functions.FunctionFactory;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.culturegraph.metamorph.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder2 {

	public static enum MMTAG {
		META, FUNCTIONS, RULES, MAPS, COMBINE, CHOOSE, ENTITY, SPLIT, EXTRACT, AGGREGATE, COUNT, MAP, ENTRY, DEF, GROUP, DATA, POSTPROCESS
	}

	public static enum ATTRITBUTE {
		VERSION("version"), SOURCE("source"), OCCURENCE("occurence"), MODE("mode"), VALUE("value"), NAME("name"), CLASS(
				"class"), DEFAULT("default"), RESET("reset"), SAME_ENTITY("sameEntity"), FLUSH_WITH("flushWith");
		private final String string;

		private ATTRITBUTE(final String string) {
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(MetamorphBuilder2.class);
	private static final String SCHEMA_FILE = "metamorph2.xsd";
	private static final ErrorHandler ERROR_HANDLER = new MetamorphDefinitionParserErrorHandler();
	private static final int LOWEST_COMPATIBLE_VERSION = 1;
	private static final int CURRENT_VERSION = 1;
	private static final String TRUE = "true";

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
			if (schemaUrl == null) {
				throw new MetamorphDefinitionException(SCHEMA_FILE + " not found!");
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

	private static MMTAG tagOf(final Node child) {
		return MMTAG.valueOf(child.getNodeName().toUpperCase());
	}

	private static String getAttr(final Node node, final ATTRITBUTE attr) {
		final Node attribute = node.getAttributes().getNamedItem(attr.getString());

		if (attribute != null) {
			return attribute.getNodeValue();
		}

		return null;
	}

	private static Map<String, String> attributesToMap(final Node node) {
		final Map<String, String> attributes = new HashMap<String, String>();
		final NamedNodeMap attrNode = node.getAttributes();

		for (int i = 0; i < attrNode.getLength(); ++i) {
			final Node itemNode = attrNode.item(i);
			attributes.put(itemNode.getNodeName(), itemNode.getNodeValue());
		}
		return attributes;
	}

	private static Metamorph build(final Document doc) {
		final Element root = doc.getDocumentElement();
		final int version = Integer.parseInt(root.getAttribute(ATTRITBUTE.VERSION.string));
		if (version < LOWEST_COMPATIBLE_VERSION || version > CURRENT_VERSION) {
			throw new MetamorphDefinitionException("Version " + version
					+ " of definition file not supported by metamorph version " + CURRENT_VERSION);
		}

		final Metamorph metamorph = new Metamorph();
		final FunctionFactory functions = new FunctionFactory();

		for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {

			switch (tagOf(child)) {
			case META:
				handleMeta(child, metamorph);
				break;
			case FUNCTIONS:
				handleFunctions(child, metamorph, functions);
				break;
			case RULES:
				handleRules(child, metamorph, functions);
				break;
			case MAPS:
				handleMaps(child, metamorph);
				break;
			default:
				illegalChild(child, root);
			}
		}

		return metamorph;
	}

	private static void illegalChild(final Node child, final Node root) {
		throw new MetamorphDefinitionException("Schema mismatch: illegal tag " + child.getNodeName() + " in node "
				+ root.getNodeName());
	}

	private static void handleMaps(final Node node, final Metamorph metamorph) {
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			handleMap(child, metamorph);
		}
	}

	private static void handleMap(final Node node, final Metamorph metamorph) {
		final String mapName = getAttr(node, ATTRITBUTE.NAME);
		final String mapDefault = getAttr(node, ATTRITBUTE.DEFAULT);
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			final String entryName = getAttr(child, ATTRITBUTE.NAME);
			final String entryValue = getAttr(child, ATTRITBUTE.VALUE);
			metamorph.putValue(mapName, entryName, entryValue);
		}
		if (mapDefault != null) {
			metamorph.putValue(mapName, SimpleMultiMap.DEFAULT_MAP_KEY, mapDefault);
		}
	}

	private static void handleRules(final Node node, final Metamorph metamorph, final FunctionFactory functions) {
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			switch (tagOf(child)) {
			case GROUP:
				handleGroup(child, metamorph, functions);
				break;
			case DATA:
				handleData((Element) child, metamorph, functions);
				break;
			case ENTITY:
			case COMBINE:
			case CHOOSE:
			case AGGREGATE:
				handleCollect(child, metamorph, functions);
				break;
			default:
				illegalChild(child, node);
			}
		}
	}

	private static Collect handleCollect(final Node node, final Metamorph metamorph, final FunctionFactory functions) {

		final boolean reset = TRUE.equals(getAttr(node, ATTRITBUTE.RESET));
		final boolean sameEntity = TRUE.equals(getAttr(node, ATTRITBUTE.SAME_ENTITY));

		final Collect collect;
		switch (tagOf(node)) {

		case ENTITY:
			collect = new CollectEntity(metamorph);
			break;
		case COMBINE:
			collect = new CollectLiteral(metamorph);
			break;
		case CHOOSE:
			collect = new ChooseLiteral(metamorph);
			break;
		case AGGREGATE:
			collect = null;
			throw new IllegalArgumentException("Aggregate not implemented yet");

		
		default:
			illegalChild(node, node.getParentNode());
			return null;
		}
		collect.setName(getAttr(node, ATTRITBUTE.NAME));
		collect.setValue(getAttr(node, ATTRITBUTE.VALUE));
		collect.setReset(reset);
		collect.setSameEntity(sameEntity);

		final String flushWith = getAttr(node, ATTRITBUTE.FLUSH_WITH);
		if (flushWith != null) {
			metamorph.addEntityEndListener(collect, flushWith);
		}

		handleLiteralRule(node, collect, metamorph, functions);

		return collect;
	}

	private static void handleLiteralRule(final Node node, final NamedValueAggregator parent, final Metamorph metamorph,
			final FunctionFactory functions) {
		switch (tagOf(node)) {
		case GROUP:
			parent.addNamedValueSource(handleGroup(node, metamorph, functions));
			break;
		case DATA:
			final Data data = handleData((Element) node, metamorph, functions);
			parent.addNamedValueSource(data);
			break;
		case COMBINE:
		case CHOOSE:
		case AGGREGATE:
			final Collect innerCollect = handleCollect(node, metamorph, functions);
			if (innerCollect instanceof NamedValueSource) {
				final NamedValueSource namedValueSource = (NamedValueSource) innerCollect;
				parent.addNamedValueSource(namedValueSource);
			}
			break;
		default:
			illegalChild(node, node.getParentNode());
			break;
		}
	}

	private static Data handleData(final Element dataNode, final Metamorph metamorph, final FunctionFactory functions) {

		final String source = getAttr(dataNode, ATTRITBUTE.SOURCE);
		final Data data = new Data(source);

		final String occurence = getAttr(dataNode, ATTRITBUTE.OCCURENCE);
		if (occurence != null && !occurence.isEmpty()) {
			data.setOccurence(Integer.parseInt(occurence));
		}

		data.setName(getAttr(dataNode, ATTRITBUTE.NAME));
		data.setValue(getAttr(dataNode, ATTRITBUTE.VALUE));

		final String mode = getAttr(dataNode, ATTRITBUTE.MODE);
		if (mode != null) {
			final Data.Mode dataMode = Data.Mode.valueOf(mode.toUpperCase(Locale.US));
			data.setMode(dataMode);
			if (dataMode == Mode.COUNT) {
				metamorph.addEntityEndListener(data, Metamorph.RECORD_KEYWORD);
			}
		}
		for (Node childNode = dataNode.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			addFunctionToProcessor(functions, metamorph, childNode, data);
		}

		metamorph.registerData(data);
		return data;
	}

	private static void addFunctionToProcessor(final FunctionFactory functions, final SimpleMultiMap multiMap,
			final Node node, final ValueProcessor processor) {
		final Function function = functions.newFunction(node.getNodeName(), attributesToMap(node));
		function.setMultiMap(multiMap);

		// add key value entries...
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			final String entryName = getAttr(child, ATTRITBUTE.NAME);
			final String entryValue = getAttr(child, ATTRITBUTE.VALUE);
			function.putValue(entryName, entryValue);
		}
		processor.addFunction(function);
	}

	private static Group handleGroup(final Node node, final Metamorph metamorph, final FunctionFactory functions) {
		final Group group = new Group();
		group.setName(getAttr(node, ATTRITBUTE.NAME));
		group.setValue(getAttr(node, ATTRITBUTE.VALUE));

		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (tagOf(node).equals(MMTAG.POSTPROCESS)) {
				handlePostprocess(child, group, metamorph, functions);
			}
			handleLiteralRule(child, group, metamorph, functions);
		}
		return group;
	}

	private static void handlePostprocess(final Node node, final ValueProcessor processor, final Metamorph metamorph,
			final FunctionFactory functions) {
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			addFunctionToProcessor(functions, metamorph, child, processor);
		}

	}

	private static void handleFunctions(final Node node, final Metamorph metamorph, final FunctionFactory functions) {
		for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			final Class<?> clazz;
			final String className = getAttr(childNode, ATTRITBUTE.CLASS);
			try {
				clazz = ReflectionUtil.getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new MetamorphDefinitionException("Class '" + className + "' not found.", e);
			}
			if (Function.class.isAssignableFrom(clazz)) {
				LOG.info("def " + getAttr(childNode, ATTRITBUTE.NAME) + "=" + clazz.getName());
				functions.registerFunction(getAttr(childNode, ATTRITBUTE.NAME), (Class<? extends Function>) clazz);
			} else {
				throw new MetamorphDefinitionException(className + " does not implement interface 'Function'");
			}
		}
	}

	private static void handleMeta(final Node node, final Metamorph metamorph) {
		for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			metamorph.putValue(Metamorph.METADATA, childNode.getNodeName(), childNode.getTextContent());
		}
	}

	public static void main(final String[] args) {
		final Metamorph metamorph = MetamorphBuilder2.build("pnd2.pica");
		System.out.println(metamorph);
	}
}
