package org.culturegraph.metamorph;

import java.io.FileInputStream;
import java.io.IOException;

import org.culturegraph.metamorph.readers.AbstractReaderFactory;
import org.culturegraph.metamorph.readers.Reader;
import org.culturegraph.metamorph.readers.ReaderFactory;
import org.culturegraph.metamorph.stream.ConsoleWriter;

/**
 * Example which reads mab2, pica and marc21 files and prints the result to the console
 * using a {@link ConsoleWriter}.
 * 
 * @author Markus Michael Geipel
 */
public final class Read {

	private Read() {/* no instances */
	}

	/**
	 * @param args
	 *            set args[0] to the recordfilename.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		if (args.length !=1) {
			System.err.println("Usage: Read FILE");
			return;
		}

		final ReaderFactory readerRegistry = AbstractReaderFactory.newInstance();

		final String fileName = args[0];
		final int dotPos = fileName.lastIndexOf('.');
		if (dotPos < 0) {
			System.err.println("Extention missing");
		} else {
			final String extension = fileName.substring(dotPos + 1);
			final Reader reader = readerRegistry.newReader(extension);

			if (reader == null) {
				System.err.println("Extention not recognized");
				return;
			}
			reader.setStreamReceiver(new ConsoleWriter());
			reader.read(new FileInputStream(fileName));
		}
	}

}
