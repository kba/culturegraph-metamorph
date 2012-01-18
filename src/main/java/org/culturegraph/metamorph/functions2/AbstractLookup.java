package org.culturegraph.metamorph.functions2;



/**
 * @author Markus Michael Geipel
 */
abstract class AbstractLookup extends AbstractSimpleStatelessFunction {

	private String mapName;

	
	protected final String lookup(final String key){
		final String returnValue;
		if(mapName==null){
			returnValue= getLocalValue(key);
		}else{
			returnValue= getValue(mapName, key);
		}
		return returnValue;
	}


	protected final void setMapName(final String mapName) {
		this.mapName = mapName;
	}
}
