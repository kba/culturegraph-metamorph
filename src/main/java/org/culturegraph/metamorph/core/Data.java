package org.culturegraph.metamorph.core;

/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class Data extends DataProcessorImpl implements DataReceiver, EntityEndListener {

	public enum Mode {
		VALUE, NAME, META, COUNT
	}

	private String name;
	private String value;
	// private String meta;

	private Mode mode = Mode.VALUE;
	private DataReceiver dataReceiver;
	private int occurence;
	private int occurenceCount;
	private int processingCount;

	private int oldRecordCount;

	public void setOccurence(final int occurence) {
		this.occurence = occurence;
	}

	private static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}

	@Override
	public void data(final String recName, final String recValue, final int recordCount, final int entityCount) {
		assert dataReceiver != null;

		updateCounts(recordCount);
		if (!isOccurenceOK()) {
			return;
		}

		final String tempData = applyFunctions(recValue);
		if (tempData == null) {
			return;
		}
		++processingCount;

		switch (mode) {
		case NAME:
			dataReceiver.data(fallback(name, tempData), fallback(value, recValue), recordCount, entityCount);
			break;
		case VALUE:
			dataReceiver.data(fallback(name, recName), fallback(value, tempData), recordCount, entityCount);
			break;
		case META:
			// dataReceiver.data(finalName, finalValue, recordCount,
			// entityCount);
			break;
		case COUNT:
			// nothing to do. count is emitted when the record is over
			break;
		default:
			break;
		}

		// if (Mode.NAME.equals(mode)) {
		// finalName = tempData;
		// } else if (finalValue == null) {
		// finalValue = tempData;
		// }
		//
		// dataReceiver.data(finalName, finalValue, recordCount, entityCount);
	}

	private boolean isOccurenceOK() {
		return occurence == 0 || occurence == occurenceCount;
	}

	private void updateCounts(final int recordCount) {
		if (recordCount == oldRecordCount) {
			++occurenceCount;
		} else {
			occurenceCount = 1;
			processingCount = 0;
			oldRecordCount = recordCount;
		}
	}

	/**
	 * @param dataReceiver
	 *            the dataReceiver to set
	 */
	public void setDataReceiver(final DataReceiver dataReceiver) {
		assert dataReceiver != null;
		this.dataReceiver = dataReceiver;
	}

	/**
	 * @return the dataReceiver
	 */
	public DataReceiver getDataReceiver() {
		return dataReceiver;
	}

	public void setMode(final Mode mode) {
		this.mode = mode;
	}

	/**
	 * @param name
	 *            the defaultName to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param value
	 *            the defaultValue to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return name;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return value;
	}

	@Override
	public void onEntityEnd(final String entityName) {
		if (Mode.COUNT == mode) {
			dataReceiver.data(name, String.valueOf(processingCount), oldRecordCount, 0);
		}
	}

}
