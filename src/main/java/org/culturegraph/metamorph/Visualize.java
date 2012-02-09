package org.culturegraph.metamorph;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.culturegraph.metamorph.core.MetamorphVisualizer;

/**
 * Generates a graphviz dot file based on a Metamorph definition.
 * 
 * @author Markus Michael Geipel
 */
public final class Visualize {

	private Visualize() {/* no instances */
	}

	/**
	 * @param args
	 *            set args[0] to the recordfilename.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		if (args.length != 1) {
	
			System.err.println("Usage: Visualize MORPHDEF");
			return;
		}
		final MetamorphVisualizer visualizer = new MetamorphVisualizer(new OutputStreamWriter(System.out, "UTF8"));
		visualizer.walk(new FileReader(args[0]));
	}

	
}
