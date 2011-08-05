package org.culturegraph.metamorph.util;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.readers.RawRecordReader;
import org.springframework.util.Assert;

public class ReaderRegistry {
	
	public static final String MAB2 = "mab2";
	public static final String PICA = "pica";
	public static final String MARC21 = "marc21";
	
	private static final String FORMAT_NULL = "'format' must not be null";
	private static final String READER_NULL = "'reader' must not be null";
	private static final String READERS_NULL = "'readers' must not be null";
	
	private Map<String, ReaderFactory> factories = new HashMap<String, ReaderFactory>();
	
	public final RawRecordReader getReaderForFormat(final String format){
		Assert.notNull(format, FORMAT_NULL);
		final ReaderFactory factory = factories.get(format);
		if(factory==null){
			return null;
		}else{
			return factory.newReader();
		}
	}
	
	public final void addReaderFactory(final String format, final ReaderFactory factory){
		Assert.notNull(factory, READER_NULL);
		Assert.notNull(format, FORMAT_NULL);
		factories.put(format, factory);
	}
	
	public final void setReaderFactories(final Map<String, ReaderFactory> factories){
		Assert.notNull(factories, READERS_NULL);
		this.factories = factories;
	}
	
	public static ReaderRegistry newReaderRegistry(){
		//TODO choose ReaderRegistry based on Java Property
		return new DefaultReaderRegistry();
	}
	
}
