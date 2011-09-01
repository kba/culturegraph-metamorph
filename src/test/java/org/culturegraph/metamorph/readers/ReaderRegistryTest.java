package org.culturegraph.metamorph.readers;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link MabReader}. So far only verifies that the correct number of records and fields is read.
 * @author Markus Michael Geipel
 * @see MabReader
 */
public final class ReaderRegistryTest {
	
	
	@Test
	public void testRegistry() throws IOException {
		final ReaderRegistry registry = new ReaderRegistry();
		Assert.assertNull(registry.getReaderForFormat("nothing"));
		Assert.assertFalse(registry.isFormatSupported("lskdfj"));
		
		registry.addReaderFactory(Format.PICA, new PicaReaderFactory());
		
		Assert.assertTrue(registry.isFormatSupported(Format.PICA));
		final RawRecordReader reader = registry.getReaderForFormat(Format.PICA);
		
		Assert.assertNotNull(reader);
		Assert.assertTrue(reader instanceof PicaReader);
		
	}
}
