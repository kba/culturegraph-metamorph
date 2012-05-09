package org.culturegraph.metamorph.core.functions;

import java.io.FileNotFoundException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.culturegraph.metamorph.core.exceptions.MetamorphDefinitionException;
import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metamorph.util.ResourceUtil;

/**
 * @author Markus Michael Geipel
 */
public final class Script extends AbstractSimpleStatelessFunction {

	//private static final Logger LOG = LoggerFactory.getLogger(Script.class);
	
	private Invocable invocable;
	private String invoke;
	
	public void setInvoke(final String invoke){
		this.invoke = invoke;
	}

	public void setFile(final String file) {

		final ScriptEngineManager manager = new ScriptEngineManager();
		final ScriptEngine engine = manager.getEngineByName("JavaScript");
		try {
			//LOG.info("loading code from '" + file + "'");
			engine.eval(ResourceUtil.getReader(file));
		} catch (ScriptException e) {
			throw new MetamorphDefinitionException("Error in script", e);
		} catch (FileNotFoundException e) {
			throw new MetamorphDefinitionException("Error loading script '" + file + "'", e);
		}
		invocable = (Invocable) engine;
	}
	
	@Override
	public String process(final String value) {
		final Object obj;
		try {
			//LOG.info("processing: " + value);
			obj = invocable.invokeFunction(invoke, value);
			//LOG.info("returning: " + obj);
			return obj.toString();
		} catch (ScriptException e) {
			throw new MetamorphException("Error in script while evaluating 'process' method", e);
		} catch (NoSuchMethodException e) {
			throw new MetamorphException("'process' method is missing in script", e);
		}
	}
}
