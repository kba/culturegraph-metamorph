package org.culturegraph.metamorph.util;

import java.util.Map;

/**
 * Basic formating utils
 * 
 * @author Markus Michael Geipel
 *
 */
public final class StringUtil {

	private StringUtil() {
		// no instances
	}
	
	public static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}

	public static String format(final String format, final Map<String, String> variables) {
		if(format.indexOf('$')<0){ // shortcut if there is nothing to replace
			return format;
		}
		
		int varStart = 0;
		int varEnd = 0;
		int oldEnd = 0;
		String varName;
		String varValue;

		final StringBuilder builder = new StringBuilder();
		final char[] formatChars = format.toCharArray();
		
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
	
	
}
