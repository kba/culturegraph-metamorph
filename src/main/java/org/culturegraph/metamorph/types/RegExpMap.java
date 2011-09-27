package org.culturegraph.metamorph.types;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.MetamorphDefinitionException;

public final class RegExpMap<V> {
	
	private Pattern pattern;
	private final StringBuilder patternBuilder = new StringBuilder();
	private final List<V> values = new ArrayList<V>();
	
	public void put(final String key, final V value){

		
		final Pattern tempPattern = Pattern.compile(key);
		
		if(tempPattern.matcher("").groupCount()!=0){
			throw new MetamorphDefinitionException("RegExp must not contain matchgroups");
		}
		
		patternBuilder.append("|(^");
		patternBuilder.append(key);
		patternBuilder.append("$)");
		pattern = Pattern.compile(patternBuilder.toString());
		
		values.add(value);
		
		
	}
	
	public V get(final String key){
		final Matcher matcher = pattern.matcher(key);
		
		if(matcher.find()){
			final int numMatchGroups = matcher.groupCount();
			for(int i=0;i<numMatchGroups;++i){
				if(matcher.group(i)!=null){
					//TODO ...
				}
			}
		}
		
		return values.get(0);
	}
}
