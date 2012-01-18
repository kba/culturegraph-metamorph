package org.culturegraph.metamorph.functions2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.culturegraph.metamorph.core.MetamorphException;

/**
 * @author Markus Michael Geipel
 */
final class URLEncode extends AbstractSimpleStatelessFunction {

	@Override
	public String process(final String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new MetamorphException(e);
		}
	}
}
