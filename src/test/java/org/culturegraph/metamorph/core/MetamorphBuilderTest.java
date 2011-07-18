package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.TestFiles;
import org.culturegraph.metamorph.streamreceiver.ConsoleWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class MetamorphBuilderTest {

	@Test(expected=MetamorphDefinitionException.class)
	public void testShemaVerification() {
		MetamorphBuilder.build(TestFiles.SYTAX_ERROR_MM, new ConsoleWriter());
	}
	
	@Test
	public void testBuild() {
		final Metamorph metamorph = MetamorphBuilder.build(TestFiles.PND_PICA_MM, new ConsoleWriter());
		Assert.assertNotNull(metamorph);
	}
}
