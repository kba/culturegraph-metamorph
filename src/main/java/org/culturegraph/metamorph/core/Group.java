package org.culturegraph.metamorph.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
final class Group extends DataProcessor  implements DataReceiver{
	
	private static final Logger LOG = LoggerFactory
			.getLogger(Group.class);

	private String name;
	private String value;

	private final Metamorph transformer;
	
	Group(final Metamorph transformer){
		super();
		this.transformer = transformer;		
	}
	
	/**
	 * @param name the nameFormat to set
	 */
	public void setName(final String name) {
		this.name = name;
		
	}

	/**
	 * @param valueFormat the valueFormat to set
	 */
	public void setValue(final String valueFormat) {
		this.value = valueFormat;
	}
	
	@Override
	public void data(final Literal literal, final DataSender sender, final int recordCount, final int entityCount) {
		String resultName = this.name;
		String resultValue = this.value;
		
		if(resultName==null){
			resultName = literal.getName();
		}
		if(resultValue==null){
			resultValue = applyFunctions(literal.getValue());
		}
		
		if(name==null || value ==null){
			LOG.warn("missing defaults name="+ resultName + " value=" + resultValue);	
		}else{
			if(LOG.isTraceEnabled()){
				LOG.trace("emiting literal "+ resultName + "=" + resultValue);
			}
			transformer.literal(resultName, resultValue);
		}
	}

	/**
	 * @param data
	 */
	public void addData(final Data data) {
		data.setDataReceiver(this);
	}
}
