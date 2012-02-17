package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.util.StringUtil;


/**
 * Implementation of the <code>&lt;data&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class Data  extends AbstractNamedValuePipeHead{

	private String name;
	//private String value;
	private final String source;

	public Data(final String source) {
		super();
		this.source = source;
	}
		
	public String getSource(){
		return source;
	}

	@Override
	public void receive(final String recName, final String recValue, final NamedValueSource source, final int recordCount, final int entityCount) {
		getNamedValueReceiver().receive(StringUtil.fallback(name, recName), recValue, this, recordCount, entityCount);
	}


	/**
	 * @param name
	 *            the defaultName to set
	 */

	public void setName(final String name) {
		this.name = name;
	}

}
