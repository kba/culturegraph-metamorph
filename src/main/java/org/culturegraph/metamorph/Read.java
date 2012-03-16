package org.culturegraph.metamorph;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.culturegraph.metamorph.stream.readers.MultiFormatReader;
import org.culturegraph.metastream.sink.StreamWriter;

/**
 * Example which reads mab2, pica and marc21 files and prints the result to the
 * console using a {@link DefaultWriter}.
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
		final StreamWriter consoleWriter = new StreamWriter();
		reader.setReceiver(consoleWriter);
		consoleWriter.setReceiver(new OutputStreamWriter(System.out, "UTF8"));

		final String fileName = args[0];
		final String extension = getExtention(fileName);
		reader.setFormat(extension);
		reader.read(new FileReader(fileName));
		reader.close();
	}

	private static String getExtention(final String fileName) {
		final int dotPos = fileName.lastIndexOf('.');
		if (dotPos < 0) {
			throw new IllegalArgumentException("Extention missing");
		}
		return fileName.substring(dotPos + 1);
	}
}
