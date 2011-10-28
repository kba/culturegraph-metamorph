package org.culturegraph.metamorph.readers;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.util.Util;



public abstract class AbstractReaderFactory implements ReaderFactory {
	
	public static final String REGISTRY_PROPERTY_NAME = "org.culturegraph.metamorph.readerregistry";
	
	protected AbstractReaderFactory(){
		//nothing to do
	}
	
	public static ReaderFactory newInstance() {

		final String className = System.getProperty(REGISTRY_PROPERTY_NAME);
	
		if (className != null) {
				final Object obj = Util.instantiateClass(className);
				if (obj instanceof AbstractReaderFactory) {
					return (ReaderFactory) obj;
				}
				throw new MetamorphException(className + " could not be instantiated as it is not a subclass of " + AbstractReaderFactory.class.getName());
		}
		return new ReaderFactoryImpl();
	}
}
