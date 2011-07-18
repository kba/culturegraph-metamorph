package org.culturegraph.metamorph.core;

import java.util.LinkedList;
import java.util.List;

import org.culturegraph.metamorph.functions.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
class DataProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(Data.class);
	
	private final List<Function> functions = new LinkedList<Function>();
	
	protected final String applyFunctions(final String data){
		String tempData = data;
		for (Function function : functions) {
			tempData = function.process(tempData);
			if(LOG.isTraceEnabled()){
				LOG.trace("applied " + function.getClass().getSimpleName() + ": " + tempData);
			}
			if(tempData==null){
				break;
			}
		}
		return tempData;
	}
	
	public final void addFunction(final Function function){
		assert function != null;
		functions.add(function);
	}
}
