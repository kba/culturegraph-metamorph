package org.culturegraph.metamorph.readers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core.MetamorphException;


/**
 * {@link AbstractReaderFactory} with pica, mab2 and marc21 {@link Reader}s already registered.
 * 
 * @author Markus Michael Geipel
 * 
 */
final class ReaderFactoryImpl extends AbstractReaderFactory {

	private static final String INSTANTIATION_PROBLEM = " could not be instantiated";
	
	private final Map<String, Class<? extends Reader>> readerClasses = new HashMap<String, Class<? extends Reader>>();
	
	public ReaderFactoryImpl() {
		super();
		readerClasses.put(Format.MAB2, MabReader.class);
		readerClasses.put(Format.MARC21, MarcReader.class);
		readerClasses.put(Format.PICA, PicaReader.class);
	}

	@Override
	public Reader newReader(final String format) {
		if(!isFormatSupported(format)){
			throw new IllegalArgumentException("format " + format + " is not supported");
		}
		final Class<? extends Reader> clazz = readerClasses.get(format);
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		} catch (IllegalAccessException e) {
			throw new MetamorphException(clazz + INSTANTIATION_PROBLEM, e);
		}
	}

	@Override
	public boolean isFormatSupported(final String format) {
		return readerClasses.containsKey(format);
	}

	@Override
	public Set<String> getSupportedFormats() {
		return readerClasses.keySet();
	}


}
