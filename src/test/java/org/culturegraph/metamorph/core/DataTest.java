package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.core.Data.Mode;
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
	private static final int MAGIC1 = 345;
	private static final int MAGIC2 = 314584;
	
	private static final String WRONG_NAME = "wrong name";
	private static final String WRONG_VALUE = "wrong value";

	@Test
	public void testSimpleReceive() {
		final Data data = new Data();
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final Literal literal, final DataSender sender, final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, INPUT, literal.getValue());
				Assert.assertEquals(WRONG_NAME, DEFAULT_NAME, literal.getName());
				Assert.assertEquals("wrong recordCount", MAGIC1, recordCount);
				Assert.assertEquals("wrong entityCount", MAGIC2, entityCount);
			}
		});
		data.setDefaultName(DEFAULT_NAME);
		data.data(INPUT, MAGIC1, MAGIC2);
	}
	
	@Test
	public void testDataToName() {
		final Data data = new Data();
		data.setDataReceiver(new DataReceiver() {
			@Override
			public void data(final Literal literal, final DataSender sender, final int recordCount,
					final int entityCount) {
				Assert.assertEquals(WRONG_VALUE, DEFAULT_VALUE, literal.getValue());
				Assert.assertEquals(WRONG_NAME, INPUT, literal.getName());
			}
		});
		data.setDefaultValue(DEFAULT_VALUE);
		data.setMode(Mode.AS_NAME);
		data.data(INPUT, MAGIC1, MAGIC2);
	}
}
