package org.culturegraph.metamorph.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Markus Michael Geipel

 */
public final class LiteralTest{
	
	private static final String NAME = "name";
	private static final String VALUE = "s234234ldkfj";
	private static final String OTHER_VALUE = "877687609";

	@Test
	public void testLiteralEquals() {
		final Literal literal1 = new Literal(NAME, VALUE);
		final Literal literal2 = new Literal(NAME, VALUE);
		final Literal literal3 = new Literal(NAME, OTHER_VALUE);
		final Literal literal4 = new Literal(OTHER_VALUE, VALUE);
		
		Assert.assertTrue(literal1.equals(literal1));
		Assert.assertTrue(literal1.equals(literal2));
		Assert.assertTrue(literal2.equals(literal1));
		
		Assert.assertFalse(literal1.equals(literal3));
		Assert.assertFalse(literal1.equals(literal4));
		Assert.assertFalse(literal4.equals(literal3));
		Assert.assertFalse(literal4.equals(new Object()));
		
		Assert.assertTrue(literal1.hashCode() == literal2.hashCode());
	}
}
