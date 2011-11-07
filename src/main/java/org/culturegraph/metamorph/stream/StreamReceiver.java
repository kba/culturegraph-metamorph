package org.culturegraph.metamorph.stream;

/**
 * @author Markus Michael Geipel
 */
public interface StreamReceiver {
	void startRecord(String identifier);
	void endRecord();
	
	void startEntity(String name);
	void endEntity();
	void literal(String name, String value);
}
