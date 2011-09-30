package org.culturegraph.metamorph.readers;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;

import de.ddb.charset.MabCharset;

/**
 * Parses a raw Mab2 stream (mab encoding assumed). Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @status Productive
 * @see StreamReceiver
 */
public final class MabReader extends AbstractReader {

	private static final Pattern FIELD_PATTERN = Pattern.compile(String.valueOf(MabCharset.FELDENDEZEICHEN));
	private static final Pattern SUBFIELD_PATTERN = Pattern.compile(String.valueOf(MabCharset.UNTERFELDBEGINNZEICHEN));
	private static final String RECORD_END = Character.toString(MabCharset.SATZENDEZEICHEN);

	private static final int FIELD_NAME_SIZE = 4;
	private static final int HEADER_SIZE = 24;
	private static final String LEADER = "Leader";
	private static final String INVALID_FORMAT = "Invalid MAB format";
	private static final String ID_TAG = "001 ";
	private static final int TAG_LENGTH = 4;

	@Override
	protected void processRecord(final String record) {
		if (record.trim().isEmpty()) {
			return;
		}

		

		// final String header = record.substring(0, HEADER_SIZE);
		// if (LOG.isTraceEnabled()) {
		// LOG.trace("Länge des Datensatzes: " + header.substring(0, 4));
		// LOG.trace("Status: " + header.charAt(5));
		// LOG.trace("Version: " + header.substring(6, 10));
		// LOG.trace("Indikatorlänge: " + header.charAt(10));
		// LOG.trace("Teilfeldkennungslänge: " + header.charAt(11));
		// LOG.trace("Datenanfang: " + header.substring(12, 17));
		// LOG.trace("Typ: " + header.substring(23, 24));
		// }

		getStreamReceiver().startRecord();

		try {
			getStreamReceiver().literal(LEADER, record.substring(0, HEADER_SIZE));
			final String content = record.substring(HEADER_SIZE);
			for (String part : FIELD_PATTERN.split(content)) {
				if (!part.startsWith(RECORD_END)) {
					final String fieldName = part.substring(0, FIELD_NAME_SIZE).trim();
					final String fieldContent = part.substring(FIELD_NAME_SIZE);
					final String[] subFields = SUBFIELD_PATTERN.split(fieldContent);

					if (subFields.length == 1) {
						getStreamReceiver().literal(fieldName, subFields[0]);
					} else {
						getStreamReceiver().startEntity(fieldName);

						for (int i = 1; i < subFields.length; ++i) {
							final String name = subFields[i].substring(0, 1);
							final String value = subFields[i].substring(1);
							getStreamReceiver().literal(name, value);
						}
						getStreamReceiver().endEntity();
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			throw new RecordFormatException("[" + record + "]", e);
		} finally {
			getStreamReceiver().endRecord();
		}
	}

	@Override
	protected Charset getCharset() {
		return new MabCharset(false);
	}

	public static String extractIdFromRawRecord(final String record) {
		try{
			final int fieldEnd = record.indexOf(MabCharset.FELDENDEZEICHEN, HEADER_SIZE);
			if(record.substring(HEADER_SIZE, HEADER_SIZE + TAG_LENGTH).equals(ID_TAG)){
				return record.substring(HEADER_SIZE + TAG_LENGTH, fieldEnd);
			}
			throw new MissingIdException(record);
		} catch (IndexOutOfBoundsException e) {
			throw new RecordFormatException(INVALID_FORMAT + record, e);
		}
	}

	@Override
	public String getId(final String record) {
		return extractIdFromRawRecord(record);
	}
}
