package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.Map;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.culturegraph.metamorph.stream.AbstractStreamReceiver;
import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.types.NamedValue;
import org.junit.Assert;
import org.junit.Test;

/**
 * tests {@link Metamorph}
 * 
 * @author Markus Michael Geipel
 */
public final class MetamorphBasicTest implements NamedValueReceiver {
	
	private static final String NAME = "name";
	private static final String VALUE = "s234234ldkfj";
	private static final String ENTITY_NAME = "dfsdf";
	private static final String LITERAL_NAME = "fghgh";
	private static final String MATCHING_PATH = ENTITY_NAME + '.' + LITERAL_NAME;
	private static final String NON_MATCHING_PATH1 = "s234234";
	private static final String NON_MATCHING_PATH2 = ENTITY_NAME + ".lskdj";
	private static final StreamReceiver EMPTY_RECEIVER = new AbstractStreamReceiver() {
		@Override
		public void literal(final String name, final String value) {
			// nothing
		}
	};
	private static final String FEEDBACK_VAR = "@var";
	private static final String MAP_NAME = "sdfklsjef";

	private NamedValue namedValue;
	
	private static Metamorph newMetamorphWithData(final NamedValueReceiver receiver){
		final Metamorph metamorph = new Metamorph();
		metamorph.setReceiver(EMPTY_RECEIVER);
		final Data data = new Data(MATCHING_PATH);
		data.setName(NAME);
		data.setNamedValueReceiver(receiver);
		metamorph.registerData(data);
		return metamorph;
	}
	
	@Test
	public void testSimpleMapping() {
		final Metamorph metamorph = newMetamorphWithData(this);
		namedValue = null;
		metamorph.startRecord(null);
		
		//simple mapping without entity
		metamorph.literal(NON_MATCHING_PATH1, VALUE);
		Assert.assertNull(namedValue);
		
		metamorph.literal(MATCHING_PATH, VALUE);
		Assert.assertNotNull(namedValue);
		Assert.assertEquals(VALUE, namedValue.getValue());
		namedValue = null;
		
		// mapping with entity
		metamorph.startEntity(ENTITY_NAME);
		metamorph.literal(LITERAL_NAME, VALUE);
		Assert.assertNotNull(namedValue);
		Assert.assertEquals(VALUE, namedValue.getValue());
		namedValue = null;
		
		metamorph.literal(NON_MATCHING_PATH2, VALUE);
		Assert.assertNull(namedValue);
		
		metamorph.endEntity();
		metamorph.literal(LITERAL_NAME, VALUE);
		Assert.assertNull(namedValue);
	}
	
	@Test
	public void testMultiMap(){
		final Metamorph metamorph = new Metamorph();
		final Map<String, String> map = new HashMap<String, String>();
		map.put(NAME, VALUE);
		
		metamorph.putMap(MAP_NAME, map);
		Assert.assertNotNull(metamorph.getMap(MAP_NAME));
		Assert.assertNotNull(metamorph.getValue(MAP_NAME,NAME));
		Assert.assertEquals(VALUE, metamorph.getValue(MAP_NAME,NAME));
		
		map.put(SimpleMultiMap.DEFAULT_MAP_KEY, VALUE);
		Assert.assertNotNull(metamorph.getValue(MAP_NAME,"sdfadsfsdf"));
		Assert.assertEquals(VALUE, metamorph.getValue(MAP_NAME,"sdfsdf"));
		
	}
	
	@Test
	public void testFeedback() {
	
		final Metamorph metamorph = new Metamorph();
		metamorph.setReceiver(EMPTY_RECEIVER);
		Data data;
		
		data = new Data(MATCHING_PATH);
		data.setName(FEEDBACK_VAR);
		data.setNamedValueReceiver(metamorph);
		metamorph.registerData(data);
		
		data = new Data(FEEDBACK_VAR);
		data.setName(NAME);
		data.setNamedValueReceiver(this);
		metamorph.registerData(data);
		
		namedValue = null;
		
		metamorph.startRecord(null);
		metamorph.literal(MATCHING_PATH, VALUE);
		Assert.assertNotNull(namedValue);
		Assert.assertEquals(VALUE, namedValue.getValue());
		namedValue = null;
		
		
	}
	

	@Test(expected=IllegalStateException.class)
	public void testEntityBorderBalanceCheck1(){
		final Metamorph metamorph = new Metamorph();
		metamorph.setReceiver(EMPTY_RECEIVER);
		
		metamorph.startRecord(null);
		metamorph.startEntity(ENTITY_NAME);
		metamorph.startEntity(ENTITY_NAME);
		metamorph.endEntity();
		metamorph.endRecord();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testEntityBorderBalanceCheck2(){
		final Metamorph metamorph = new Metamorph();
		metamorph.setReceiver(EMPTY_RECEIVER);
		
		metamorph.startRecord(null);
		metamorph.startEntity(ENTITY_NAME);
		metamorph.endEntity();
		metamorph.endEntity();
		metamorph.endRecord();
	}
	



	@Override
	public void receive(final String name, final String value, final NamedValueSource source, final int recordCount, final int entityCount) {
		this.namedValue = new NamedValue(name, value);
		
	}
}
