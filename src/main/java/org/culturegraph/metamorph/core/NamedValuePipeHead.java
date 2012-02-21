package org.culturegraph.metamorph.core;

/**
 * just a combination of both {@link NamedValueReceiver} and {@link NamedValueSource}
 * 
 * @author Markus Michael Geipel
 *
 */
public interface NamedValuePipeHead extends NamedValuePipe{
	void appendPipe(NamedValuePipe namedValuePipe);
	void endPipe(NamedValueReceiver lastReceiver);
}
