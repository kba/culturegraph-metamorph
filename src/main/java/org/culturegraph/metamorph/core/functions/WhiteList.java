package org.culturegraph.metamorph.core.functions;



/**
 * @author Markus Michael Geipel
 */
public final class WhiteList extends AbstractLookup {


	@Override
	public String process(final String key) {
		final String returnValue = lookup(key);
		 
		if(returnValue!=null){
			return key;
		}
		return null;
	}
}
