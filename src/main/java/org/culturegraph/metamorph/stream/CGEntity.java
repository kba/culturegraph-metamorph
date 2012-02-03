package org.culturegraph.metamorph.stream;

import org.culturegraph.metamorph.stream.readers.CGEntityReader;
import org.culturegraph.metamorph.stream.receivers.CGEntityWriter;



/**
 * Constants used by the CGEntity file format
 * 
 * @author Markus Michael Geipel
 *
 * @see CGEntityReader
 * @see CGEntityWriter
 */
public final class CGEntity {
	public static final char FIELD_DELIMITER = '\u001e';
	public static final char SUB_DELIMITER = '\u001f';
	public static final char NEWLINE_ESC = '\u001d';
	public static final char NEWLINE = '\n';
	public static final char LITERAL_MARKER = '-';
	public static final char ENTITY_START_MARKER = '<';
	public  static final char ENTITY_END_MARKER = '>';
	
	private CGEntity() {
		// no instances
	}
}
