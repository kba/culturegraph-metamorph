package org.culturegraph.metamorph.readers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

/**
 * Parses a raw Marc stream using Marc4j. Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @see StreamReceiver
 */
public final class MarcReader implements RawRecordReader {

	// private static final Logger LOG = LoggerFactory
	// .getLogger(MarcReader2.class);
	private StreamReceiver streamReceiver;

	@SuppressWarnings("unchecked")
	// marc4j is not type safe!
	protected void processRecord(final Record record) {
		getStreamReceiver().startRecord();

		getStreamReceiver().literal("type",
				String.valueOf(record.getLeader().getTypeOfRecord()));

		for (ControlField cField : (List<ControlField>) record
				.getControlFields()) {
			getStreamReceiver().literal(cField.getTag(), cField.getData());
		}
		for (DataField dataField : (List<DataField>) record.getDataFields()) {

			final String tag = dataField.getTag();
			final char ind1 = dataField.getIndicator1();
			final char ind2 = dataField.getIndicator2();
			final String tagName = tag + ind1 + ind2;
			final List<Subfield> subfields = ((DataField) dataField)
					.getSubfields();

			getStreamReceiver().startEntity(tagName);
			for (Subfield subfield : subfields) {
				final String value = subfield.getData();

				getStreamReceiver().literal(
						Character.toString(subfield.getCode()), value);

			}
			getStreamReceiver().endEntity();

		}
		getStreamReceiver().endRecord();
	}

	@Override
	public void read(final InputStream inputStream) throws IOException {
		final MarcStreamReader marcStreamReader = new MarcStreamReader(
				inputStream);
		while (marcStreamReader.hasNext()) {
			processRecord(marcStreamReader.next());
		}
	}

	@Override
	public void read(final String entry) {
		try {
			read(new ByteArrayInputStream(entry.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new MetamorphException("Encoding not found", e);
		} catch (IOException e) {
			throw new MetamorphException(e);
		}
	}

	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		this.streamReceiver = streamReceiver;
	}

	protected StreamReceiver getStreamReceiver() {
		return streamReceiver;
	}

	@Override
	public String getId(final String record) {
		throw new UnsupportedOperationException();
	}
}
