package org.culturegraph.metamorph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.readers.RawRecordReader;
import org.culturegraph.metamorph.streamreceiver.ConsoleWriter;
import org.culturegraph.metamorph.util.DefaultReaderRegistry;
import org.culturegraph.metamorph.util.ReaderRegistry;


/**
 * Example which can read mab2, pica and marc21 files, applies the {@link Metamorph}
 * defined in FORMAT.xml and prints the result to the console using a {@link ConsoleWriter}.
 * 
 * @author Markus Michael Geipel
 */
public final class Morph {

	private Morph(){}
	/**
	 * @param args set args[0] to the recordfilename.
	 * @throws IOException 
	 */
	public static void main(final String[] args) throws  IOException{
		final ReaderRegistry readerRegistry = new DefaultReaderRegistry();
		
		final String fileName = args[0];
        final int dotPos = fileName.lastIndexOf('.');
        if(dotPos < 0){
        	throw new MetamorphException("Extention missing");
        }else{
	        final String extension = fileName.substring(dotPos+1);
	        final RawRecordReader reader = readerRegistry.getReaderForFormat(extension);

	        if(reader==null){
	        	throw new MetamorphException("Extention not recognized");
	        }
	
	        final File definition = new File("src/main/resources/"+extension+".xml");
			reader.setStreamReceiver(MetamorphBuilder.build(definition, new ConsoleWriter()));
			reader.read(new FileInputStream(args[0]));
        }
	}
}
