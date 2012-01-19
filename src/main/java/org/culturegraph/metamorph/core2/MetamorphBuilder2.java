package org.culturegraph.metamorph.core2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.culturegraph.metamorph.core2.collectors.AbstractCollect;
import org.culturegraph.metamorph.core2.collectors.CollectFactory;
import org.culturegraph.metamorph.core2.collectors.NamedValueAggregator;
import org.culturegraph.metamorph.core2.exceptions.MetamorphDefinitionException;
import org.culturegraph.metamorph.core2.functions.Function;
import org.culturegraph.metamorph.core2.functions.FunctionFactory;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.culturegraph.metamorph.stream.readers.PicaReader;
import org.culturegraph.metamorph.stream.readers.Reader;
import org.culturegraph.metamorph.stream.receivers.DefaultWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder2 {

	public static enum MMTAG {
		META, FUNCTIONS, RULES, MAPS, ENTITY, MAP, ENTRY,
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
	private static final String DATA = "data";
	private static final String POSTPROCESS = "postprocess";

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
		try {
			return build(getDocumentBuilder().parse(inputStream));
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
		final int version = Integer.parseInt(root.getAttribute(ATTRITBUTE.VERSION.getString()));
		if (version < LOWEST_COMPATIBLE_VERSION || version > CURRENT_VERSION) {
			throw new MetamorphDefinitionException("Version " + version
					+ " of definition file not supported by metamorph version " + CURRENT_VERSION);
		}

		final Metamorph metamorph = new Metamorph();
		final FunctionFactory functions = new FunctionFactory();
		final CollectFactory collects = new CollectFactory();

		for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {

			switch (tagOf(child)) {
			case META:
				handleMeta(child, metamorph);
				break;
			case FUNCTIONS:
				handleFunctionDefinitions(child, functions);
				break;
			case RULES:
				for (Node ruleNode = child.getFirstChild(); ruleNode != null; ruleNode = ruleNode.getNextSibling()) {
					handleRule(ruleNode, null, metamorph, functions, collects);
				}
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
		for (Node mapNode = node.getFirstChild(); mapNode != null; mapNode = mapNode.getNextSibling()) {
			final String mapName = getAttr(node, ATTRITBUTE.NAME);
			final String mapDefault = getAttr(node, ATTRITBUTE.DEFAULT);
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

	private static void handleRule(final Node node, final AbstractCollect parent, final Metamorph metamorph,
			final FunctionFactory functions, final CollectFactory collects) {

		final String nodeName = node.getNodeName();
		LOG.info("adding " + nodeName + " under " + node.getParentNode().getNodeName());
		if (collects.getAvailableClasses().contains(nodeName)) {
			final NamedValuePipe innerCollect = handleCollect(node, metamorph, functions, collects);

			if (parent != null) {
				parent.addNamedValueSource(innerCollect);
			}
			
		} else if (DATA.equals(nodeName)) {
			final NamedValueSource data = handleData(node, metamorph, functions);
			if (parent == null) {
				data.setNamedValueReceiver(metamorph);
			} else {
				data.setNamedValueReceiver(parent);
				parent.addNamedValueSource(data);
			}
		} else {
			illegalChild(node, node.getParentNode());
		}
	}

	private static NamedValuePipe handleCollect(final Node node, final Metamorph metamorph, final FunctionFactory functions,
			final CollectFactory collects) {

		final AbstractCollect collect = collects.newInstance(node.getNodeName(), attributesToMap(node), metamorph);

		NamedValuePipe lastSource = collect;
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (POSTPROCESS.equals(child.getNodeName())) {
				lastSource = handlePostprocess(child, collect, metamorph, functions);
			} else {
				handleRule(child, collect, metamorph, functions, collects);
			}
		}
		return lastSource;
	}

	private static NamedValueSource handleData(final Node dataNode, final Metamorph metamorph, final FunctionFactory functions) {

		final String source = getAttr(dataNode, ATTRITBUTE.SOURCE);
		final Data data = new Data(source);

		data.setName(getAttr(dataNode, ATTRITBUTE.NAME));
		data.setValue(getAttr(dataNode, ATTRITBUTE.VALUE));

		metamorph.registerData(data);
		
		return handlePostprocess(dataNode, data, metamorph, functions);
	}

	private static NamedValuePipe handlePostprocess(final Node postprocessNode, final NamedValuePipe processor,
			final Metamorph metamorph, final FunctionFactory functions) {

		NamedValuePipe lastSource = processor;
		for (Node functionNode = postprocessNode.getFirstChild(); functionNode != null; functionNode = functionNode
				.getNextSibling()) {
			final Function function = functions.newInstance(functionNode.getNodeName(), attributesToMap(functionNode));
			function.setMultiMap(metamorph);

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

	private static void handleFunctionDefinitions(final Node node, final FunctionFactory functions) {
		for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			final Class<?> clazz;
			final String className = getAttr(childNode, ATTRITBUTE.CLASS);
			try {
				clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new MetamorphDefinitionException("Class '" + className + "' not found.", e);
			}
			if (Function.class.isAssignableFrom(clazz)) {
				LOG.info("def " + getAttr(childNode, ATTRITBUTE.NAME) + "=" + clazz.getName());
				functions.registerClass(getAttr(childNode, ATTRITBUTE.NAME), (Class<Function>) clazz);
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

	public static void main(final String[] args) throws IOException {
		final Reader reader = new PicaReader();
		final Metamorph metamorph = reader.setReceiver(MetamorphBuilder2.build("pnd2.pica"));
		LOG.info(metamorph.toString());
		metamorph.setReceiver(new DefaultWriter(new BufferedWriter(new OutputStreamWriter(System.out, "UTF8"))));
		reader.read(new FileReader("src/test/resources/PND_10entries.pica"));
	}
}
