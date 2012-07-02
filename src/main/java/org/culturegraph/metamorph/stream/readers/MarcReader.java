package org.culturegraph.metamorph.stream.readers;

import java.util.regex.Pattern;

import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * Parses a raw Marc stream. UTF-8 encoding expected. Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @see StreamReceiver
 */
public final class MarcReader extends AbstractReader {

	private static final String FIELD_DELIMITER = "\u001e";
	private static final String SUB_DELIMITER = "\u001f";
	private static final Pattern FIELD_PATTERN = Pattern.compile(FIELD_DELIMITER, Pattern.LITERAL);
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile(SUB_DELIMITER, Pattern.LITERAL);
	private static final int POS_ENCODING = 9;
	//private static final int POS_LEADER6 = 6;
	//private static final int POS_LEADER7 = 7;
	//private static final int POS_LEADER19 = 19;
	private static final int POS_DIRECTORY = 24;
	private static final int DIRECTORY_ENTRY_WIDTH = 12;
	//private static final String LEADER6 = "leader6";
	//private static final String LEADER7 = "leader7";
	private static final int TAG_LENGTH = 3;
	private static final int DATA_START_BEGIN = 12;
	private static final int DATA_START_END = 17;
	//private static final String LEADER19 = "leader19";
	
	private static final Object ID_TAG = "001";
	private static final String LEADER = "leader";
	private static final int LEADER_END = 24;

	@Override
	protected void processRecord(final String record) {
		final StreamReceiver receiver = getStreamReceiver();
		try {
			if (record.charAt(POS_ENCODING) != 'a') {
				throw new IllegalEncodingException("UTF-8 encoding expected");
			}
			receiver.startRecord(extractIdFromRecord(record));
			receiver.literal(LEADER, record.substring(0, LEADER_END));
			//ceiver.literal(LEADER6, String.valueOf(record.charAt(POS_LEADER6)));
			///receiver.literal(LEADER7, String.valueOf(record.charAt(POS_LEADER7)));
			//receiver.literal(LEADER19, String.valueOf(record.charAt(POS_LEADER19)));

			final int dataStart = Integer.parseInt(record.substring(DATA_START_BEGIN, DATA_START_END));
			final String directory = record.substring(POS_DIRECTORY, dataStart);
			final int numDirEntries = directory.length() / DIRECTORY_ENTRY_WIDTH;
			final String[] fields = FIELD_PATTERN.split(record);

			for (int i = 0; i < numDirEntries; i += 1) {
				final int base = i * 12;
				final String[] subFields = SUBFIELD_PATTERN.split(fields[i + 1]);
				final String tag = directory.substring(base, base + TAG_LENGTH);
				if (tag.charAt(1) == '0' && tag.charAt(0) == '0') {
					receiver.literal(tag, subFields[0]);
				} else {
					receiver.startEntity(tag + subFields[0]);
					for (int j = 1; j < subFields.length; ++j) {
						final String subField = subFields[j];
						receiver.literal(String.valueOf(subField.charAt(0)), subField.substring(1));
					}
					receiver.endEntity();
				}
			}
			receiver.endRecord();
		} catch (IndexOutOfBoundsException exception) {
			throw new RecordFormatException(record, exception);
		}
			
	
	}

	public static String extractIdFromRecord(final String record) {
		try {
			if (record.substring(POS_DIRECTORY, POS_DIRECTORY + TAG_LENGTH).equals(ID_TAG)) {
				final int start = record.indexOf(FIELD_DELIMITER) + 1;
				final int end = record.indexOf(FIELD_DELIMITER, start);
				return record.substring(start, end);
			}
			throw new MissingIdException(record);

		} catch (IndexOutOfBoundsException exception) {
			throw new RecordFormatException(record, exception);
		}
	}


}
