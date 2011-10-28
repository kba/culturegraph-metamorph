package org.culturegraph.metamorph.types;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Markus Michael Geipel

 */
public final class LiteralTest{
	
	private static final String NAME = "name";
	private static final String VALUE = "s234234ldkfj";
	private static final String OTHER_VALUE = "877687609";
	private static final String SMALL = "a";
	private static final String BIG = "b";


	
	@Test
	public void testLiteralCompare(){
		final NamedValue literal1 = new NamedValue(SMALL, SMALL);
		final NamedValue literal2 = new NamedValue(SMALL, BIG);
		final NamedValue literal3 = new NamedValue(BIG, BIG);
		final NamedValue literal4 = new NamedValue(SMALL, SMALL);
		
		Assert.assertTrue(literal1.compareTo(literal4)==0);
		
		Assert.assertTrue(literal1.compareTo(literal2)==-1);
		Assert.assertTrue(literal2.compareTo(literal1)==1);
		
		Assert.assertTrue(literal2.compareTo(literal3)==-1);
		Assert.assertTrue(literal3.compareTo(literal2)==1);
		
		
		Assert.assertTrue(literal1.compareTo(literal4)==0);
	}
	
	@Test
	public void testLiteralEquals() {
		final NamedValue literal1 = new NamedValue(NAME, VALUE);
		final NamedValue literal2 = new NamedValue(NAME, VALUE);
		final NamedValue literal3 = new NamedValue(NAME, OTHER_VALUE);
		final NamedValue literal4 = new NamedValue(OTHER_VALUE, VALUE);
		
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
