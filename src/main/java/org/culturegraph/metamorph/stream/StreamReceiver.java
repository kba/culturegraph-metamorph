package org.culturegraph.metamorph.stream;

/**
 * @author Markus Michael Geipel
 */
public interface StreamReceiver {
	void startRecord();
	void endRecord();
	
	void startEntity(String name);
	void endEntity();
	void literal(String name, String value);
}
