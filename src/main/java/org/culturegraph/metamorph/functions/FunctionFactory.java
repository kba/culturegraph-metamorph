package org.culturegraph.metamorph.functions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Properties;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FunctionFactory {

	private static final String PROPERTIES_LOCATION = "function-mapping.properties";
	
	private static final String INSTANTIATION_PROBLEM = " could not be instantiated";
	
		
	private static final Logger LOG = LoggerFactory.getLogger(FunctionFactory.class);

	private final Map<String, Class<? extends Function>> functionClasses = new HashMap<String, Class<? extends Function>>();
	private final Map<Class<? extends Function>, Map<String, Method>> functionMethodMaps = new HashMap<Class<? extends Function>, Map<String, Method>>();
	private final Set<String> availableFunctions;

	public FunctionFactory() {
		super();

		final InputStream inStream = FunctionFactory.class.getResourceAsStream(PROPERTIES_LOCATION);
		final Properties properties = new Properties();

		if(inStream==null){
			throw new MetamorphException(PROPERTIES_LOCATION + " not found");
		}
		try {
			properties.load(inStream);
		} catch (IOException e) {
			throw new MetamorphException("'"+ PROPERTIES_LOCATION + "' could not be loaded", e);
		} 

		for (Entry<Object, Object> entry : properties.entrySet()) {
			final String className = entry.getValue().toString();
			final String name = entry.getKey().toString();
			registerFunction(name, className);
		}
		availableFunctions = Collections.unmodifiableSet(functionClasses.keySet());
	}
	
	@SuppressWarnings("unchecked")
	// protected by "if (Function.class.isAssignableFrom(clazz)) {"
	private void registerFunction(final String name, final String className) {
		try {
			final Class<? extends Function> clazz = (Class<? extends Function>)Util.getClassLoader().loadClass(className);
			if (Function.class.isAssignableFrom(clazz)) {
				functionClasses.put(name,  clazz);
				LOG.debug("Reader for '" + name + "': " + className);
				
				final Map<String, Method> methodMap = new HashMap<String, Method>();
				functionMethodMaps.put(clazz, methodMap);
				for (Method method : clazz.getMethods()) {
					final String methodName = method.getName().replace("set", "").toLowerCase();
					methodMap.put(methodName, method);
				}
				
			} else {
				LOG.warn(className + " does not implement " + Function.class.getName() + " registration with "
						+ FunctionFactory.class.getSimpleName() + " failed.");
			}
		} catch (ClassNotFoundException e) {
			LOG.warn(e.getMessage(), e);
		}
		
	}

	public Set<String> getAvailableFunctions(){
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
