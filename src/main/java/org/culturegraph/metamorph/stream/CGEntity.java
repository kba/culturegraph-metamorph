package org.culturegraph.metamorph.stream;



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
