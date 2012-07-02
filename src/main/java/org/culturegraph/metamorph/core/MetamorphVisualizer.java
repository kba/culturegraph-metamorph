package org.culturegraph.metamorph.core;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Node;

/**
 * Visualizes a Metamorph definition in dot format. <br><strong>Warning:</strong> This visualizer is a mere proof of concept. Code is messy and not covered by unit tests!
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class MetamorphVisualizer extends AbstractMetamorphDomWalker {

	private static final String RECURSION_INDICATOR = Character.toString(Metamorph.FEEDBACK_CHAR);
	private static final Set<String> ORDERED_COLLECTS = new HashSet<String>();
	private final Map<String, String> meta = new HashMap<String, String>();
	private final PrintWriter writer;
	private int count;
	private final Deque<String> idStack = new LinkedList<String>();

	// private final ListMap<String, String> sourceIdMap = new ListMap<String,
	// String>();
	private final Set<String> sources = new HashSet<String>();
	private final StringBuilder edgeBuffer = new StringBuilder();
	private final Deque<String> lastProcessorStack = new LinkedList<String>();
	private final Deque<Integer> childCountStack = new LinkedList<Integer>();

	static {
		Collections.addAll(ORDERED_COLLECTS, "choose", "entity");
	}

	public MetamorphVisualizer(final Writer writer) {
		super();
		this.writer = new PrintWriter(writer);
	}


	@Override
	protected void init() {
		childCountStack.push(Integer.valueOf(0));
		writer.println("digraph dataflow {\n" + "graph [ rankdir = \"LR\"];\n"
				+ "node [ fontsize = \"9\"  shape = \"plaintext\"  fontname=\"Helvetica\"];\n" + "edge [ fontsize = \"9\"   fontname=\"Helvetica\"];\n");
	}
	
	private static String buildRecord(final String identifier, final String name, final String color,
			final Map<String, String> attributes) {
		final StringBuilder builder = new StringBuilder();
		builder.append("\"" + identifier + "\" [label = <<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">");
		if (name != null) {
			builder.append("<TR><TD COLSPAN=\"2\" BGCOLOR=\"" + color + "\"><B>" + name + "</B></TD></TR>");
		}

		for (Entry<String, String> entry : attributes.entrySet()) {
			builder.append("<TR><TD>" + entry.getKey() + "</TD><TD>'" + StringEscapeUtils.escapeHtml(entry.getValue())
					+ "'</TD></TR>");
		}
		builder.append("</TABLE>>];");
		return builder.toString();
	}

	private static String buildMap(final String identifier, final String name, final Map<String, String> attributes) {
		final StringBuilder builder = new StringBuilder();
		builder.append("\"" + identifier
				+ "\" [color=\"grey\" label = <<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">");
		if (name != null) {
			builder.append("<TR><TD COLSPAN=\"2\"><B>" + name + "</B></TD></TR>");
		}

		for (Entry<String, String> entry : attributes.entrySet()) {
			builder.append("<TR><TD>" + entry.getKey() + "</TD><TD>'" + StringEscapeUtils.escapeHtml(entry.getValue())
					+ "'</TD></TR>");
		}
		builder.append("</TABLE>>];");
		return builder.toString();
	}

	private void addEdge(final String fromId, final String toId) {
		edgeBuffer.append("\"" + fromId + "\" -> \"" + toId + "\" \n");
	}

	private void addEdge(final String fromId, final String toId, final String label) {
		if (label == null) {
			addEdge(fromId, toId);
		} else {
			edgeBuffer.append("\"" + fromId + "\" -> \"" + toId + "\" [ label = \"" + label + "\" ] \n");
		}
	}

	private void addIncludeEdge(final String fromId, final String toId) {
		edgeBuffer.append("\"" + fromId + "\" -> \"" + toId + "\" [color = \"grey\" dir=\"none\"]\n");
	}


	@Override
	protected void finish() {

		for (String source : sources) {
			final String color;
			if (source.startsWith(RECURSION_INDICATOR)) {
				color = "lemonchiffon";
			} else {
				color = "skyblue";
			}

			writer.println("\"" + source + "\" [label=\"" + source + "\" shape=\"ellipse\"  fillcolor=\"" + color
					+ "\" style=\"filled\"];");
		}

		writer.append(edgeBuffer.toString());
		writer.println("}");
		writer.flush();
	}

	@Override
	protected void setEntityMarker(final String entityMarker) {
		// nothing to do
	}

	@Override
	protected void handleMap(final Node mapNode) {
		final String mapName = getAttr(mapNode, ATTRITBUTE.NAME);
		final Map<String, String> map = getMap(mapNode);
		writer.println(buildMap(mapName, mapName, map));
	}

	private Map<String, String> getMap(final Node mapNode) {
		final Map<String, String> map = new HashMap<String, String>();
		final String mapDefault = getAttr(mapNode, ATTRITBUTE.DEFAULT);
		if (mapDefault != null) {
			map.put("_default", mapDefault);
		}

		for (Node entryNode = mapNode.getFirstChild(); entryNode != null; entryNode = entryNode.getNextSibling()) {
			final String entryName = getAttr(entryNode, ATTRITBUTE.NAME);
			final String entryValue = getAttr(entryNode, ATTRITBUTE.VALUE);
			map.put(entryName, entryValue);
		}
		return map;
	}

	@Override
	protected void handleMetaEntry(final String name, final String value) {
		meta.put(name, value);
	}

	@Override
	protected void handleFunctionDefinition(final Node functionDefNode) {
		// nothing to do
	}

	private String getNewId() {
		return String.valueOf(++count);
	}

	private String newOutNode() {
		final String identifier = getNewId();
		writer.println("\"" + identifier
				+ "\" [shape=\"circle\"  fillcolor=\"palegreen\" style=\"filled\" label=\"\"];");
		return identifier;
	}

	@Override
	protected void enterData(final Node node) {
		incrementChildCount();
		lastProcessorStack.push(getAttr(node, ATTRITBUTE.SOURCE));
	}



	@Override
	protected void exitData(final Node node) {
		sources.add(getAttr(node, ATTRITBUTE.SOURCE));
		exit(node);
	}

	@Override
	protected void enterCollect(final Node node) {
		incrementChildCount();
		pushChildCount();

		final String identifier = getNewId();
		lastProcessorStack.push(identifier);
		idStack.push(identifier);

		final Map<String, String> attributes = attributesToMap(node);
		attributes.remove(ATTRITBUTE.NAME.getString());
		writer.println(buildRecord(identifier, node.getLocalName(), "lightgray", attributes));
	}

	@Override
	protected void exitCollect(final Node node) {
		idStack.pop();
		childCountStack.pop();
		exit(node);
		
	}

	private void exit(final Node node) {
		String name = getAttr(node, ATTRITBUTE.NAME);
		if (name == null) {
			name = "";
		}

		final String lastProcessor = lastProcessorStack.pop();
		if (idStack.isEmpty()) {
			if (name.startsWith(RECURSION_INDICATOR)) {
				addEdge(lastProcessor, name);
				sources.add(name);
			} else {
				addEdge(lastProcessor, newOutNode(), name);
			}
		} else {
			if (ORDERED_COLLECTS.contains(node.getParentNode().getLocalName())) {
				addEdge(lastProcessor, idStack.peek(), name + "(" + childCountStack.peek() + ")");
			} else {
				addEdge(lastProcessor, idStack.peek(), name);
			}
		}

	}

	@Override
	protected void handleFunction(final Node functionNode) {
		final String identifier = getNewId();
		final Map<String, String> attributes = attributesToMap(functionNode);
		
		//for lookups TODO: find generic solution and get rid of the ifs.
		attributes.remove("default");
		String inAttr = attributes.remove("in");
		if (inAttr != null) {
			inAttr = attributes.remove("map");
		}
		if (inAttr != null) {
			addIncludeEdge(inAttr, identifier);
		}
		writer.println(buildRecord(identifier, functionNode.getLocalName(), "white", attributes));

		if (functionNode.hasChildNodes()) {
			final Map<String, String> map = getMap(functionNode);
			final String mapId = identifier + "M";
			addIncludeEdge(mapId, identifier);
			writer.println(buildMap(mapId, null, map));
		}

		addEdge(lastProcessorStack.pop(), identifier);
		lastProcessorStack.push(identifier);
	}

	private void pushChildCount() {
		childCountStack.push(Integer.valueOf(0));
	}
	
	private void incrementChildCount() {
		childCountStack.push(Integer.valueOf(1 + childCountStack.pop().intValue()));
	}
}
