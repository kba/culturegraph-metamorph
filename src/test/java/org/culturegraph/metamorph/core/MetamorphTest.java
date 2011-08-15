package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.streamreceiver.DefaultStreamReceiver;
import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Markus Michael Geipel

 */
public final class MetamorphTest implements DataReceiver {
	
	private static final String NAME = "name";
	
	private static final String VALUE = "s234234ldkfj";
	
	private static final String ENTITY_NAME = "dfsdf";
	private static final String LITERAL_NAME = "fghgh";
	
	private static final String MATCHING_PATH = ENTITY_NAME + '.' + LITERAL_NAME;
	
	private static final String NON_MATCHING_PATH1 = "s234234";
	private static final String NON_MATCHING_PATH2 = ENTITY_NAME + ".lskdj";
	
	private static final StreamReceiver EMPTY_RECEIVER = new DefaultStreamReceiver();
	
	private Literal literal;

	
	private static Metamorph newMetamorphWithData(final DataReceiver receiver){
		final Metamorph metamorph = new Metamorph();
		metamorph.setOutputStreamReceiver(EMPTY_RECEIVER);
		final Data data = new Data();
		data.setDefaultName(NAME);
		data.setDataReceiver(receiver);
		metamorph.registerDataSource(data, MATCHING_PATH);
		return metamorph;
	}
	
	@Test
	public void testSimpleMapping() {
		final Metamorph metamorph = newMetamorphWithData(this);
		literal = null;
		metamorph.startRecord();
		
		//simple mapping without entity
		metamorph.literal(NON_MATCHING_PATH1, VALUE);
		Assert.assertNull(literal);
		
		metamorph.literal(MATCHING_PATH, VALUE);
		Assert.assertNotNull(literal);
		Assert.assertEquals(VALUE, literal.getValue());
		literal = null;
		
		// mapping with entity
		metamorph.startEntity(ENTITY_NAME);
		metamorph.literal(LITERAL_NAME, VALUE);
		Assert.assertNotNull(literal);
		Assert.assertEquals(VALUE, literal.getValue());
		literal = null;
		
		metamorph.literal(NON_MATCHING_PATH2, VALUE);
		Assert.assertNull(literal);
		
		metamorph.endEntity();
		metamorph.literal(LITERAL_NAME, VALUE);
		Assert.assertNull(literal);
	}
	

	@Test(expected=MetamorphException.class)
	public void testEntityBorderBalanceCheck1(){
		final Metamorph metamorph = new Metamorph();
		metamorph.setOutputStreamReceiver(EMPTY_RECEIVER);
		
		metamorph.startRecord();
		metamorph.startEntity(ENTITY_NAME);
		metamorph.startEntity(ENTITY_NAME);
		metamorph.endEntity();
		metamorph.endRecord();
	}
	
	@Test(expected=MetamorphException.class)
	public void testEntityBorderBalanceCheck2(){
		final Metamorph metamorph = new Metamorph();
		metamorph.setOutputStreamReceiver(EMPTY_RECEIVER);
		
		metamorph.startRecord();
		metamorph.startEntity(ENTITY_NAME);
		metamorph.endEntity();
		metamorph.endEntity();
		metamorph.endRecord();
	}
	

	@Override
	public void data(final String name, final String value, final DataSender sender, final int recordCount,
			final int entityCount) {
		this.literal = new Literal(name, value);
	}
}
