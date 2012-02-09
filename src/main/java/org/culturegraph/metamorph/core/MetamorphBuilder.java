package org.culturegraph.metamorph.core;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.culturegraph.metamorph.core.collectors.Collect;
import org.culturegraph.metamorph.core.collectors.CollectFactory;
import org.culturegraph.metamorph.core.exceptions.MetamorphDefinitionException;
import org.culturegraph.metamorph.core.functions.Function;
import org.culturegraph.metamorph.core.functions.FunctionFactory;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.w3c.dom.Node;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder extends AbstractMetamorphDomWalker{


	private static final String DATA = "data";
	private static final String POSTPROCESS = "postprocess";
	private static final String FLUSH_WITH = "flushWith";

	// private final String morphDef;
	private FunctionFactory functions;
	private CollectFactory collects;
	private Metamorph metamorph;

	private MetamorphBuilder() {
		super();
		// nothing to do
	}

	public static Metamorph build(final String morphDef) {
		final MetamorphBuilder builder = new MetamorphBuilder();		
		builder.walk(morphDef);
		return builder.metamorph;

	}

	public static Metamorph build(final InputStream inputStream) {
		final MetamorphBuilder builder = new MetamorphBuilder();		
		builder.walk(inputStream);
		return builder.metamorph;
	}

	public static Metamorph build(final Reader reader) {
		final MetamorphBuilder builder = new MetamorphBuilder();		
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
	protected void handleMap(final Node mapNode) {
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

	private void handleRule(final Node node, final Collect parent) {

		final String nodeName = node.getLocalName();
		if (collects.getAvailableClasses().contains(nodeName)) {
			handleCollect(node, parent);

		} else if (DATA.equals(nodeName)) {
			handleData(node, parent);

		} else {
			illegalChild(node);
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

	@Override
	@SuppressWarnings("unchecked")
	// protected by 'if (Function.class.isAssignableFrom(clazz))'
	protected void handleFunctionDefinition(final Node functionDefNode) {
		final Class<?> clazz;
		final String className = getAttr(functionDefNode, ATTRITBUTE.CLASS);
		try {
			clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new MetamorphDefinitionException("Class '" + className + "' not found.", e);
		}
		if (Function.class.isAssignableFrom(clazz)) {
			functions.registerClass(getAttr(functionDefNode, ATTRITBUTE.NAME), (Class<Function>) clazz);
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
		functions = new FunctionFactory();
		collects = new CollectFactory();
		metamorph = new Metamorph();
	}

	@Override
	protected void handleRule(final Node ruleNode) {
		handleRule(ruleNode, null);
	}

	@Override
	protected void finish() {
		// nothing to do
		
	}
}
