package org.culturegraph.metamorph.readers;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.streamreceiver.StreamReceiver;

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
	private static final Pattern FIELD_PATTERN = Pattern.compile(FIELD_DELIMITER);
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile(SUB_DELIMITER);
	private static final int POS_ENCODING = 9;
	private static final int POS_TYPE = 6;
	private static final int POS_DIRECTORY = 24;
	private static final int DIRECTORY_ENTRY_WIDTH = 12;
	private static final String TYPE = "type";
	private static final int TAG_LENGTH = 3;
	private static final int DATA_START_BEGIN = 12;
	private static final int DATA_START_END = 17;
	private static final String MULTIPART = "multipart";
	private static final int POS_MULTIPART = 19;
	private static final Object ID_TAG = "001";

	@Override
	protected void processRecord(final String record) {

		if (record.charAt(POS_ENCODING) != 'a') {
			throw new MetamorphException("UTF-8 encoding expected");
		}
		final StreamReceiver receiver = getStreamReceiver();
		try {
			receiver.startRecord();
			receiver.literal(TYPE, String.valueOf(record.charAt(POS_TYPE)));
			receiver.literal(MULTIPART, String.valueOf(record.charAt(POS_MULTIPART)));

			final int dataStart = Integer.parseInt(record.substring(DATA_START_BEGIN, DATA_START_END));
			final String directory = record.substring(POS_DIRECTORY, dataStart);
			final int numDirEntries = directory.length() / DIRECTORY_ENTRY_WIDTH;
			final String[] fields = FIELD_PATTERN.split(record);
			boolean isDataField = false;

			for (int i = 0; i < numDirEntries; i += 1) {
				final int base = i * 12;
				final String[] subFields = SUBFIELD_PATTERN.split(fields[i + 1]);
				final String tag = directory.substring(base, base + TAG_LENGTH);
				if (isDataField) {
					receiver.startEntity(tag + subFields[0]);
					for (int j = 1; j < subFields.length; ++j) {
						final String subField = subFields[j];
						receiver.literal(String.valueOf(subField.charAt(0)), subField.substring(1));
					}
					receiver.endEntity();
				} else {
					receiver.literal(tag, subFields[0]);
					isDataField = tag.charAt(1) != '0';
				}
			}
		} catch (IndexOutOfBoundsException exception) {
			throw new RecordFormatException("[" + record + "]", exception);
		} finally {
			receiver.endRecord();
		}
	}

	public static String extractIdFromRawRecord(final String record) {
		if (record.substring(POS_DIRECTORY, POS_DIRECTORY + TAG_LENGTH).equals(ID_TAG)) {
			final int start = record.indexOf(FIELD_DELIMITER) + 1;
			final int end = record.indexOf(FIELD_DELIMITER, start);
			return record.substring(start, end);
		}
		return null;
	}

	@Override
	public String getId(final String record) {
		return extractIdFromRawRecord(record);
	}

	@Override
	protected Charset getCharset() {
		return Charset.forName("UTF8");
	}
}
