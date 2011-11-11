package org.culturegraph.metamorph.stream;

/**
 * @author Markus Michael Geipel
 */
public interface StreamReceiver {
	String ID_NAME = "_id";
	
	void startRecord(String identifier);
	void endRecord();
	
	void startEntity(String name);
	void endEntity();
	void literal(String name, String value);
}
