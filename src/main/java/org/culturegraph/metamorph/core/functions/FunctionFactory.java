package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.util.ObjectFactory;

/**
 * Provides the functions for {@link Metamorph}. 
 * By the default it contains the standard function set.
 * New functions can be registered during runtime.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class FunctionFactory extends ObjectFactory<Function>{


	public FunctionFactory() {
		super();

		registerClass("regexp", Regexp.class);
		registerClass("substring", Substring.class);
		registerClass("compose", Compose.class);
		registerClass("lookup", Lookup.class);
		registerClass("whitelist", WhiteList.class);
		registerClass("blacklist", BlackList.class);
		registerClass("replace", Replace.class);
		registerClass("isbn", ISBN.class);
		registerClass("equals", Equals.class);
		registerClass("htmlanchor", HtmlAnchor.class);
		registerClass("trim", Trim.class);
		registerClass("normalize-utf8", NormalizeUTF8.class);
		registerClass("urlencode", URLEncode.class);
		registerClass("split", Split.class);
		registerClass("occurence", Occurence.class);
		registerClass("constant", Constant.class);
		registerClass("count", Count.class);
	}

}
