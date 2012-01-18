package org.culturegraph.metamorph.functions2;

public interface ValueReceiver {
	void receive(String value, int recordCount, int entityCount);
}
