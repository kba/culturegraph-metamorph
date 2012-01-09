/**
 * 
 */
package org.culturegraph.metamorph.functions;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Performs normalization of diacritics in utf-8 encoded strings.
 *   
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class NormalizeUTF8 extends AbstractFunction {

	@Override
	public String process(String value) {
		if (value != null) {
			return Normalizer.normalize(value, Form.NFC);
		}
		return null;
	}

}
