package org.culturegraph.metamorph.util.string;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Markus Michael Geipel
 *
 */
public final class SetReplacer {
	private final SetMatcher<String> matcher = new SetMatcher<String>();
	
	public void addReplacement(final String key, final String with){
		matcher.put(key, with);
	}
	
	public void addReplacements(final Map<String, String> replacements){
		for (Entry<String, String> entry : replacements.entrySet()) {
			addReplacement(entry.getKey(), entry.getValue());
		}
	}
	
	
	public String replaceIn(final String text){
		final List<SetMatcher.Match<String>> matches = matcher.match(text);
		final StringBuilder builder = new StringBuilder();
		
		int lastCut = 0;
		for (SetMatcher.Match<String> match : matches) {
			
			if(match.getStart()<lastCut){
				continue;
			}
			
			//System.out.println(match.getStart() + " "+ match.getValue() +" "+ match.getLength());
			
			builder.append(text.substring(lastCut, match.getStart()));
			builder.append(match.getValue());
			
			lastCut = match.getStart() + match.getLength();
		}
		builder.append(text.substring(lastCut, text.length()));
		
		return builder.toString();
	}
	
}
