package org.culturegraph.metamorph.functions;


/**
 * @author Markus Michael Geipel
 */
final class Lookup extends AbstractFunction {


	private String mapName;

	@Override
	public String process(final String key) {
		return getValue(mapName, key);
	}

	public void setIn(final String datastoreName) {
		this.mapName = datastoreName;
	}
}
