package org.culturegraph.metamorph.stream.readers;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Parses a raw Picaplus stream (utf8 encoding assumed).
 * Events are handled by a {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @status Productive
 * @see StreamReceiver
 */
public final class PicaReader extends AbstractReader{

	private static final String FIELD_DELIMITER = "\u001e";
	private static final String SUB_DELIMITER = "\u001f";
	private static final Pattern FIELD_PATTERN = Pattern.compile(FIELD_DELIMITER, Pattern.LITERAL);
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile(SUB_DELIMITER, Pattern.LITERAL);
	private static final String ID_PATTERN_STRING = FIELD_DELIMITER + "003@ " + SUB_DELIMITER + "0(.*?)" + FIELD_DELIMITER;
	private static final Pattern ID_PATTERN = Pattern.compile(ID_PATTERN_STRING);


	@Override
	protected void processRecord(final String record) {
		read(record, getStreamReceiver());
	}
	
	

	@Override
	protected Charset getCharset() {
		return Charset.forName("UTF8");
	}
	
	public static void read(final String record, final StreamReceiver receiver){
		receiver.startRecord(extractIdFromRecord(record));
		
		try{
			for (String field : FIELD_PATTERN.split(record)) {
				
				final String[] subfields = SUBFIELD_PATTERN.split(field);
				
				final String fieldName;
				final int firstSubfield;
				if(subfields[1].charAt(0)=='S'){
					fieldName = subfields[0].trim() + subfields[1].charAt(1);
					firstSubfield = 2;
				}else{
					fieldName = subfields[0].trim();
					firstSubfield = 1;
				}
				
				receiver.startEntity(fieldName);
	
				for (int i = firstSubfield; i < subfields.length; ++i) {
					final String subfield = subfields[i];
					receiver.literal(subfield.substring(0, 1), subfield.substring(1));
				}
				receiver.endEntity();
				
			}
		}catch(IndexOutOfBoundsException exception){
			throw new RecordFormatException("[" + record + "]", exception);
		}
		receiver.endRecord();
	}
	
	
	public static String extractIdFromRecord(final String record) {
		// TODO tune!
		final Matcher idMatcher = ID_PATTERN.matcher(record);
		if(idMatcher.find()){
			return idMatcher.group(1);
		}
		throw new MissingIdException(record);
	}

	@Override
	public String getId(final String record) {
		return extractIdFromRecord(record);
	}
}
