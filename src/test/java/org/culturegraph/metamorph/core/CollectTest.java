package org.culturegraph.metamorph.core;

import junit.framework.Assert;

import org.culturegraph.metamorph.stream.receivers.MapWriter;
import org.junit.Ignore;
import org.junit.Test;

/**
 * tests {@link CollectLiteral} and {@link CollectEntity}
 * 
 * @author Markus Michael Geipel
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
	private static final String SOURCE = "fantasy";
	
	private final Data dataA = newData(NAME_A);
	private final Data dataB = newData(NAME_B);
	
	private final MapWriter mapCollector = new MapWriter();
	private final Metamorph metamorph = new Metamorph();
	
	public CollectTest() {
		metamorph.setReceiver(mapCollector);
	}
	
	private static Data newData(final String name){
		final Data data = new Data(SOURCE);
		data.setName(name);
		return data;
	}
	
	private static Collect wireCollect(final Collect collect, final Data data1, final Data data2){
		collect.addNamedValueSource(data1);
		collect.addNamedValueSource(data2);
		collect.setName(COLLECT_NAME);
		return collect;
	}
	
	
	private void wireCollect(final Collect collect){
		collect.addNamedValueSource(dataA);
		collect.addNamedValueSource(dataB);
		collect.setName(COLLECT_NAME);
	}
	
	@Test
	public void testCollectEntity() {
		final CollectEntity collectEntity = new CollectEntity(metamorph);
		wireCollect(collectEntity);
		cleanUp();
		
		Assert.assertEquals(0, mapCollector.size());
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertEquals(0, mapCollector.size());
		dataB.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(2, mapCollector.size());
		Assert.assertEquals(VALUE_A, mapCollector.get(NAME_A));
		Assert.assertEquals(VALUE_B, mapCollector.get(NAME_B));
	}
	

	
	@Test
	public void testCollectLiteral() {
		
		final CollectLiteral collectLiteral = new CollectLiteral(metamorph);
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);

		cleanUp();
		
		Assert.assertTrue(nothingReceived());
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertTrue(nothingReceived());
		dataB.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertFalse(nothingReceived());
		
	
		
		Assert.assertEquals(COMPASITION_AB, getReceived());
	
	}
	
	
	@Test
	@Ignore
	//TODO: implement
	public void testCollectLiteralWithForceOnEnd() {
		
		final CollectLiteral collectLiteral = new CollectLiteral(metamorph);
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		cleanUp();
		
		Assert.assertTrue(nothingReceived());
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertTrue(nothingReceived());
		
		collectLiteral.onEntityEnd(Metamorph.RECORD_KEYWORD, 0,0);
		
	
		
		Assert.assertEquals(COMPASITION_AB, getReceived());
	
	}
	
	
	@Test
	public void testCollectLiteralWithOccurence() {
		
		final Data data1 = newData(NAME_A);
		data1.setOccurence(1);
		final Data data2 = newData(NAME_B);
		data2.setOccurence(2);
		
		
		final CollectLiteral collectLiteral = new CollectLiteral(metamorph);
		collectLiteral.setValue(VALUE_FORMAT);
		collectLiteral.setReset(true);
		wireCollect(collectLiteral, data1, data2);

		cleanUp();
		
		data1.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		data2.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		data1.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		data2.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertFalse(nothingReceived());
		Assert.assertEquals(COMPASITION_AB, getReceived());	
		
		cleanUp();
	
		data1.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		data2.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		data1.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		data2.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertTrue(nothingReceived());
			
		data1.receive(ORIGIN_NAME, VALUE_A, 1, 0);
		data2.receive(ORIGIN_NAME, VALUE_A, 1, 0);
		data1.receive(ORIGIN_NAME, VALUE_B, 1, 0);
		Assert.assertTrue(nothingReceived());
		
		data2.receive(ORIGIN_NAME, VALUE_B, 1, 0);

		Assert.assertFalse(nothingReceived());
		Assert.assertEquals(COMPASITION_AB, getReceived());	
	}
	
	@Test
	public void testExplicitReset() {
	
		final CollectLiteral collectLiteral = new CollectLiteral(metamorph);
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);

		cleanUp();
		
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		dataB.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(COMPASITION_AB, getReceived());

		cleanUp();
		
		collectLiteral.setReset(true);
		dataB.receive(ORIGIN_NAME, VALUE_C, 0, 0);
		Assert.assertEquals(COMPASITION_AC, getReceived());
		
		cleanUp();
		
		collectLiteral.setReset(false);
		dataB.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(null, getReceived());
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertEquals(COMPASITION_AB, getReceived());
	}
	
	@Test
	public void testResetAfterNewRecord(){
		final CollectLiteral collectLiteral = new CollectLiteral(metamorph);

		cleanUp();
		
		collectLiteral.setValue(VALUE_FORMAT);
		wireCollect(collectLiteral);
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		dataB.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertEquals(COMPASITION_AB, getReceived());
		mapCollector.clear();
		
		dataA.receive(ORIGIN_NAME, VALUE_A, 1, 0);
		Assert.assertTrue(nothingReceived());
		dataB.receive(ORIGIN_NAME, VALUE_B, 2, 0);
		Assert.assertTrue(nothingReceived());
		dataA.receive(ORIGIN_NAME, VALUE_A, 2, 0);
		Assert.assertEquals(COMPASITION_AB, getReceived());
		
	}
	
	@Test
	public void testChooseLiteral() {
		final ChooseLiteral chooseLiteral = new ChooseLiteral(metamorph);
		chooseLiteral.setValue(VALUE_A);
		
		wireCollect(chooseLiteral);

		cleanUp();
		
		Assert.assertTrue(nothingReceived());
		dataA.receive(ORIGIN_NAME, VALUE_A, 0, 0);
		Assert.assertTrue(nothingReceived());
		dataB.receive(ORIGIN_NAME, VALUE_B, 0, 0);
		Assert.assertTrue(nothingReceived());
		chooseLiteral.onEntityEnd(null,0,0);
		Assert.assertEquals(VALUE_A, getReceived());		
	}
	
	
		
	private String getReceived(){
		return mapCollector.get(COLLECT_NAME);
	}
	
	private void cleanUp(){
		mapCollector.clear();
	}
	
	private boolean nothingReceived(){
		return mapCollector.isEmpty();
	}

}
