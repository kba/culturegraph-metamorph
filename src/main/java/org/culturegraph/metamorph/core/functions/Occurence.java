/**
 * 
 */
package org.culturegraph.metamorph.core.functions;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.util.StringUtil;



/**
 * @author Markus Michael Geipel
 *
 */
public final class Occurence extends AbstractStatefulFunction {

	private int count;
	private int only;
	private String format;
	
	private final Map<String, String> variables = new HashMap<String, String>();
	
	@Override
	public String process(final String value) {
		++count;
		if(count==only){
			if(format==null){
				return value;
			}
			variables.put("value", value);
			variables.put("count", String.valueOf(count));
			return StringUtil.format(format, variables);
		}
		return null;
	}
	
	public void setFormat(final String format) {
		this.format = format;
	}

	@Override
	protected void reset() {
		count=0;
	}

	@Override
	protected boolean doResetOnEntityChange() {
		return false;
	}

	public void setOnly(final int only) {
		this.only = only;
	}
}
