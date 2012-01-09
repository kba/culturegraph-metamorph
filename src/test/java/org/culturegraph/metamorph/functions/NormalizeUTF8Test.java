/**
 * 
 */
package org.culturegraph.metamorph.functions;

import static org.junit.Assert.*;

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
		NormalizeUTF8 normalize = new NormalizeUTF8();
		
		assertEquals(OUTPUT_STR, normalize.process(INPUT_STR));
	}

}
