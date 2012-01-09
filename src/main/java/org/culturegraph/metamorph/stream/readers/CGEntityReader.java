package org.culturegraph.metamorph.stream.readers;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.stream.CGEntity;
import org.culturegraph.metamorph.stream.StreamReceiver;

public final class CGEntityReader extends AbstractReader {

	private static final Pattern FIELD_PATTERN = Pattern.compile(String.valueOf(CGEntity.FIELD_DELIMITER),
			Pattern.LITERAL);
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile(String.valueOf(CGEntity.SUB_DELIMITER),
			Pattern.LITERAL);

	@Override
	public String getId(final String record) {
		final int cut = record.indexOf(CGEntity.FIELD_DELIMITER);
		return record.substring(0, cut);
	}

	@Override
	protected Charset getCharset() {
		return Charset.forName("UTF-8");
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

				if (fields[i].charAt(0) == CGEntity.LITERAL_MARKER) {
					final String[] parts = SUBFIELD_PATTERN.split(fields[i]);
					if(parts.length == 1){
						receiver.literal(parts[0].substring(1), "");
					}else {
						receiver.literal(parts[0].substring(1), parts[1].replace(CGEntity.NEWLINE_ESC, CGEntity.NEWLINE));
					}
				} else if (fields[i].charAt(0) == CGEntity.ENTITY_START_MARKER) {
					receiver.startEntity(fields[i].substring(1));
				} else if (fields[i].charAt(0) == CGEntity.ENTITY_END_MARKER) {
					receiver.endEntity();
				} else {
					throw new RecordFormatException(record);
				}
			}
			receiver.endRecord();
		} catch (IndexOutOfBoundsException exception) {
			throw new RecordFormatException(record, exception);
		}
	}
}