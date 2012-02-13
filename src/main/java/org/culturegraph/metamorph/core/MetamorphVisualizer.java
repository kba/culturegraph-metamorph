package org.culturegraph.metamorph.core;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.culturegraph.metamorph.types.ListMap;
import org.w3c.dom.Node;

/**
 * Visualized a Metamorph definition in dot format
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class MetamorphVisualizer extends AbstractMetamorphDomWalker {

	private final Map<String, String> meta = new HashMap<String, String>();
	private final PrintWriter writer;
	private int count;
	private final Deque<String> idStack = new LinkedList<String>();
	private final ListMap<String, String> sourceIdMap = new ListMap<String, String>();
	private final StringBuilder edgeBuffer = new StringBuilder();
	private String currentId;
	private String currentTarget;

	public MetamorphVisualizer(final Writer writer) {
		super();
		this.writer = new PrintWriter(writer);
	}

	@Override
	protected void init() {

		writer.println("digraph g {\n" + "graph [ rankdir = \"LR\" ];\n"
				+ "node [ fontsize = \"11\"  shape = \"record\"];\n" + "edge [ ];\n");
	}

	@Override
	protected void finish() {
		
		
		for (String source : sourceIdMap.keySet()) {
			writer.println("\"" + source + "\" [label=\"" + source + "\" shape=\"ellipse\"];");
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
		final String mapDefault = getAttr(mapNode, ATTRITBUTE.DEFAULT);
		
		final StringBuilder builder = new StringBuilder();
		builder.append("{"	+ mapName + "}");
		builder.append("|{_default| "+mapDefault+"}");
		
		for (Node entryNode = mapNode.getFirstChild(); entryNode != null; entryNode = entryNode.getNextSibling()) {
			final String entryName = getAttr(entryNode, ATTRITBUTE.NAME);
			final String entryValue = getAttr(entryNode, ATTRITBUTE.VALUE);
			builder.append("|{"+entryName+"| "+entryValue+"}");
		}
		
		writer.println("\"" + mapName + "\" [label = \""+builder+"\"];");
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

	@Override
	protected void enterData(final Node node) {
		final String identifier = "D" + getNewId();
		currentId = identifier;
		
		
		final String name = getAttr(node, ATTRITBUTE.NAME);
		final String source = getAttr(node, ATTRITBUTE.SOURCE);
		
		if(name==null){
			currentId = source;
			
			
		}else{
			idStack.push(identifier);
			
			final StringBuilder builder = new StringBuilder();
			final Map<String, String> attributes = attributesToMap(node);
			attributes.remove(ATTRITBUTE.SOURCE.getString());
			
			for (Entry<String, String> entry  : attributes.entrySet()) {
				builder.append("{" + entry.getKey() + "|" +entry.getValue()+ "}|");
			}
			builder.delete(builder.length()-1, builder.length());
			writer.println("\"" + identifier + "\" [label = \""+builder+"\"];");
			
			edgeBuffer.append("\"" + source + "\" -> \"" + identifier + "\" \n");
			
			if(name.startsWith("@")){
				//edgeBuffer.append("\"" + identifier + "\" -> \"" + name + "\" \n");
				currentTarget = name;
			}else{
				currentTarget = null;
			}
		}
		

	}

	@Override
	protected void exitData(final Node node) {
		final String name = getAttr(node, ATTRITBUTE.NAME);
		final String childId;
		if(name==null){
			childId = getAttr(node, ATTRITBUTE.SOURCE);
			sourceIdMap.put(childId, idStack.peek());
		}else{
			childId = idStack.pop();
			sourceIdMap.put(getAttr(node, ATTRITBUTE.SOURCE), childId);
		}
		
		
		
		if (!idStack.isEmpty()) {
			final String parentId = idStack.peek();
			edgeBuffer.append("\"" + childId + "\" -> \"" + parentId + "\" \n");
		}else if(currentTarget != null){
			edgeBuffer.append("\"" + currentId + "\" -> \"" + currentTarget + "\" \n");
		}
	}

	@Override
	protected void enterCollect(final Node node) {
		final String identifier = "C" + getNewId();
		idStack.push(identifier);
		
		String name = getAttr(node, ATTRITBUTE.NAME);
		if(name==null){
			name="";
		}			
		
		final StringBuilder builder = new StringBuilder();
		final Map<String, String> attributes = attributesToMap(node);
		attributes.remove(ATTRITBUTE.SOURCE.getString());
		
		builder.append("{" + node.getLocalName() + "}");
		for (Entry<String, String> entry  : attributes.entrySet()) {
			builder.append("|{" + entry.getKey() + "|" +entry.getValue().replaceAll("\\{","\\\\{").replaceAll("\\}","\\\\}")+ "}");
		}
		
		writer.println("\"" + identifier + "\" [label = \"" +builder +"\"];");
		
		if(name.startsWith("@")){
			edgeBuffer.append("\"" + identifier + "\" -> \"" + name + "\" \n");
		}
	}

	@Override
	protected void exitCollect(final Node node) {
		final String childId = idStack.pop();
		if (!idStack.isEmpty()) {
			final String parentId = idStack.peek();
			writer.println("\"" + childId + "\" -> \"" + parentId + "\"");
		}
	}

	@Override
	protected void handleFunction(final Node functionNode) {
		final String identifier = "F" + getNewId();
		
		writer.println("\"" + identifier + "\" [label = \""+ functionNode.getLocalName() +"\"];");
		
		edgeBuffer.append("\"" + currentId + "\" -> \"" + identifier + "\" \n");
		currentId = identifier;
	}
}
