package org.culturegraph.metamorph.core.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metamorph.util.StringUtil;


/**
 * Performs regexp matching
 * @author Markus Michael Geipel
 */
public final class Regexp extends AbstractSimpleStatelessFunction {

	private static final String TRUE = "true";
	private Matcher matcher;
	private String format;
	private boolean exceptionOnFail;
	private final Map<String, String> tempVars = new HashMap<String, String>();
	
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
			if(exceptionOnFail){
				throw new PatternNotFoundException(matcher.pattern(), value);
			}
		}
		return result;
	}
	
	private String matchAndFormat(){
		tempVars.clear();
		for (int i = 0; i <= matcher.groupCount(); ++i) {
			tempVars.put(String.valueOf(i), matcher.group(i));
		}
		return StringUtil.format(format, tempVars); 
	}
	
	public void setExceptionOnFail(final String exceptionOnFail){
		this.exceptionOnFail = TRUE.equals(exceptionOnFail);
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
	
	/**
	 * Thrown if no match was found
	 *
	 */
	public static final class PatternNotFoundException extends MetamorphException{
		private static final long serialVersionUID = 4113458605196557204L;
		public PatternNotFoundException(final Pattern pattern, final String input) {
			super("Pattern '" + pattern + "' not found in '" + input + "'");
		}
	}

}
