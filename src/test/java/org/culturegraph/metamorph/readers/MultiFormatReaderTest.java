package org.culturegraph.metamorph.readers;

import org.junit.Assert;
import org.junit.Test;

public final class MultiFormatReaderTest {
	
	private static final String PICA = "pica";
	private static final String MAB2 = "mab2";
	

	@Test(expected=IllegalStateException.class)
	public void testMissingFormat(){
		final MultiFormatReader formatReader = new MultiFormatReader();
		formatReader.read("gurk");
	}
	
	@Test
	public void testFormatSwitch(){
		final MultiFormatReader formatReader = new MultiFormatReader();
		formatReader.setFormat(PICA);
		Assert.assertEquals(PICA, formatReader.getFormat());
		formatReader.setFormat(MAB2);
		Assert.assertEquals(MAB2, formatReader.getFormat());
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMissingReceiver(){	
		final MultiFormatReader formatReader = new MultiFormatReader();
		formatReader.setFormat(PICA);
		formatReader.read("hula");
	}
	
	@Test
	public void testMorph(){	
		final MultiFormatReader formatReader = new MultiFormatReader("ingest");
		formatReader.setFormat(PICA);
		Assert.assertNotNull(formatReader.getMetamorph());
	}
}
