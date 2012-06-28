package org.culturegraph.metamorph.core.functions;



/**
 * @author Markus Michael Geipel
 */
abstract class AbstractLookup extends AbstractSimpleStatelessFunction {
	
	protected final String lookup(final String key){
		final String returnValue;
		if(getMapName()==null){
			returnValue= getLocalValue(key);
		}else{
			returnValue= getValue(getMapName(), key);
		}
		return returnValue;
	}


	
}
