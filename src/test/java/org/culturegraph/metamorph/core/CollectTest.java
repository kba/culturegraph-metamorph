package org.culturegraph.metamorph.core;

import junit.framework.Assert;

import org.culturegraph.metamorph.streamreceiver.MapCollector;
import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
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
	

	public CollectTest() {
		dataA.setDefaultName(NAME_A);
		dataB.setDefaultName(NAME_B);
	}
	
	
	private void wireCollect(final Collect collect, final StreamReceiver streamReceiver){
		collect.addData(dataA);
		collect.addData(dataB);
		collect.setName(ENTITY_NAME);
		collect.setStreamReceiver(streamReceiver);
	}
	
	@Test
	public void testCollectEntity() {
	
		final MapCollector mapCollector = new MapCollector();
		wireCollect(new CollectEntity(), mapCollector);
		
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
		final MapCollector mapCollector = new MapCollector();
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral, mapCollector);
		
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataA.data(VALUE_A, 0, 0);
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(1, mapCollector.getMap().size());
		Assert.assertEquals(COMPASITION_AB, mapCollector.getMap().get(ENTITY_NAME));	
	}
	
	@Test
	public void testReset() {
		final MapCollector mapCollector = new MapCollector();
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral, mapCollector);
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
}
