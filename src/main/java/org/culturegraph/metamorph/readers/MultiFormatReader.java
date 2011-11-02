package org.culturegraph.metamorph.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.culturegraph.metamorph.core.DefaultErrorHandler;
import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.core.MetamorphErrorHandler;
import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * {@link MultiFormatReader} uses the {@link AbstractReaderFactory} to handle
 * all registered input formats.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class MultiFormatReader implements Reader, MetamorphErrorHandler {

	private static final String ERROR_NO_FORMAT = "no format set";
	private static final String ERROR_RECEIVER_NULL = "'streamReceiver' must not be null";
	private Reader currentReader;
	private final Map<String, Reader> openReaders = new HashMap<String, Reader>();
	private final List<Metamorph> metamorphs = new ArrayList<Metamorph>();
	private final ReaderFactory readerFactory = AbstractReaderFactory.newInstance();
	private StreamReceiver streamReceiver;
	private final String morphDefinition;
	private MetamorphErrorHandler errorHandler = new DefaultErrorHandler();

	public MultiFormatReader(final String morphDefinition) {
		this.morphDefinition = morphDefinition;
	}

	public MultiFormatReader() {
		morphDefinition = null;
	}

	public void setErrorHandler(final MetamorphErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void setFormat(final String format) {
		currentReader = openReaders.get(format);
		if (null == currentReader) {
			currentReader = readerFactory.newReader(format);
			openReaders.put(format, currentReader);

			if (morphDefinition == null) {
				currentReader.setStreamReceiver(streamReceiver);
			} else {
				final String morphDefinitionFinal = morphDefinition + '.' + format + ".xml";
				final InputStream inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(morphDefinitionFinal);
				if (inputStream == null) {
					throw new MetamorphException(morphDefinitionFinal + " not found");
				}
				final Metamorph metamorph = MetamorphBuilder.build(inputStream);
				metamorphs.add(metamorph);
				currentReader.setStreamReceiver(metamorph);
				metamorph.setStreamReceiver(streamReceiver);
				metamorph.setErrorHandler(this);
			}
		}
	}

	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		if (streamReceiver == null) {
			throw new IllegalArgumentException(ERROR_RECEIVER_NULL);
		}
		this.streamReceiver = streamReceiver;
		if (morphDefinition == null) {
			for (Reader reader : openReaders.values()) {
				reader.setStreamReceiver(streamReceiver);
			}
		} else {
			for (Metamorph metamorph : metamorphs) {
				metamorph.setStreamReceiver(streamReceiver);
			}
		}
	}

	@Override
	public void read(final InputStream inputStream) throws IOException {
		if (streamReceiver == null) {
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}

		try {
			currentReader.read(inputStream);
		} catch (MetamorphException e) {
			errorHandler.error(e);
		}
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
	public String getId(final String record) {
		if (streamReceiver == null) {
			throw new IllegalStateException(ERROR_NO_FORMAT);
		}

		return currentReader.getId(record);

	}

	@Override
	public void error(final Exception exception) {
		errorHandler.error(exception);
	}
}
