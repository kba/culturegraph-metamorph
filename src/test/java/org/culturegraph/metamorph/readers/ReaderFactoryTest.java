package org.culturegraph.metamorph.readers;

import junit.framework.Assert;

import org.junit.Test;

public final class ReaderFactoryTest {
	
	
	@Test
	public void test(){
		ReaderFactory readerFactory;
		
		System.getProperties().remove(AbstractReaderFactory.REGISTRY_PROPERTY_NAME);
		readerFactory = AbstractReaderFactory.newInstance();
		Assert.assertTrue("The default reader registry was not correctly loaded", readerFactory instanceof ReaderFactoryImpl);
		
		System.setProperty(AbstractReaderFactory.REGISTRY_PROPERTY_NAME, DummyReaderRegistry.class.getName());
		readerFactory = AbstractReaderFactory.newInstance();
		Assert.assertTrue("The reader registry defined in system properties was not correctly loaded",readerFactory instanceof DummyReaderRegistry);
	
	}
}
