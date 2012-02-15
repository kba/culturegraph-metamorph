package org.culturegraph.metamorph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.culturegraph.metamorph.core.MetamorphVisualizer;

/**
 * Generates a graphviz dot file based on a Metamorph definition.
 * 
 * @author Markus Michael Geipel
 */
public final class Visualize {

	private static final String ENCODING = "UTF8";

	private Visualize() {/* no instances */
	}

	/**
	 * @param args
	 *            set args[0] to the recordfilename.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final Writer out;
		if(args.length==1){
			out = new OutputStreamWriter(System.out, ENCODING);
		}else if(args.length==2){
			System.out.println("Writing to " + args[1]);
			out = new  OutputStreamWriter(new FileOutputStream(new File(args[1])), ENCODING);
		}else{
			System.err.println("Usage: Visualize MORPHDEF [DOTFILE]");
			return;
		}
		final MetamorphVisualizer visualizer = new MetamorphVisualizer(out);
		visualizer.walk(new FileReader(args[0]));
	}

	
}
