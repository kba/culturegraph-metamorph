package org.culturegraph.metamorph;

import java.io.FileInputStream;
import java.io.IOException;

import org.culturegraph.metamorph.readers.MultiFormatReader;
import org.culturegraph.metamorph.stream.ConsoleWriter;

/**
 * Example which reads mab2, pica and marc21 files and prints the result to the
 * console using a {@link ConsoleWriter}.
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
		final MultiFormatReader reader;
		if (args.length == 1) {
			reader = new MultiFormatReader();
		} else if (args.length == 2) {
			reader = new MultiFormatReader(args[1]);
		} else {
			System.err.println("Usage: Read FILE [MORPHDEF]");
			return;
		}

		reader.setStreamReceiver(new ConsoleWriter());

		final String fileName = args[0];
		final String extension = getExtention(fileName);
		reader.setFormat(extension);
		reader.read(new FileInputStream(fileName));
	}

	private static String getExtention(final String fileName) {
		final int dotPos = fileName.lastIndexOf('.');
		if (dotPos < 0) {
			throw new IllegalArgumentException("Extention missing");
		}
		return fileName.substring(dotPos + 1);
	}
}
