package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.EntityEndListener;
import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValuePipe;
import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * Base interface for all classes which act as collectors in {@link Metamorph}
 * 
 * @author Markus Michael Geipel
 *
 */
public interface Collect extends EntityEndListener, NamedValuePipe{

	void setFlushWith(String flushEntity);
	void setSameEntity(boolean sameEntity);
	void addNamedValueSource(final NamedValueSource namedValueSource);
	void setReset(boolean reset);

	String getName();
	void setName(String name);

	

}