package org.culturegraph.metamorph.core2.functions;



/**
 * @author Markus Michael Geipel
 */
final class WhiteList extends AbstractLookup {


	@Override
	public String process(final String key) {
		final String returnValue = lookup(key);
		 
		if(returnValue!=null){
			return key;
		}
		return null;
	}

	public void setList(final String mapName) {
		setMapName(mapName);
	}
}
