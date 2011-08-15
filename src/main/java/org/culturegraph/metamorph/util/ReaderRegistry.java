package org.culturegraph.metamorph.util;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.readers.RawRecordReader;

public class ReaderRegistry {
	

	private static final String FORMAT_NULL = "'format' must not be null";
	private static final String FACTORY_NULL = "'factory' must not be null";
	private static final String FACTORIES_NULL = "'factories' must not be null";
	
	private Map<String, ReaderFactory> factories = new HashMap<String, ReaderFactory>();
	
	
	public final RawRecordReader getReaderForFormat(final String format){
		if(format==null){
			throw new IllegalArgumentException(FORMAT_NULL);
		}
		final ReaderFactory factory = factories.get(format);
		if(factory==null){
			return null;
		}else{
			return factory.newReader();
		}
	}
	
	public final boolean isFormatSupported(final String format){
		return factories.get(format) != null;
	}
	
	public final void addReaderFactory(final String format, final ReaderFactory factory){
		if(format==null){
			throw new IllegalArgumentException(FORMAT_NULL);
		}
		if(factory==null){
			throw new IllegalArgumentException(FACTORY_NULL);
		}
	
		factories.put(format, factory);
	}
	
	public final void setReaderFactories(final Map<String, ReaderFactory> factories){
		if(factories==null){
			throw new IllegalArgumentException(FACTORIES_NULL);
		}
		this.factories = factories;
	}
	
//	public static ReaderRegistry newReaderRegistry(){
//		//TODO choose ReaderRegistry based on Java Property
//		return new DefaultReaderRegistry();
//	}
}
