package org.culturegraph.metamorph.core;

public interface NamedValueSource {
	<R extends NamedValueReceiver> R setNamedValueReceiver(R receiver);
}
