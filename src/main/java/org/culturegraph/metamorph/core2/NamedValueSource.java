package org.culturegraph.metamorph.core2;

public interface NamedValueSource {
	<R extends NamedValueReceiver> R setNamedValueReceiver(R receiver);
//	void setName(String name);
//	void setValue(String value);
//	String getName();
//	String getValue(); 
}
