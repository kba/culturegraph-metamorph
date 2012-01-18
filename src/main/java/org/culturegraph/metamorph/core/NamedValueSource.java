package org.culturegraph.metamorph.core;

public interface NamedValueSource {
	void setNamedValueReceiver(NamedValueReceiver dataReceiver);
	void setName(String name);
	void setValue(String value);
	String getName();
	String getValue(); 
}
