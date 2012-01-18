package org.culturegraph.metamorph.core2;

/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class Data extends ValueProcessorImpl implements NamedValueReceiver, NamedValueSource, EntityEndListener {

	public enum Mode {
		VALUE, COUNT
	}

	private String name;
	private String value;
	// private String meta;
	private final String source;

	private Mode mode = Mode.VALUE;
	private NamedValueReceiver dataReceiver;
	private int occurence;
	private int occurenceCount;
	private int processingCount;

	private int oldRecordCount;


	public Data(final String source) {
		super();
		this.source = source;
	}
	
	public void setOccurence(final int occurence) {
		this.occurence = occurence;
	}
	
	public String getSource(){
		return source;
	}

	private static String fallback(final String value, final String fallbackValue) {
		if (value == null) {
			return fallbackValue;
		}
		return value;
	}

	@Override
	public void receive(final String recName, final String recValue, final int recordCount, final int entityCount) {
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

		if(mode.equals(Mode.VALUE)){
			dataReceiver.receive(fallback(name, recName), fallback(value, tempData), recordCount, entityCount);
		}
		
		//dataReceiver.data(fallback(name, tempData), fallback(value, recValue), recordCount, entityCount);

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
	@Override
	public void setNamedValueReceiver(final NamedValueReceiver dataReceiver) {
		assert dataReceiver != null;
		this.dataReceiver = dataReceiver;
	}

	/**
	 * @return the dataReceiver
	 */
	public NamedValueReceiver getDataReceiver() {
		return dataReceiver;
	}

	public void setMode(final Mode mode) {
		this.mode = mode;
	}

	/**
	 * @param name
	 *            the defaultName to set
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param value
	 *            the defaultValue to set
	 */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the defaultName
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the defaultValue
	 */
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void onEntityEnd(final String entityName, final int recordCount, final int entityCount) {
		if (Mode.COUNT == mode) {
			dataReceiver.receive(name, String.valueOf(processingCount), oldRecordCount, 0);
		}
	}

}
