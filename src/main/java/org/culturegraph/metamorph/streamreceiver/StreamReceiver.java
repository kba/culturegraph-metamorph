package org.culturegraph.metamorph.streamreceiver;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public interface StreamReceiver {
	void startRecord();
	void endRecord();
	
	void startEntity(String name);
	void endEntity();
	void literal(String name, String value);
}
