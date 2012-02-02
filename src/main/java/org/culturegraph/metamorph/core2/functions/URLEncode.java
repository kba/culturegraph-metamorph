package org.culturegraph.metamorph.core2.functions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.culturegraph.metamorph.core2.exceptions.MetamorphException;

/**
 * @author Markus Michael Geipel
 */
public final class URLEncode extends AbstractSimpleStatelessFunction {

	@Override
	public String process(final String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new MetamorphException(e);
		}
	}
}
