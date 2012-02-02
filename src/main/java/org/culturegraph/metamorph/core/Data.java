package org.culturegraph.metamorph.core;


/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class Data  extends AbstractNamedValuePipe{

	private String name;
	private String value;
	private final String source;

	public Data(final String source) {
		super();
		this.source = source;
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
	public void receive(final String recName, final String recValue, final NamedValueSource source, final int recordCount, final int entityCount) {
		getNamedValueReceiver().receive(fallback(name, recName), fallback(value, recValue),this, recordCount, entityCount);
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
	
	public String getName() {
		return name;
	}

	/**
	 * @return the defaultValue
	 */

	public String getValue() {
		return value;
	}


}
