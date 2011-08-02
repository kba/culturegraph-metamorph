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
	
	private Map<String, RawRecordReader> readers = new HashMap<String, RawRecordReader>();
	
	public final RawRecordReader getReaderForFormat(final String format){
		Assert.notNull(format, FORMAT_NULL);
		return readers.get(format);
	}
	
	public final void addReader(final String format, final RawRecordReader reader){
		Assert.notNull(reader, READER_NULL);
		Assert.notNull(format, FORMAT_NULL);
		readers.put(format, reader);
	}
	
	public final void setReaders(final Map<String, RawRecordReader> readers){
		Assert.notNull(readers, READERS_NULL);
		this.readers = readers;
	}
	
}
