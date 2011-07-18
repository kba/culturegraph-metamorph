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
		final MetamorphBuilder builder = new MetamorphBuilder();
		builder.setDefinitionFile(TestFiles.SYTAX_ERROR_MM);
		builder.setOutputHandler(new ConsoleWriter());
		builder.build();
	}
	
	@Test
	public void testBuild() {
		final MetamorphBuilder builder = new MetamorphBuilder();
		builder.setDefinitionFile(TestFiles.PND_PICA_MM);
		builder.setOutputHandler(new ConsoleWriter());
		final Metamorph metamorph = builder.build();
		Assert.assertNotNull(metamorph);
	}
}
