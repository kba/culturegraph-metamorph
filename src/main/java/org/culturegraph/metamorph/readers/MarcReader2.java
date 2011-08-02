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
import org.marc4j.marc.VariableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a raw Marc stream using Marc4j. Events are handled by a
 * {@link StreamReceiver}.
 * 
 * @author "Markus Michael Geipel"
 * @see StreamReceiver
 */
public final class MarcReader2 implements RawRecordReader {

	private static final Logger LOG = LoggerFactory
			.getLogger(MarcReader2.class);
	private StreamReceiver streamReceiver;

	@SuppressWarnings("unchecked")
	// marc4j is not type safe!
	protected void processRecord(final Record record) {
		getStreamReceiver().startRecord();

		LOG.debug("Type of record: " + record.getLeader().getTypeOfRecord()); // send
																				// as
																				// literal

		for (ControlField cField : (List<ControlField>) record
				.getControlFields()) {
			getStreamReceiver().literal(cField.getTag(), cField.getData());
		}

		for (VariableField vField : (List<VariableField>) record
				.getVariableFields()) {
			if (vField instanceof DataField) {
				final String tag = vField.getTag();
				final char ind1 = ((DataField) vField).getIndicator1();
				final char ind2 = ((DataField) vField).getIndicator2();
				final String tagName = tag + ind1 + ind2;
				final List<Subfield> subfields = ((DataField) vField)
						.getSubfields();
			//final Iterator<DataField> iter = subfields.iterator();

				if (subfields.size() == 1) {
					final Subfield subfield = subfields.get(0);
					getStreamReceiver().literal(tagName + subfield.getCode(),
							subfield.getData());
					//LOG.debug((tagName + subfield.getCode()) + "->"
					//		+ subfield.getData());
				}else {
					getStreamReceiver().startEntity(tagName);
					for(Subfield subfield:subfields) {
						final String value = subfield.getData();

					//	LOG.debug(subfield.getCode() + "->" + value);
						getStreamReceiver().literal(Character.toString(subfield.getCode()), value);

					}
					getStreamReceiver().endEntity();
				}
			}
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
}
