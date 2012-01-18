package org.culturegraph.metamorph.core2;

/**
 * Used by {@link Metamorph} to flush collected data in {@link Collect}.
 * @author Markus Michael Geipel
 */
public interface EntityEndListener {
	void onEntityEnd(final String name, int recordCount, int entityCount);
}
