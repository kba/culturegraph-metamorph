package org.culturegraph.metamorph.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Regexp extends AbstractFunction {

	private Matcher matcher;
	private String format;

	
	@Override
	public String process(final String value) {
		matcher.reset(value);
		final String result;
		
		if(matcher.find()){
			if(null==format){
				result = matcher.group();
			}else{
				result = matchAndFormat();
			}
		}else{
			result = null;
		}
		
		return result;
	}
	
	private String matchAndFormat(){
		String result = format;		
		for (int i = 0; i < matcher.groupCount(); ++i) {
			
			result = result.replaceAll("\\$\\{"+i+"\\}", matcher.group(i));
		}
		return result; 
	}
	
	/**
	 * @param match the match to set
	 */
	public void setMatch(final String match) {
		this.matcher = Pattern.compile(match).matcher("");
	}

	/**
	 * @param format the output to set
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

}
