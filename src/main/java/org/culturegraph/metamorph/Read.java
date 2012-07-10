package org.culturegraph.metamorph;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.reader.MultiFormatReader;
import org.culturegraph.metamorph.reader.Reader;
import org.culturegraph.metamorph.reader.ReaderFactory;
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
		if(args.length < 1 || args.length>2){
			System.err.println("Usage: Read FILE [MORPHDEF]");
			return;
		}
		
		final String fileName = args[0];
		final Reader reader = new MultiFormatReader(getExtention(fileName));
		final StreamWriter consoleWriter = new StreamWriter(new OutputStreamWriter(System.out, "UTF8"));

		if (args.length == 2) {
			final Metamorph metamorph = MetamorphBuilder.build(args[1]);
			reader.setReceiver(metamorph).setReceiver(consoleWriter);
		}else{
			reader.setReceiver(consoleWriter);
		}

		reader.process(new FileReader(fileName));
		reader.closeStream();
	}

	private static String getExtention(final String fileName) {
		final int dotPos = fileName.lastIndexOf('.');
		if (dotPos < 0) {
			throw new IllegalArgumentException("Extention missing");
		}
		return fileName.substring(dotPos + 1);
	}
}
