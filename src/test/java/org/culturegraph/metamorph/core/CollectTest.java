package org.culturegraph.metamorph.core;

import junit.framework.Assert;

import org.culturegraph.metamorph.streamreceiver.MapCollector;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class CollectTest {
	
	private static final String ORIGIN_NAME = "sdfsdf";
	private static final String NAME_A = "A";
	private static final String NAME_B = "B";
	private static final String VALUE_A = "Franz";
	private static final String VALUE_B = "Kafka";
	private static final String VALUE_C = "Josef";
	private static final String COLLECT_NAME = "asdfsdfsdf";
	private static final String VALUE_FORMAT = "${" + NAME_A + "} ${" + NAME_B + "}";
	private static final String SPACE = " ";
	private static final String COMPASITION_AB = VALUE_A + SPACE + VALUE_B;
	private static final String COMPASITION_AC = VALUE_A + SPACE + VALUE_C;
	
	private final Data dataA = new Data();
	private final Data dataB = new Data();
	
	private final MapCollector mapCollector = new MapCollector();
	private final SimpleDataReceiver dataReceiver = new SimpleDataReceiver();
	

	public CollectTest() {
		dataA.setName(NAME_A);
		dataB.setName(NAME_B);
	}
	
	
	private void wireCollect(final Collect collect){
		collect.addData(dataA);
		collect.addData(dataB);
		collect.setName(COLLECT_NAME);
	}
	
	@Test
	public void testCollectEntity() {
		final CollectEntity collectEntity = new CollectEntity();
		wireCollect(collectEntity);
		collectEntity.setStreamReceiver(mapCollector);
		mapCollector.getMap().clear();
		
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataA.data(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertEquals(0, mapCollector.getMap().size());
		dataB.data(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(2, mapCollector.getMap().size());
		Assert.assertEquals(VALUE_A, mapCollector.getMap().get(NAME_A));
		Assert.assertEquals(VALUE_B, mapCollector.getMap().get(NAME_B));
	}
	

	
	@Test
	public void testCollectLiteral() {
		
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		collectLiteral.setDataReceiver(dataReceiver);
		dataReceiver.clear();
		
		Assert.assertTrue(dataReceiver.isEmpty());
		dataA.data(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertTrue(dataReceiver.isEmpty());
		dataB.data(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertFalse(dataReceiver.isEmpty());
		Assert.assertEquals(COMPASITION_AB, dataReceiver.getContent());	
	}
	
	@Test
	public void testExplicitReset() {
	
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		collectLiteral.setDataReceiver(dataReceiver);
		dataReceiver.clear();
		
		dataA.data(ORIGIN_NAME, VALUE_A, 0, 0);
		dataB.data(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(COMPASITION_AB, dataReceiver.getContent());
		dataReceiver.clear();
		
		collectLiteral.setReset(true);
		dataB.data(ORIGIN_NAME, VALUE_C, 0, 0);
		Assert.assertEquals(COMPASITION_AC, dataReceiver.getContent());
		dataReceiver.clear();
		
		collectLiteral.setReset(false);
		dataB.data(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(null, dataReceiver.getContent());
		dataA.data(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertEquals(COMPASITION_AB, dataReceiver.getContent());
	}
	
	@Test
	public void testResetAfterNewRecord(){
		final CollectLiteral collectLiteral = new CollectLiteral();
		collectLiteral.setDataReceiver(dataReceiver);
		dataReceiver.clear();
		
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		dataA.data(ORIGIN_NAME, VALUE_A, 0, 0);
		dataB.data(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(COMPASITION_AB, dataReceiver.getContent());
		dataReceiver.clear();
		
		dataA.data(ORIGIN_NAME, VALUE_A, 1, 0);
		Assert.assertTrue(dataReceiver.isEmpty());
		dataB.data(ORIGIN_NAME, VALUE_B, 2, 0);
		Assert.assertTrue(dataReceiver.isEmpty());
		dataA.data(ORIGIN_NAME, VALUE_A, 2, 0);
		Assert.assertEquals(COMPASITION_AB, dataReceiver.getContent());
		
	}
	
	private static final class SimpleDataReceiver implements DataReceiver{

		private String content;

		protected SimpleDataReceiver() {
			// to avoid synthetic accessor methods
		}

		public String getContent() {
			return content;
		}
		
		public boolean isEmpty(){
			return content==null;
		}
		
		public void clear(){
			content = null;
		}

		@Override
		public void data(final String name, final String value, final int recordCount, final int entityCount) {
			content = value;
		}
	}
}
