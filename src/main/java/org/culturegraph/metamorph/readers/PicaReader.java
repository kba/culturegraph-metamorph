package org.culturegraph.metamorph.readers;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;

/**
 * Parses a raw Picaplus stream (utf8 encoding assumed).
 * Events are handled by a {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @status Productive
 * @see StreamReceiver
 */
public final class PicaReader extends AbstractReader{

	private static final Pattern FIELD_PATTERN = Pattern.compile("\u001e");
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile("\u001f");

	@Override
	protected void processRecord(final String record) {
		getStreamReceiver().startRecord();
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
			
			getStreamReceiver().startEntity(fieldName);

			for (int i = firstSubfield; i < subfields.length; ++i) {
				final String subfield = subfields[i];
				getStreamReceiver().literal(subfield.substring(0, 1), subfield.substring(1));
			}
			getStreamReceiver().endEntity();
			
		}
		getStreamReceiver().endRecord();
	}

	@Override
	protected Charset getCharset() {
		return Charset.forName("UTF8");
	}

}
