package org.culturegraph.metamorph.rdf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.culturegraph.metamorph.readers.MultiFormatReader;
import org.culturegraph.metamorph.stream.ConsoleWriter;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Example which reads mab2, pica and marc21 files and prints the result to the
 * console using a {@link ConsoleWriter}.
 * 
 * @author Markus Michael Geipel
 */
public final class RdfMorph {

	private static final String RDF_XML_ABR = "RDF/XML-ABBREV";

	private RdfMorph() {/* no instances */
	}

	/**
	 * @param args
	 *            set args[0] to the recordfilename.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final MultiFormatReader reader;
		if (args.length == 2) {
			reader = new MultiFormatReader(args[1]);
		} else {
			System.err.println("Usage: RdfMorph FILE MORPHDEF");
			return;
		}

		final JenaWriter jenaWriter = new JenaWriter();
		final Writer out = new OutputStreamWriter(System.out, "UTF8");
		jenaWriter.setBatchSize(1);
		jenaWriter.setBatchFinishedListener(new JenaWriter.BatchFinishedListener() {
			@Override
			public void onBatchFinished(final Model model) {
				model.write(out, RDF_XML_ABR);
			}
		});
		reader.setStreamReceiver(jenaWriter);

		final String fileName = args[0];
		final String extension = getExtention(fileName);
		reader.setFormat(extension);

		final Map<String, String> namespaces = reader.getMetamorph().getMap("namespaces");
		if (namespaces == null) {
			System.err.println("no namespaces defined");
		} else {
			jenaWriter.setNsPrefixes(namespaces);
		}

		reader.read(new FileInputStream(fileName));
		jenaWriter.getModel().write(out, RDF_XML_ABR);
	}

	private static String getExtention(final String fileName) {
		final int dotPos = fileName.lastIndexOf('.');
		if (dotPos < 0) {
			throw new IllegalArgumentException("Extention missing");
		}
		return fileName.substring(dotPos + 1);
	}
}
