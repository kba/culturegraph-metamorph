package org.culturegraph.metamorph.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides instances of preregistered classes.
 * New classes can be registered during runtime.
 * 
 * @author Markus Michael Geipel
 * @param <O> the type of objects created
 *
 */
public class ObjectFactory<O> {

	private static final String INSTANTIATION_PROBLEM = " could not be instantiated";

	private static final Logger LOG = LoggerFactory.getLogger(ObjectFactory.class);

	private static final String SET = "set";

	private final Map<String, Class<? extends O>> classes = new HashMap<String, Class<? extends O>>();
	private final Map<Class<? extends O>, Map<String, Method>> classMethodMaps = new HashMap<Class<? extends O>, Map<String, Method>>();
	private final Set<String> availableClasses = Collections.unmodifiableSet(classes.keySet()); 

	public final void registerClass(final String name, final Class<? extends O> clazz) {

		classes.put(name, clazz);
		LOG.debug("Registered class '" + name + "': " + clazz.getName());

		final Map<String, Method> methodMap = new HashMap<String, Method>();
		classMethodMaps.put(clazz, methodMap);
		for (Method method : clazz.getMethods()) {
			if(method.getParameterTypes().length==1 && method.getName().startsWith(SET)){
				final String methodName = method.getName().substring(SET.length()).toLowerCase();
				methodMap.put(methodName, method);
			}
		}
	}

	public final Set<String> getAvailableClasses() {
		return availableClasses;
	}

	public final O newInstance(final String name,  final Map<String, String> attributes, final Object...contructorArgs) {
		if (!classes.containsKey(name)) {
			throw new IllegalArgumentException("class '" + name + "' not found");
		}
		
		final Class<? extends O> clazz = classes.get(name);
		try {
			final Class<?>[] contructorArgTypes = new Class[contructorArgs.length];
			for (int i = 0; i < contructorArgs.length; ++i) {
				contructorArgTypes[i] = contructorArgs[i].getClass();
			}
				
			final Constructor<? extends O> constructor = clazz.getConstructor(contructorArgTypes);
			final O instance = constructor.newInstance(contructorArgs);
			applySetters(instance, classMethodMaps.get(clazz), attributes);
			return instance;

		} catch (InstantiationException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (SecurityException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (NoSuchMethodException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (IllegalArgumentException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (IllegalAccessException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (InvocationTargetException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} 
	}

	private void applySetters(final O instance, final Map<String, Method> methodMap, final Map<String, String> attributes)   {
		
		for (Map.Entry<String, String> attribute : attributes.entrySet()) {
			final String methodName = attribute.getKey().toLowerCase();
			final Method method = methodMap.get(methodName);
			if(null==method){
				setMethodError(methodName, instance.getClass().getSimpleName());
			}
			final Class<?> type = method.getParameterTypes()[0];
			
			try{
			if(type == boolean.class){
				method.invoke(instance, Boolean.valueOf(attribute.getValue()));
			}else if(type == int.class){
				method.invoke(instance, Integer.valueOf(attribute.getValue()));
			}else{
				method.invoke(instance, attribute.getValue());
			}
			}catch(IllegalArgumentException e){
				setMethodError(methodName, instance.getClass().getSimpleName());
			} catch (IllegalAccessException e) {
				setMethodError(methodName, instance.getClass().getSimpleName());
			} catch (InvocationTargetException e) {
				setMethodError(methodName, instance.getClass().getSimpleName());
			} 
		}
	}

	private void setMethodError(final String methodName, final String simpleName) {
		throw new MetamorphException("Cannot set '" + methodName + "' for class '" + simpleName + "'");
		
	}
}
