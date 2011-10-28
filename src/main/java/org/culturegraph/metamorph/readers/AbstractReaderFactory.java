package org.culturegraph.metamorph.readers;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.util.Util;



public abstract class AbstractReaderFactory implements ReaderFactoryTemp {
	
	private static final String REGISTRY_PROPERTY_NAME = "org.culturegraph.metamorph.readerregistry";
	
	public static ReaderFactoryTemp newInstance() {

		final String className = System.getProperty(REGISTRY_PROPERTY_NAME);
	
		if (className != null) {
				final Object obj = Util.instantiateClass(className);
				if (obj instanceof AbstractReaderFactory) {
					return (ReaderFactoryTemp) obj;
				}
				throw new MetamorphException(className + " could not be instantiated as it is not a subclass of " + AbstractReaderFactory.class.getName());
		}
		return new ReaderFactoryImpl();
	}
}
