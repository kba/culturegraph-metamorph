package org.culturegraph.metamorph.core2;

public interface NamedValueSource {
	void setNamedValueReceiver(NamedValueReceiver dataReceiver);
	void setName(String name);
	void setValue(String value);
	String getName();
	String getValue(); 
}
