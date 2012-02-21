package org.culturegraph.metamorph.types;


/**
 * interface for classes which represent identifiable pieces of data
 * @author Markus Michael Geipel
 *
 */
public interface Record{
	void setId(final String identifier);
	String getId();
}
