package org.culturegraph.metamorph.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core.MetamorphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides instances of preregistered classes.
 * New classes can be registered during runtime.
 * 
 * @author Markus Michael Geipel
 *
 */
public class ObjectFactory<O> {

	private static final String INSTANTIATION_PROBLEM = " could not be instantiated";

	private static final Logger LOG = LoggerFactory.getLogger(ObjectFactory.class);

	private final Map<String, Class<? extends O>> classes = new HashMap<String, Class<? extends O>>();
	private final Map<Class<? extends O>, Map<String, Method>> classMethodMaps = new HashMap<Class<? extends O>, Map<String, Method>>();
	private final Set<String> availableClasses = Collections.unmodifiableSet(classes.keySet()); 

	public final void registerClass(final String name, final Class<? extends O> clazz) {

		classes.put(name, clazz);
		LOG.debug("Registered function '" + name + "': " + clazz.getName());

		final Map<String, Method> methodMap = new HashMap<String, Method>();
		classMethodMaps.put(clazz, methodMap);
		for (Method method : clazz.getMethods()) {
			final String methodName = method.getName().replace("set", "").toLowerCase();
			methodMap.put(methodName, method);
		}

	}

	public final Set<String> getAvailableClasses() {
		return availableClasses;
	}

	public final O newInstance(final String name, final Map<String, String> attributes) {
		if (!classes.containsKey(name)) {
			throw new IllegalArgumentException("class '" + name + "' not found");
		}
		final Class<? extends O> clazz = classes.get(name);
		try {
			final O instance = clazz.newInstance();
			for (Map.Entry<String, String> attribute : attributes.entrySet()) {
				final String methodName = attribute.getKey().toLowerCase();
				final Method method = classMethodMaps.get(clazz).get(methodName);
				if(null==method){
					throw new MetamorphException("Cannot set '" + methodName + "' for class '" + name +"'");
				}
				method.invoke(instance, attribute.getValue());
			}
			return instance;

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
