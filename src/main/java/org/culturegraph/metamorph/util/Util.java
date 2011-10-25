package org.culturegraph.metamorph.util;

import java.util.Map;

public final class Util {
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
		
		while(true){
			varStart = format.indexOf("${", oldEnd);
			varEnd = format.indexOf('}', varStart);
			if(varStart < 0 || varEnd < 0){
				builder.append(formatChars, oldEnd, formatChars.length-oldEnd);
				break;
			}
			
			builder.append(formatChars, oldEnd, varStart-oldEnd);
			
			varName = format.substring(varStart+2, varEnd);
			varValue = variables.get(varName);
			if(varValue==null){
				varValue="";
			}
			builder.append(varValue);
			
			oldEnd = varEnd+1;
		}
		return builder.toString();
	}
}
