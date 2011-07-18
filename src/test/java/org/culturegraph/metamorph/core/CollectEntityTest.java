package org.culturegraph.metamorph.core;

import junit.framework.Assert;

import org.culturegraph.metamorph.streamreceiver.DefaultStreamReceiver;
import org.culturegraph.metamorph.streamreceiver.MapCollector;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class CollectEntityTest {
	
	private static final String NAME_A = "A";
	private static final String NAME_B = "B";
	private static final String VALUE_A = "Franz";
	private static final String VALUE_B = "Kafka";
	private static final String ENTITY_NAME = "asdfsdfsdf";
	private final CollectEntity collectEntity = new CollectEntity();
	private final Data dataA = new Data();
	private final Data dataB = new Data();
	

	public CollectEntityTest() {
		dataA.setDefaultName(NAME_A);
		dataB.setDefaultName(NAME_B);
		collectEntity.addData(dataA);
		collectEntity.addData(dataB);
		collectEntity.setName(ENTITY_NAME);
		
		
	}
	
	@Test
	public void testCollect() {
		final MapCollector mapCollector = new MapCollector();
		collectEntity.setStreamReceiver(mapCollector);
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataA.data(VALUE_A, 0, 0);
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataB.data(VALUE_B, 0, 0);
		Assert.assertEquals(2, mapCollector.getMap().size());
		Assert.assertEquals(VALUE_A, mapCollector.getMap().get(NAME_A));
		Assert.assertEquals(VALUE_B, mapCollector.getMap().get(NAME_B));
	}
}
