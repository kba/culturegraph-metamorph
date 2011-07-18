package org.culturegraph.metamorph;

import java.io.FileInputStream;
import java.io.IOException;

import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.readers.MabReader;
import org.culturegraph.metamorph.readers.PicaReader;
import org.culturegraph.metamorph.readers.RawRecordReader;
import org.culturegraph.metamorph.streamreceiver.ConsoleWriter;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Morph {

	
	private Morph(){}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(final String[] args) throws  IOException{
		final String fileName = args[0];
        final int dotPos = fileName.lastIndexOf('.');
        if(dotPos < 0){
        	throw new MetamorphException("Extention missing");
        }else{
	        final String extension = fileName.substring(dotPos+1);
	        
	        final MetamorphBuilder transformerBuilder = new MetamorphBuilder();
	        final RawRecordReader reader;

	        if("pica".equalsIgnoreCase(extension)){
	        	reader = new PicaReader();
	        	transformerBuilder.setDefinitionFile("src/main/resources/pica.xml");
	        }else if("mab2".equalsIgnoreCase(extension)){
	        	reader = new MabReader();
	        	transformerBuilder.setDefinitionFile("src/main/resources/mab2.xml");
	        }else{
	        	throw new MetamorphException("Extention not recognized");
	        }

			transformerBuilder.setOutputHandler(new ConsoleWriter());
			reader.setStreamReceiver(transformerBuilder.build());
			reader.read(new FileInputStream(args[0]));

        }
	}
}
