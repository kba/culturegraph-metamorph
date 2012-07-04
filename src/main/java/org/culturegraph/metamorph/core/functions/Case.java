package org.culturegraph.metamorph.core.functions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.culturegraph.metamorph.core.MetamorphDefinitionException;

/**
 * @author Markus Michael Geipel
 */
public final class Case extends AbstractSimpleStatelessFunction {

	private static final String UPPER = "upper";
	private static final Set<String> LANGUAGES;
	private Locale locale = Locale.getDefault();
	private boolean toUpper;

	static{
		final Set<String> set = new HashSet<String>();
		Collections.addAll(set, Locale.getISOLanguages());
		LANGUAGES = Collections.unmodifiableSet(set);
	}
	
	@Override
	public String process(final String value) {
		if(toUpper){
			return value.toUpperCase(locale);
		}
		return value.toLowerCase(locale);
	}

	/**
	 * @param string
	 */
	public void setTo(final String string) {
		this.toUpper = UPPER.equals(string);
	}
	
	public void setLanguage(final String language){
		if(!LANGUAGES.contains(language)){
			throw new MetamorphDefinitionException("Language " + language + " not supported.");
		}
		this.locale = new Locale(language);
	}
}
