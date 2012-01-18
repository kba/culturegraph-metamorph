package org.culturegraph.metamorph.core2.functions;

public interface ValueReceiver {
	void receive(String value, int recordCount, int entityCount);
}
