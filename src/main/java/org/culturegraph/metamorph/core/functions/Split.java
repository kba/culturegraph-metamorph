package org.culturegraph.metamorph.core.functions;

import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * @author Markus Michael Geipel
 */
public final class Split extends AbstractFunction{
	
	private Pattern delimiterPattern;
	
	@Override
	public void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		final String[] parts = delimiterPattern.split(value);
		for (String part : parts) {
			getNamedValueReceiver().receive(name, part, source, recordCount, entityCount);
		}
	}


	public void setDelimiter(final String delimiter) {
		this.delimiterPattern = Pattern.compile(delimiter);
	}
}
