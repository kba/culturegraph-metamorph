package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.util.string.SetReplacer;



/**
 * @author Markus Michael Geipel
 */
public final class SetReplace extends AbstractSimpleStatelessFunction {

	private final SetReplacer setReplacer = new SetReplacer();
	private boolean prepared;
	
	
	@Override
	public String process(final String text) {
		if(!prepared){
			setReplacer.addReplacements(getMap());
			prepared = true;
		}
		return setReplacer.replaceIn(text);
		
	}
}
