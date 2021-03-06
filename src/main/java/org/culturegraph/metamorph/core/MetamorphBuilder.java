package org.culturegraph.metamorph.core;

import java.io.InputStream;
import java.io.Reader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.culturegraph.metamorph.core.collectors.Collect;
import org.culturegraph.metamorph.core.functions.Function;
import org.culturegraph.util.SimpleMultiMap;
import org.w3c.dom.Node;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder extends AbstractMetamorphDomWalker {

	private static final String NOT_FOUND = " not found.";
	// private final String morphDef;
	private final Metamorph metamorph;
	private final Deque<Collect> collectStack;
	private Data data;

	protected MetamorphBuilder(final Metamorph metamorph) {
		super();
		this.collectStack = new LinkedList<Collect>();
		this.metamorph = metamorph;
	}

	protected void buildIntern(final String morphDef) {
		walk(morphDef);
	}

	protected void buildIntern(final InputStream inputStream) {
		walk(inputStream);
	}

	public static Metamorph build(final String morphDef) {
		final MetamorphBuilder builder = new MetamorphBuilder(new Metamorph());
		builder.walk(morphDef);
		return builder.metamorph;

	}

	public static Metamorph build(final InputStream inputStream) {
		final MetamorphBuilder builder = new MetamorphBuilder(new Metamorph());
		builder.walk(inputStream);
		return builder.metamorph;
	}

	public static Metamorph build(final Reader reader) {
		final MetamorphBuilder builder = new MetamorphBuilder(new Metamorph());
		builder.walk(reader);
		return builder.metamorph;
	}

	@Override
	protected void setEntityMarker(final String entityMarker) {
		if (null != entityMarker && !entityMarker.isEmpty()) {
			metamorph.setEntityMarker(entityMarker.charAt(0));
		}
	}

	@Override
	protected void handleInternalMap(final Node mapNode) {
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

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMapClass(final Node mapNode) {
		final Map<String, String> attributes = attributesToMap(mapNode);

		final String mapName = attributes.remove(ATTRITBUTE.NAME.getString());
		if (!getMapFactory().containsKey(mapNode.getLocalName())) {
			throw new IllegalArgumentException("Map " + mapNode.getLocalName() + NOT_FOUND);
		}
		final Map<String,String> map = getMapFactory().newInstance(mapNode.getLocalName(), attributes);
		metamorph.putMap(mapName, map);
	}

	@Override
	@SuppressWarnings("unchecked")
	// protected by 'if (Function.class.isAssignableFrom(clazz))'
	protected void handleFunctionDefinition(final Node functionDefNode) {
		final Class<?> clazz;
		final String className = getAttr(functionDefNode, ATTRITBUTE.CLASS);
		try {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new MetamorphDefinitionException("Function " + className + NOT_FOUND, e);
		}
		if (Function.class.isAssignableFrom(clazz)) {
			getFunctionFactory().registerClass(getAttr(functionDefNode, ATTRITBUTE.NAME), (Class<Function>) clazz);
		} else {
			throw new MetamorphDefinitionException(className + " does not implement interface 'Function'");
		}
	}

	@Override
	protected void handleMetaEntry(final String name, final String value) {
		metamorph.putValue(Metamorph.METADATA, name, value);
	}

	@Override
	protected void init() {

		// metamorph = new Metamorph();
	}

	@Override
	protected void finish() {
		// nothing to do
	}

	@Override
	protected void enterData(final Node dataNode) {
		final String source = getAttr(dataNode, ATTRITBUTE.SOURCE);
		data = new Data(source);
		data.setName(getAttr(dataNode, ATTRITBUTE.NAME));
		// data.setValue(getAttr(dataNode, ATTRITBUTE.VALUE));
		metamorph.registerData(data);
	}

	@Override
	protected void exitData(final Node node) {
		if (collectStack.isEmpty()) {
			data.endPipe(metamorph);
		} else {
			final Collect parent = collectStack.peek();
			data.endPipe(parent);
			parent.addNamedValueSource(data);
		}
		data = null;
	}

	@Override
	protected void enterCollect(final Node node) {
		final Map<String, String> attributes = attributesToMap(node);
		// must be set after recursive calls to flush decendents before parent
		attributes.remove(ATTRITBUTE.FLUSH_WITH.getString());

		if (!getCollectFactory().containsKey(node.getLocalName())) {
			throw new IllegalArgumentException("Collector " + node.getLocalName() + NOT_FOUND);
		}
		final Collect collect = getCollectFactory().newInstance(node.getLocalName(), attributes, metamorph);

		collectStack.push(collect);
	}

	@Override
	protected void exitCollect(final Node node) {
		final Collect collect = collectStack.pop();
		if (collectStack.isEmpty()) {
			collect.endPipe(metamorph);
		} else {
			final Collect parent = collectStack.peek();
			parent.addNamedValueSource(collect);
			collect.endPipe(parent);
		}
		// must be set after recursive calls to flush decendents before parent
		final String flushWith = getAttr(node, ATTRITBUTE.FLUSH_WITH);
		if (null != flushWith) {
			collect.setFlushWith(flushWith);
		}
	}

	@Override
	protected void handleFunction(final Node functionNode) {
		if (!getFunctionFactory().containsKey(functionNode.getLocalName())) {
			throw new IllegalArgumentException(functionNode.getLocalName() + NOT_FOUND);
		}
		final Function function = getFunctionFactory().newInstance(functionNode.getLocalName(),
				attributesToMap(functionNode));

		function.setMultiMap(metamorph);
		function.setEntityEndIndicator(metamorph);

		// add key value entries...
		for (Node mapEntryNode = functionNode.getFirstChild(); mapEntryNode != null; mapEntryNode = mapEntryNode
				.getNextSibling()) {
			final String entryName = getAttr(mapEntryNode, ATTRITBUTE.NAME);
			final String entryValue = getAttr(mapEntryNode, ATTRITBUTE.VALUE);
			function.putValue(entryName, entryValue);
		}
		if (data == null) {
			collectStack.peek().appendPipe(function);
		} else {
			data.appendPipe(function);
		}
	}

}
