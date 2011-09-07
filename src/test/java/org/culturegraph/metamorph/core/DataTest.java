package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.core.Data.Mode;
import org.culturegraph.metamorph.functions.Constant;
import org.culturegraph.metamorph.functions.Regexp;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class DataTest {
	
	private static final String DEFAULT_NAME = "name";
	private static final String DEFAULT_VALUE = "value";
	private static final String INPUT = "alkjfoeijf38";
	private static final int RECORD_COUNT = 345;
	private static final int ENTITY_COUNT = 314584;
	private static final String CONSTANT_A = "AAA";
	private static final String CONSTANT_B = "BBB";
	private static final String ORIGIN_NAME = "s;lkepo";
	
	private static final String WRONG_NAME = "wrong name";
	private static final String WRONG_VALUE = "wrong value";
	
	
	private final Constant constant1 = new Constant();
	private final Constant constant2 = new Constant();
	private final Regexp regexp = new Regexp();

	public DataTest() {
		constant1.setValue(CONSTANT_A);
		constant2.setValue(CONSTANT_B);
		regexp.setMatch(CONSTANT_A);
	}
	
	@Test
	public void testSimpleReceive() {
		final Data data = new Data();
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final String name, final String value,  final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, INPUT, value);
				Assert.assertEquals(WRONG_NAME, DEFAULT_NAME, name);
				Assert.assertEquals("wrong recordCount", RECORD_COUNT, recordCount);
				Assert.assertEquals("wrong entityCount", ENTITY_COUNT, entityCount);
			}
		});
		data.setName(DEFAULT_NAME);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
	
	@Test
	public void testConstantValueAndName() {
		final Data data = new Data();
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final String name, final String value,  final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, DEFAULT_VALUE, value);
				Assert.assertEquals(WRONG_NAME, DEFAULT_NAME, name);

			}
		});
		data.setName(DEFAULT_NAME);
		data.setValue(DEFAULT_VALUE);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
	
	@Test
	public void testDataToName() {
		final Data data = new Data();
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final String name, final String value,  final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, DEFAULT_VALUE, value);
				Assert.assertEquals(WRONG_NAME, INPUT, name);
			}
		});
		data.setValue(DEFAULT_VALUE);
		data.setMode(Mode.AS_NAME);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
	
	@Test
	public void testFunctionProcessing() {
		final Data data = new Data();
		data.addFunction(constant1);
		data.addFunction(constant2);
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final String name, final String value, final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, CONSTANT_B, value);
				Assert.assertEquals(WRONG_NAME, DEFAULT_NAME, name);
			}
		});
		data.setName(DEFAULT_NAME);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
	
	@Test
	public void testFunctionProcessingBreak() {
		final Data data = new Data();
		data.addFunction(constant2);
		data.addFunction(regexp);
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final String name, final String value, final int recordCount,
					final int entityCount) {
				Assert.fail(); // Regexp should not find anything
			}
		});
		data.setName(DEFAULT_NAME);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
}
