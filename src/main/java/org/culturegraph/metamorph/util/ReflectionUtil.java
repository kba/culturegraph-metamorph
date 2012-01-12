package org.culturegraph.metamorph.util;

import org.culturegraph.metamorph.core.MetamorphException;

public final class ReflectionUtil {
	private static final String INSTANTIATION_ERROR = " could not be instantiated";

	private ReflectionUtil() {
		// no instances
	}
	

	public static ClassLoader getClassLoader(){
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			throw new MetamorphException("Class loader could not be found.");
		}
		return loader;
	}

	public static Object instantiateClass(final String className) {
		if (className == null) {
			throw new IllegalArgumentException("'className' must not be null.");
		}
		try {
			final Class<?> clazz = getClassLoader().loadClass(className);
			return clazz.newInstance();
			
		} catch (ClassNotFoundException e) {
			throw new MetamorphException(className + INSTANTIATION_ERROR, e);
		} catch (InstantiationException e) {
			throw new MetamorphException(className + INSTANTIATION_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new MetamorphException(className + INSTANTIATION_ERROR, e);
		}
	}
}
