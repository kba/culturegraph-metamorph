package org.culturegraph.metamorph.readers;

import java.util.Set;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * tests {@link AbstractReaderFactory} and {@link StandardReaderFactory}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class ReaderFactoryTest {

	private static final String ALT_FORMAT = "hula";
	private static final int NUMBER_OF_STANDARD_FORMATS = 3;

	@BeforeClass
	@AfterClass
	public static void cleanProperties(){
		System.getProperties().remove(AbstractReaderFactory.IMPLEMENTATION_NAME);
		System.getProperties().remove(StandardReaderFactory.PROPERTY_LOCATION_NAME);
	}
	
	@Test
	public void testFactoryChange() {
		ReaderFactory readerFactory; 
		
		readerFactory = AbstractReaderFactory.newInstance();
		Assert.assertTrue("The default reader registry was not correctly loaded",
				readerFactory instanceof StandardReaderFactory);

		System.setProperty(AbstractReaderFactory.IMPLEMENTATION_NAME, DummyReaderRegistry.class.getName());
		readerFactory = AbstractReaderFactory.newInstance();
		Assert.assertTrue("The reader registry defined in system properties was not correctly loaded",
				readerFactory instanceof DummyReaderRegistry);
	}
	
	@Test
	public void testStandartReaderRegistration() {
		final ReaderFactory readerFactory = new StandardReaderFactory();
		final Set<String> formats = readerFactory.getSupportedFormats();
		
		Assert.assertTrue("missing standard formats", NUMBER_OF_STANDARD_FORMATS <= formats.size());
		
		for(String format:formats){
			Assert.assertNotNull(readerFactory.newReader(format));
		}
	}
	
	@Test
	public void testAlternativeFormatBindings() {
		ReaderFactory readerFactory;
				
		readerFactory = new StandardReaderFactory();
		Assert.assertFalse(readerFactory.isFormatSupported(ALT_FORMAT));
		
		System.setProperty(StandardReaderFactory.PROPERTY_LOCATION_NAME, "metamorph-alt-readers.properties");
		readerFactory = new StandardReaderFactory();
		Assert.assertTrue(readerFactory.isFormatSupported(ALT_FORMAT));
	}
}
