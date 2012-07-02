package org.culturegraph.metamorph.stream.readers;

import java.util.regex.Pattern;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Parses a raw Mab2 stream (utf-8 encoding assumed). Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @see StreamReceiver
 */
public final class MabReader extends AbstractReader {

	private static final String FIELD_END = "\u001e";
	private static final Pattern FIELD_PATTERN = Pattern.compile(FIELD_END, Pattern.LITERAL);
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile("\u001f", Pattern.LITERAL);
	private static final String RECORD_END = "\u001d";

	private static final int FIELD_NAME_SIZE = 4;
	private static final int HEADER_SIZE = 24;
	private static final String LEADER = "Leader";
	private static final String TYPE = "type";
	private static final String INVALID_FORMAT = "Invalid MAB format";
	private static final String ID_TAG = "001 ";
	private static final int TAG_LENGTH = 4;

	@Override
	protected void processRecord(final String record) {
		if (record.trim().isEmpty()) {
			return;
		}

		final StreamReceiver receiver = getStreamReceiver();
		receiver.startRecord(extractIdFromRecord(record));

		try {
			receiver.literal(LEADER, record.substring(0, HEADER_SIZE));
			receiver.literal(TYPE, String.valueOf(record.charAt(HEADER_SIZE - 1)));
			final String content = record.substring(HEADER_SIZE);
			for (String part : FIELD_PATTERN.split(content)) {
				if (!part.startsWith(RECORD_END)) {
					final String fieldName = part.substring(0, FIELD_NAME_SIZE).trim();
					final String fieldContent = part.substring(FIELD_NAME_SIZE);
					final String[] subFields = SUBFIELD_PATTERN.split(fieldContent);

					if (subFields.length == 1) {
						receiver.literal(fieldName, subFields[0]);
					} else {
						receiver.startEntity(fieldName);

						for (int i = 1; i < subFields.length; ++i) {
							final String name = subFields[i].substring(0, 1);
							final String value = subFields[i].substring(1);
							getStreamReceiver().literal(name, value);
						}
						receiver.endEntity();
					}
				}
			}
			receiver.endRecord();
		} catch (IndexOutOfBoundsException e) {
			throw new RecordFormatException("[" + record + "]", e);
		}

	}

	public static String extractIdFromRecord(final String record) {
		try {
			final int fieldEnd = record.indexOf(FIELD_END, HEADER_SIZE);
			if (record.substring(HEADER_SIZE, HEADER_SIZE + TAG_LENGTH).equals(ID_TAG)) {
				return record.substring(HEADER_SIZE + TAG_LENGTH, fieldEnd);
			}
			throw new MissingIdException(record);
		} catch (IndexOutOfBoundsException e) {
			throw new RecordFormatException(INVALID_FORMAT + record, e);
		}
	}

}
