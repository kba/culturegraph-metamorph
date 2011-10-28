package org.culturegraph.metamorph.util;

import java.util.Map;

import org.culturegraph.metamorph.core.MetamorphException;

public final class Util {
	private static final String INSTANTIATION_ERROR = " could not be instantiated";

	private Util() {
		// no instances
	}

	public static String format(final String format, final Map<String, String> variables) {
		final StringBuilder builder = new StringBuilder();
		final char[] formatChars = format.toCharArray();
		int varStart = 0;
		int varEnd = 0;
		int oldEnd = 0;
		String varName;
		String varValue;

		while (true) {
			varStart = format.indexOf("${", oldEnd);
			varEnd = format.indexOf('}', varStart);
			if (varStart < 0 || varEnd < 0) {
				builder.append(formatChars, oldEnd, formatChars.length - oldEnd);
				break;
			}

			builder.append(formatChars, oldEnd, varStart - oldEnd);

			varName = format.substring(varStart + 2, varEnd);
			varValue = variables.get(varName);
			if (varValue == null) {
				varValue = "";
			}
			builder.append(varValue);

			oldEnd = varEnd + 1;
		}
		return builder.toString();
	}

	public static Object instantiateClass(final String className) {
		if (className == null) {
			throw new IllegalArgumentException("'className' must not be null.");
		}
		try {
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader == null) {
				throw new MetamorphException("Class loader could not be found.");
			}
			final Class<?> clazz = loader.loadClass(className);
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
