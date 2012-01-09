package org.culturegraph.metamorph.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core.MetamorphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FunctionFactory {



	private static final String INSTANTIATION_PROBLEM = " could not be instantiated";

	private static final Logger LOG = LoggerFactory.getLogger(FunctionFactory.class);

	private final Map<String, Class<? extends Function>> functionClasses = new HashMap<String, Class<? extends Function>>();
	private final Map<Class<? extends Function>, Map<String, Method>> functionMethodMaps = new HashMap<Class<? extends Function>, Map<String, Method>>();
	private final Set<String> availableFunctions;

	public FunctionFactory() {
		super();

		registerFunction("constant", Constant.class);
		registerFunction("regexp", Regexp.class);
		registerFunction("substring", Substring.class);
		registerFunction("compose", Compose.class);
		registerFunction("lookup", Lookup.class);
		registerFunction("replace", Replace.class);
		registerFunction("isbn", ISBN.class);
		registerFunction("equals", Equals.class);
		registerFunction("htmlanchor", HtmlAnchor.class);
		registerFunction("normalize-utf8", NormalizeUTF8.class);
		
		availableFunctions = Collections.unmodifiableSet(functionClasses.keySet());
	}


	public void registerFunction(final String name, final Class<? extends Function> clazz) {

		functionClasses.put(name, clazz);
		LOG.debug("Registered function '" + name + "': " + clazz.getName());

		final Map<String, Method> methodMap = new HashMap<String, Method>();
		functionMethodMaps.put(clazz, methodMap);
		for (Method method : clazz.getMethods()) {
			final String methodName = method.getName().replace("set", "").toLowerCase();
			methodMap.put(methodName, method);
		}

	}

	public Set<String> getAvailableFunctions() {
		return availableFunctions;
	}

	public Function newFunction(final String name, final Map<String, String> attributes) {
		if (!functionClasses.containsKey(name)) {
			throw new IllegalArgumentException("function '" + name + "' not found");
		}
		final Class<? extends Function> clazz = functionClasses.get(name);
		try {
			final Function function = clazz.newInstance();
			for (Map.Entry<String, String> attribute : attributes.entrySet()) {
				final String methodName = attribute.getKey().toLowerCase();
				final Method method = functionMethodMaps.get(clazz).get(methodName);
				if(null==method){
					throw new MetamorphException("Cannot set '" + methodName + "' for function '" + name +"'");
				}
				method.invoke(function, attribute.getValue());
			}
			return function;

		} catch (InstantiationException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (IllegalAccessException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (IllegalArgumentException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (InvocationTargetException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		}
	}
}
