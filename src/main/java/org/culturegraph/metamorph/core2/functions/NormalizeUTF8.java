/**
 * 
 */
package org.culturegraph.metamorph.core2.functions;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Performs normalization of diacritics in utf-8 encoded strings.
 *   
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
final class NormalizeUTF8 extends AbstractSimpleStatelessFunction {

	@Override
	public String process(final String value) {
		if (value != null) {
			return Normalizer.normalize(value, Form.NFC);
		}
		return null;
	}

}
