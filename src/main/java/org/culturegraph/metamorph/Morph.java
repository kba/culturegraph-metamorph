package org.culturegraph.metamorph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.core.MetamorphErrorHandler;
import org.culturegraph.metamorph.readers.DefaultReaderRegistry;
import org.culturegraph.metamorph.readers.RawRecordReader;
import org.culturegraph.metamorph.readers.ReaderRegistry;
import org.culturegraph.metamorph.streamreceiver.ConsoleWriter;

/**
 * Example which can read mab2, pica and marc21 files, applies the
 * {@link Metamorph} defined in FORMAT.xml and prints the result to the console
 * using a {@link ConsoleWriter}.
 * 
 * @author Markus Michael Geipel
 */
public final class Morph {

	private Morph() {/* no instances */
	}

	/**
	 * @param args
	 *            set args[0] to the recordfilename.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		if (args.length < 1 || args.length > 2) {
			System.err.println("Usage: Morph FILE [MORPH_DEFINITON]");
			return;
		}
		// System.setProperty("log4j.configuration", "MorphLog.xml");

		final ReaderRegistry readerRegistry = new DefaultReaderRegistry();

		final String fileName = args[0];
		final int dotPos = fileName.lastIndexOf('.');
		if (dotPos < 0) {
			System.err.println("Extention missing");
		} else {
			final String extension = fileName.substring(dotPos + 1);
			final RawRecordReader reader = readerRegistry.getReaderForFormat(extension);

			if (reader == null) {
				System.err.println("Extention not recognized");
				return;
			}

			final InputStream morphDefinition;

			if (args.length == 2) {
				morphDefinition = new FileInputStream(args[1]);
			} else {
				morphDefinition = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(extension + ".xml");

			}
			final Metamorph metamorph = MetamorphBuilder.build(reader,morphDefinition, new ConsoleWriter());

			metamorph.setErrorHandler(new MetamorphErrorHandler() {
				@Override
				public void error(final Exception exception) {
					System.err.println(extension);
				}
			});
	
			reader.read(new FileInputStream(fileName));
		}
	}
}
