package org.culturegraph.metamorph.core;

import junit.framework.Assert;

import org.culturegraph.metamorph.streamreceiver.MapCollector;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class CollectTest {
	
	private static final String NAME_A = "A";
	private static final String NAME_B = "B";
	private static final String VALUE_A = "Franz";
	private static final String VALUE_B = "Kafka";
	private static final String VALUE_C = "Josef";
	private static final String ENTITY_NAME = "asdfsdfsdf";
	private static final String VALUE_FORMAT = "${" + NAME_A + "} ${" + NAME_B + "}";
	private static final String SPACE = " ";
	private static final String COMPASITION_AB = VALUE_A + SPACE + VALUE_B;
	private static final String COMPASITION_AC = VALUE_A + SPACE + VALUE_C;
	
	private final Data dataA = new Data();
	private final Data dataB = new Data();
	
	private final MapCollector mapCollector = new MapCollector();
	

	public CollectTest() {
		dataA.setDefaultName(NAME_A);
		dataB.setDefaultName(NAME_B);
	}
	
	
	private void wireCollect(final Collect collect){
		collect.addData(dataA);
		collect.addData(dataB);
		collect.setName(ENTITY_NAME);
		collect.setStreamReceiver(mapCollector);
		mapCollector.getMap().clear();
	}
	
	@Test
	public void testCollectEntity() {

		wireCollect(new CollectEntity());
		
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataA.data(VALUE_A, 0, 0);
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(2, mapCollector.getMap().size());
		Assert.assertEquals(VALUE_A, mapCollector.getMap().get(NAME_A));
		Assert.assertEquals(VALUE_B, mapCollector.getMap().get(NAME_B));
	}
	

	
	@Test
	public void testCollectLiteral() {
		
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataA.data(VALUE_A, 0, 0);
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(1, mapCollector.getMap().size());
		Assert.assertEquals(COMPASITION_AB, mapCollector.getMap().get(ENTITY_NAME));	
	}
	
	@Test
	public void testExplicitReset() {
	
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		dataA.data(VALUE_A, 0, 0);
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(COMPASITION_AB, mapCollector.getMap().get(ENTITY_NAME));
		mapCollector.getMap().clear();
		
		collectLiteral.setReset(true);
		dataB.data(VALUE_C, 0, 0);
		Assert.assertEquals(COMPASITION_AC, mapCollector.getMap().get(ENTITY_NAME));
		mapCollector.getMap().clear();
		
		collectLiteral.setReset(false);
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(null, mapCollector.getMap().get(ENTITY_NAME));
		dataA.data(VALUE_A, 0, 0);
		Assert.assertEquals(COMPASITION_AB, mapCollector.getMap().get(ENTITY_NAME));
	}
	
	@Test
	public void testResetAfterNewRecord(){
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		dataA.data(VALUE_A, 0, 0);
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(COMPASITION_AB, mapCollector.getMap().get(ENTITY_NAME));
		mapCollector.getMap().clear();
		
		dataA.data(VALUE_A, 1, 0);
		Assert.assertTrue(mapCollector.getMap().isEmpty());
		dataB.data(VALUE_B, 2, 0);
		Assert.assertTrue(mapCollector.getMap().isEmpty());
		dataA.data(VALUE_A, 2, 0);
		Assert.assertEquals(COMPASITION_AB, mapCollector.getMap().get(ENTITY_NAME));
		
	}

}
