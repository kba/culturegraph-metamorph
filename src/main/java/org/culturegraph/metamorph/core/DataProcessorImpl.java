package org.culturegraph.metamorph.core;

import java.util.LinkedList;
import java.util.List;

import org.culturegraph.metamorph.functions.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstraction for classes which use chains of {@link Function}s to process data. If one {@link Function}
 * in the chain returns <code>null</code>, the processing is stopped.
 * 
 * @author Markus Michael Geipel
 */
class DataProcessorImpl implements DataProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(DataProcessorImpl.class);
	
	private final List<Function> functions = new LinkedList<Function>();
	

	public final String applyFunctions(final String data){
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
	

	@Override
	public final void addFunction(final Function function){
		assert function != null;
		functions.add(function);
	}
}
