/**
 * 
 */
package org.culturegraph.metamorph.core.functions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class NormalizeUTF8Test {

	private static final String INPUT_STR = "Bauer, Sigmund: Über den Einfluß der Ackergeräthe auf den Reinertrag.";
	private static final String OUTPUT_STR = "Bauer, Sigmund: Über den Einfluß der Ackergeräthe auf den Reinertrag.";
	
	@Test
	public void testProcess() {
		final NormalizeUTF8 normalize = new NormalizeUTF8();
		assertEquals("Normalization incorrect", OUTPUT_STR, normalize.process(INPUT_STR));
	}
}
