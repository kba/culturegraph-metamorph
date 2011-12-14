package org.culturegraph.metamorph.core;

import java.util.ArrayList;
import java.util.List;

import org.culturegraph.metamorph.core.Data.Mode;
import org.culturegraph.metamorph.functions.AbstractFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * tests {@link Data}
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
	private static final String SOURCE = "fantasy";
	
	private static final String WRONG_NAME = "wrong name";
	private static final String WRONG_VALUE = "wrong value";
	private static final String WRONG_COUNT = "wrong count";
	private static final int THREE = 3;
	
	private final Constant constant1 = new Constant(CONSTANT_A);
	private final Constant constant2 = new Constant(CONSTANT_B);
	private final Match regexp = new Match(CONSTANT_A);
	
	@Test
	public void testSimpleReceive() {
		final Data data = new Data(SOURCE);
		data.setDataReceiver(new NamedValueReceiver() {
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
		final Data data = new Data(SOURCE);
		data.setDataReceiver(new NamedValueReceiver() {
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
	public void testCount() {
		final Data data = new Data(SOURCE);
		data.setDataReceiver(new NamedValueReceiver() {
			@Override
			public void data(final String name, final String value,  final int recordCount,
					final int entityCount) {
				Assert.assertEquals("wrong record count", RECORD_COUNT+1, recordCount);
				Assert.assertEquals(WRONG_COUNT, String.valueOf(THREE), value);
				Assert.assertEquals(WRONG_NAME, DEFAULT_NAME, name);
			}
		});
		data.setName(DEFAULT_NAME);
		data.setMode(Mode.COUNT);
		
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT); // just a decoy
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT); // just a decoy
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT+1, ENTITY_COUNT); // counter will be reset on record count change
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT+1, ENTITY_COUNT);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT+1, ENTITY_COUNT);
		data.onEntityEnd(Metamorph.RECORD_KEYWORD); // emit count
	}
	
	@Test
	public void testOccurence() {

		final Data data = new Data(SOURCE);
		final CollectingDataReceiver receiver = new CollectingDataReceiver();
		data.setDataReceiver(receiver);
		data.setName(DEFAULT_NAME);
		data.setOccurence(2);
		
		data.data(ORIGIN_NAME, WRONG_VALUE, RECORD_COUNT, ENTITY_COUNT);
		data.data(ORIGIN_NAME, DEFAULT_VALUE, RECORD_COUNT, ENTITY_COUNT); // this is the correct one
		data.data(ORIGIN_NAME, WRONG_VALUE, RECORD_COUNT, ENTITY_COUNT); 
		
		data.setOccurence(THREE);
		data.data(ORIGIN_NAME, WRONG_VALUE, RECORD_COUNT+1, ENTITY_COUNT);
		data.data(ORIGIN_NAME, WRONG_VALUE, RECORD_COUNT+1, ENTITY_COUNT); 
		data.data(ORIGIN_NAME, DEFAULT_VALUE, RECORD_COUNT+1, ENTITY_COUNT); // this is the correct one
		
		Assert.assertEquals(2, receiver.getValues().size());
		Assert.assertEquals(DEFAULT_VALUE, receiver.getValues().get(0));
		Assert.assertEquals(DEFAULT_VALUE, receiver.getValues().get(1));
	}
	
	protected static final class CollectingDataReceiver implements NamedValueReceiver{
		private final List<String> values = new ArrayList<String>();
		protected List<String> getValues() {
			return values;
		}
		@Override
		public void data(final String name, final String value, final int recordCount, final int entityCount) {
			values.add(value);
		}
		
		
	}
	
	@Test
	public void testDataToName() {
		final Data data = new Data(SOURCE);
		data.setDataReceiver(new NamedValueReceiver() {
			@Override
			public void data(final String name, final String value,  final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, DEFAULT_VALUE, value);
				Assert.assertEquals(WRONG_NAME, INPUT, name);
			}
		});
		data.setValue(DEFAULT_VALUE);
		data.setMode(Mode.NAME);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
	
	@Test
	public void testFunctionProcessing() {
		final Data data = new Data(SOURCE);
		data.addFunction(constant1);
		data.addFunction(constant2);
		data.setDataReceiver(new NamedValueReceiver() {
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
		final Data data = new Data(SOURCE);
		data.addFunction(constant2);
		data.addFunction(regexp);
		data.setDataReceiver(new NamedValueReceiver() {
			@Override
			public void data(final String name, final String value, final int recordCount,
					final int entityCount) {
				Assert.fail(); // Regexp should not find anything
			}
		});
		data.setName(DEFAULT_NAME);
		data.data(ORIGIN_NAME, INPUT, RECORD_COUNT, ENTITY_COUNT);
	}
	
	private static final class Constant extends AbstractFunction{

		private final String constant;

		public Constant(final String constant) {
			super();
			this.constant = constant;
		}

		@Override
		public String process(final String value) {
			return constant;
		}

	}

	private static final class Match extends AbstractFunction {

		private final String match;

		public Match(final String match) {
			super();
			this.match = match;
		}

		@Override
		public String process(final String value) {
			if(match.equals(value)){
				return value;
			}
			return null;
		}

	}
}


