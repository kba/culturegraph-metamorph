package org.culturegraph.metamorph.core2;

public interface NamedValueSource {
	<R extends NamedValueReceiver> R setNamedValueReceiver(R receiver);
}
