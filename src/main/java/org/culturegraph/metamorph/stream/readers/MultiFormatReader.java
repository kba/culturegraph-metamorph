package org.culturegraph.metamorph.stream.readers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.DefaultErrorHandler;
import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.core.MetamorphErrorHandler;
import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * {@link MultiFormatReader} uses the {@link AbstractReaderFactory} to handle
 * all registered input formats.
 * @deprecated no longer supported
 * 
 * @author Markus Michael Geipel
 * 
 */
@Deprecated
public final class MultiFormatReader implements Reader, MetamorphErrorHandler {

	private static final String ERROR_NO_FORMAT = "no format set";
	private static final String ERROR_RECEIVER_NULL = "'streamReceiver' must not be null";
	private Reader currentReader;
	private final Map<String, Reader> openReaders = new HashMap<String, Reader>();
	private final Map<String, Metamorph> metamorphs = new HashMap<String, Metamorph>();
	private final ReaderFactory readerFactory = AbstractReaderFactory.newInstance();
	private StreamReceiver streamReceiver;
	private final String morphDefinition;
	private MetamorphErrorHandler errorHandler = new DefaultErrorHandler();
	private String currentFormat;

	public MultiFormatReader(final String morphDefinition) {
		this.morphDefinition = morphDefinition;
	}

	public MultiFormatReader() {
		morphDefinition = null;
	}

	public Metamorph getMetamorph() {
		return metamorphs.get(currentFormat);
	}

	public void setErrorHandler(final MetamorphErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public String getFormat() {
		return currentFormat;
	}

	public void setFormat(final String format) {
		if (format == null) {
			throw new IllegalArgumentException("'format' must not be null");
		}

		currentReader = openReaders.get(format);
		currentFormat = format;

		if (null != currentReader) {
			return;
		}
		currentReader = readerFactory.newReader(format);
		openReaders.put(format, currentReader);

		if (morphDefinition == null && streamReceiver != null) {
			currentReader.setReceiver(streamReceiver);
		} else if (morphDefinition != null) {
			final String morphDefinitionFinal = morphDefinition + '.' + format + ".xml";
			final Metamorph metamorph = MetamorphBuilder.build(morphDefinitionFinal);
			metamorphs.put(format, metamorph);
			metamorph.setErrorHandler(this);
			currentReader.setReceiver(metamorph);
			if (streamReceiver != null) {
				metamorph.setReceiver(streamReceiver);
			}
		}
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		if (streamReceiver == null) {
			throw new IllegalArgumentException(ERROR_RECEIVER_NULL);
		}

		this.streamReceiver = streamReceiver;
		if (morphDefinition == null) {
			for (Reader reader : openReaders.values()) {
				reader.setReceiver(streamReceiver);
			}
		} else {
			for (Metamorph metamorph : metamorphs.values()) {
				metamorph.setReceiver(streamReceiver);
			}
		}

		return streamReceiver;
	}

	@Override
	public void read(final String entry) {
		if (streamReceiver == null) {
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}
		try {
			currentReader.read(entry);
		} catch (MetamorphException e) {
			errorHandler.error(e);
		}
	}


	@Override
	public void error(final Exception exception) {
		errorHandler.error(exception);
	}

	@Override
	public void read(final java.io.Reader reader) throws IOException {
		currentReader.read(reader);

	}
}
