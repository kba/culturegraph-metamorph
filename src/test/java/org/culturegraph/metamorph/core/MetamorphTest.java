package org.culturegraph.metamorph.core;

import org.culturegraph.metamorph.streamreceiver.MapCollector;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class MetamorphTest implements DataReceiver {
	
	private static final String NAME = "name";
	private static final String MATCHING_SOURCE = "sldkfj";
	private static final String NON_MATCHING_SOURCE = "s234234";
	private static final String VALUE = "s234234ldkfj";
	private Literal literal;

	@Test
	public void testSimpleMapping() {
		final Metamorph metamorph = new Metamorph();
		metamorph.setOutputStreamReceiver(new MapCollector());
		final Data data = new Data();
		data.setDefaultName(NAME);
		data.setDataReceiver(this);
		metamorph.registerDataSource(data, MATCHING_SOURCE);
		literal = null;
		metamorph.startRecord();
		metamorph.literal(NON_MATCHING_SOURCE, VALUE);
		Assert.assertNull(literal);
		metamorph.literal(MATCHING_SOURCE, VALUE);
		Assert.assertNotNull(literal);
		Assert.assertEquals(VALUE, literal.getValue());
	}

	@Override
	public void data(final Literal literal, final DataSender sender, final int recordCount,
			final int entityCount) {
		this.literal = literal;
	}
}
