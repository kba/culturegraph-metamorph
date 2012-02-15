package org.culturegraph.metamorph.core;

import java.io.InputStream;
import java.io.Reader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.culturegraph.metamorph.core.collectors.Collect;
import org.culturegraph.metamorph.core.exceptions.MetamorphDefinitionException;
import org.culturegraph.metamorph.core.functions.Function;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 * Builds a {@link Metamorph} from an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilder extends AbstractMetamorphDomWalker{

	private static final Logger LOG = LoggerFactory.getLogger(MetamorphBuilder.class);
	
	// private final String morphDef;
	private Metamorph metamorph;
	private Deque<Collect> collectStack;
	private Data data;

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
		collectStack = new LinkedList<Collect>();
		metamorph = new Metamorph();
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
		data.setValue(getAttr(dataNode, ATTRITBUTE.VALUE));
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
		
		final String flushWith = getAttr(node, ATTRITBUTE.FLUSH_WITH);
		if (null != flushWith) {
			collect.setFlushWith(flushWith);
		}
	}

	@Override
	protected void handleFunction(final Node functionNode) {
			final Function function = getFunctionFactory().newInstance(functionNode.getLocalName(), attributesToMap(functionNode));
			function.setMultiMap(metamorph);
			function.setEntityEndIndicator(metamorph);

			// add key value entries...
			for (Node mapEntryNode = functionNode.getFirstChild(); mapEntryNode != null; mapEntryNode = mapEntryNode
					.getNextSibling()) {
				final String entryName = getAttr(mapEntryNode, ATTRITBUTE.NAME);
				final String entryValue = getAttr(mapEntryNode, ATTRITBUTE.VALUE);
				function.putValue(entryName, entryValue);
			}
			if(data==null){
				collectStack.peek().appendPipe(function);
			}else{
				data.appendPipe(function);
			}
	}
}
