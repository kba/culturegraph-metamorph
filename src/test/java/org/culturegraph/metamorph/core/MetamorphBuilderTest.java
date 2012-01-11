package org.culturegraph.metamorph.core;

import java.io.File;

import org.culturegraph.metamorph.DataFilePath;
import org.junit.Assert;
import org.junit.Test;

/**
 * tests {@link MetamorphBuilder} and {@link MetamorphDefinitionHandler}
 * @author Markus Michael Geipel
 */
public final class MetamorphBuilderTest {

	@Test(expected=MetamorphDefinitionException.class)
	public void testShemaVerification() {
		MetamorphBuilder.build(new File(DataFilePath.SYTAX_ERROR_MM));
	}
	
	@Test
	public void testBuild() {
		final Metamorph metamorph = MetamorphBuilder.build(new File(DataFilePath.PND_PICA_MM));
		Assert.assertNotNull(metamorph);
	}
}
