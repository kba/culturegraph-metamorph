package org.culturegraph.metamorph.core;

import java.io.File;

import org.culturegraph.metamorph.Files;
import org.culturegraph.metamorph.streamreceiver.ConsoleWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilderTest {

	@Test(expected=MetamorphDefinitionException.class)
	public void testShemaVerification() {
		MetamorphBuilder.build(new File(Files.SYTAX_ERROR_MM), new ConsoleWriter());
	}
	
	@Test
	public void testBuild() {
		final Metamorph metamorph = MetamorphBuilder.build(new File(Files.PND_PICA_MM), new ConsoleWriter());
		Assert.assertNotNull(metamorph);
	}
}
