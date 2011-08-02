package org.culturegraph.metamorph.core;

/**
 * Used by {@link Metamorph} to flush collected data in {@link Collect}.
 * @author Markus Michael Geipel
 */
interface EntityEndListener {
	void onEntityEnd(final String name);
}
