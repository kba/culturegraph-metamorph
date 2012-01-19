package org.culturegraph.metamorph.stream.readers;

import java.util.regex.Pattern;

import org.culturegraph.metamorph.stream.CGEntity;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CGEntityReader extends AbstractReader {

	private static final Pattern FIELD_PATTERN = Pattern.compile(String.valueOf(CGEntity.FIELD_DELIMITER),
			Pattern.LITERAL);
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile(String.valueOf(CGEntity.SUB_DELIMITER),
			Pattern.LITERAL);
	
	private static Logger LOG = LoggerFactory.getLogger(CGEntityReader.class);

	@Override
	public String getId(final String record) {
		final int cut = record.indexOf(CGEntity.FIELD_DELIMITER);
		return record.substring(0, cut);
	}


	@Override
	protected void processRecord(final String record) {
		read(record, getStreamReceiver());
	}

	public static void read(final String record, final StreamReceiver receiver) {
		try {			
			final String[] fields = FIELD_PATTERN.split(record);
			receiver.startRecord(fields[0]);
			for (int i = 1; i < fields.length; ++i) {
				final char firstChar = fields[i].charAt(0);
				if (firstChar == CGEntity.LITERAL_MARKER) {
					final String[] parts = SUBFIELD_PATTERN.split(fields[i], -1);
					receiver.literal(parts[0].substring(1), parts[1].replace(CGEntity.NEWLINE_ESC, CGEntity.NEWLINE));
				} else if (firstChar == CGEntity.ENTITY_START_MARKER) {
					receiver.startEntity(fields[i].substring(1));
				} else if (firstChar == CGEntity.ENTITY_END_MARKER) {
					receiver.endEntity();
				} else if(firstChar == CGEntity.NEWLINE){
					LOG.debug("unexpected newline");
				}else{
					throw new RecordFormatException(record);
				}
			}
			receiver.endRecord();
		} catch (IndexOutOfBoundsException exception) {
			throw new RecordFormatException(record, exception);
		}
	}
}