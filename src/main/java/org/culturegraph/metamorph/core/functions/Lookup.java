package org.culturegraph.metamorph.core.functions;



/**
 * @author Markus Michael Geipel
 */
public final class Lookup extends AbstractLookup {


	private String defaultValue;
	

	public void setDefault(final String defaultValue) {
		this.defaultValue = defaultValue;
	}	

	@Override
	public String process(final String key) {
		final String returnValue = lookup(key);
		 
		if(returnValue==null){
			return defaultValue;
		}
		return returnValue;
	}

	public void setIn(final String mapName) {
		setMapName(mapName);
	}
}
