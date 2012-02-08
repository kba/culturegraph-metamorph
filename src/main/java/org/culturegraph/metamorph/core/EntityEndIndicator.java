package org.culturegraph.metamorph.core;

/**
 * Interface for classes which signal entity end events
 * 
 * @author Markus Michael Geipel
 *
 */
public interface EntityEndIndicator {
	String RECORD_KEYWORD = "record";

	void addEntityEndListener(EntityEndListener entityEndListener, String entityName) ;
}
