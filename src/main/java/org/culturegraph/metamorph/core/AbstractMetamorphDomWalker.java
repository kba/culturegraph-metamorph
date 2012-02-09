package org.culturegraph.metamorph.core;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.exceptions.MetamorphDefinitionException;
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
public abstract class AbstractMetamorphDomWalker {

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

	private static final String SCHEMA_FILE = "schema/metamorph.xsd";
	private static final int LOWEST_COMPATIBLE_VERSION = 1;
	private static final int CURRENT_VERSION = 1;

	
	public final void walk(final String morphDef) {
		try {
			walk(ResourceUtil.getStream(morphDef));
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException(e);
		}
	}

	public final void walk (final InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("'inputStream' must not be null");
		}
		walk(DomLoader.parse(SCHEMA_FILE, new InputSource(inputStream)));
	}

	public final void walk(final Reader reader) {
		if (reader == null) {
			throw new IllegalArgumentException("'reader' must not be null");
		}
		walk(DomLoader.parse(SCHEMA_FILE, new InputSource(reader)));
	}

	private static MMTAG tagOf(final Node child) {
		return MMTAG.valueOf(child.getLocalName().toUpperCase());
	}

	protected static String getAttr(final Node node, final ATTRITBUTE attr) {
		final Node attribute = node.getAttributes().getNamedItem(attr.getString());
		if (attribute != null) {
			return attribute.getNodeValue();
		}
		return null;
	}

	protected static Map<String, String> attributesToMap(final Node node) {
		final Map<String, String> attributes = new HashMap<String, String>();
		final NamedNodeMap attrNode = node.getAttributes();

		for (int i = 0; i < attrNode.getLength(); ++i) {
			final Node itemNode = attrNode.item(i);
			attributes.put(itemNode.getLocalName(), itemNode.getNodeValue());
		}
		return attributes;
	}

	protected final void walk(final Document doc) {

		init();

		final Element root = doc.getDocumentElement();

		final int version = Integer.parseInt(getAttr(root, ATTRITBUTE.VERSION));
		checkVersionCompatibility(version);

		setEntityMarker(getAttr(root, ATTRITBUTE.ENTITY_MARKER));

		for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {

			switch (tagOf(node)) {
			case META:
				handleMeta(node);
				break;
			case FUNCTIONS:
				handleFunctionDefinitions(node);
				break;
			case RULES:
				handleRules(node);

				break;
			case MAPS:
				handleMaps(node);

				break;
			default:
				illegalChild(node);
			}
		}
		finish();
	}

	protected abstract void init();
	protected abstract void finish();
	
	protected abstract void setEntityMarker(final String entityMarker);

	protected abstract void handleMap(final Node mapNode);

	protected abstract void handleMetaEntry(final String name, final String value);

	protected abstract void handleFunctionDefinition(final Node functionDefNode);
	
	protected abstract  void handleRule(Node ruleNode);

	private void handleMaps(final Node node) {
		for (Node mapNode = node.getFirstChild(); mapNode != null; mapNode = mapNode.getNextSibling()) {
			handleMap(mapNode);
		}
	}

	private void handleRules(final Node node) {
		for (Node ruleNode = node.getFirstChild(); ruleNode != null; ruleNode = ruleNode.getNextSibling()) {
			handleRule(ruleNode);
		}
	}
	

	private void checkVersionCompatibility(final int version) {
		if (version < LOWEST_COMPATIBLE_VERSION || version > CURRENT_VERSION) {
			throw new MetamorphDefinitionException("Version " + version
					+ " of definition file not supported by metamorph version " + CURRENT_VERSION);
		}
	}

	protected final void illegalChild(final Node child) {
		throw new MetamorphDefinitionException("Schema mismatch: illegal tag " + child.getLocalName() + " in node "
				+ child.getParentNode().getLocalName());
	}

	private void handleFunctionDefinitions(final Node node) {
		for (Node functionDefNode = node.getFirstChild(); functionDefNode != null; functionDefNode = functionDefNode
				.getNextSibling()) {
			handleFunctionDefinition(functionDefNode);
		}
	}

	private void handleMeta(final Node node) {
		for (Node metaEntryNode = node.getFirstChild(); metaEntryNode != null; metaEntryNode = metaEntryNode
				.getNextSibling()) {
			handleMetaEntry(metaEntryNode.getLocalName(), metaEntryNode.getTextContent());
		}
	}
}
