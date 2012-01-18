package org.culturegraph.metamorph.core2.functions;

import java.util.LinkedList;
import java.util.List;


/**
 * Abstraction for classes which use chains of {@link Function}s to process data. If one {@link Function}
 * in the chain returns <code>null</code>, the processing is stopped.
 * 
 * @author Markus Michael Geipel
 */
public class ValueProcessorImpl implements ValueProcessor {
	//private static final Logger LOG = LoggerFactory.getLogger(DataProcessorImpl.class);
	
	private final List<Function> functions = new LinkedList<Function>();
	

	public final String applyFunctions(final String data){
		String tempData = data;
		for (Function function : functions) {
			//tempData = function.process(tempData); //TODO change entirely!
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
