package org.culturegraph.metamorph.core;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.collectors.CollectFactory;
import org.w3c.dom.Node;

/**
 * Visualized a Metamorph definition in dot format
 * @author Markus Michael Geipel
 *
 */
public final class MetamorphVisualizer extends AbstractMetamorphDomWalker {

	private final Map<String, String> meta = new HashMap<String, String>();
	private CollectFactory collects;
	private final PrintWriter writer;
	private int count;
	
	public MetamorphVisualizer(final Writer writer) {
		super();
		this.writer = new PrintWriter(writer);
	}
	
	@Override
	protected void init() {
		collects = new CollectFactory();
		
		writer.println("digraph g {\n" + 
				"graph [ rankdir = \"LR\" ];\n" +
				"node [ fontsize = \"16\" shape = \"ellipse\" ];\n" +
				"edge [ ];\n");
	}
	
	@Override
	protected void finish() {
		writer.println("}");
		writer.flush();
	}

	@Override
	protected void setEntityMarker(final String entityMarker) {
		// nothing to do
	}

	@Override
	protected void handleMap(final Node mapNode) {
		// nothing to do

	}

	@Override
	protected void handleMetaEntry(final String name, final String value) {
		meta.put(name, value);
	}

	@Override
	protected void handleFunctionDefinition(final Node functionDefNode) {
		// nothing to do
	}

	@Override
	protected void handleRule(final Node ruleNode) {
		writer.println("\"" + getNewId() + "\" [label = \""+ ruleNode.getLocalName()+"\" shape = \"record\"];");
	}

	private int getNewId() {
		return ++count;
	}
}
