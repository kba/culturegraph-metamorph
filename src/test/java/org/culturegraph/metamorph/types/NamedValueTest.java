package org.culturegraph.metamorph.types;

import org.junit.Assert;
import org.junit.Test;

/**
 * tests {@link NamedValue}
 * 
 * @author Markus Michael Geipel
 */
public final class NamedValueTest{
	
	private static final String NAME = "name";
	private static final String VALUE = "s234234ldkfj";
	private static final String OTHER_VALUE = "877687609";
	private static final String SMALL = "a";
	private static final String BIG = "b";


	
	@Test
	public void testNamedValueCompare(){
		final NamedValue namedValue1 = new NamedValue(SMALL, SMALL);
		final NamedValue namedValue2 = new NamedValue(SMALL, BIG);
		final NamedValue namedValue3 = new NamedValue(BIG, BIG);
		final NamedValue namedValue4 = new NamedValue(SMALL, SMALL);
		
		Assert.assertTrue(namedValue1.compareTo(namedValue4)==0);
		
		Assert.assertTrue(namedValue1.compareTo(namedValue2)==-1);
		Assert.assertTrue(namedValue2.compareTo(namedValue1)==1);
		
		Assert.assertTrue(namedValue2.compareTo(namedValue3)==-1);
		Assert.assertTrue(namedValue3.compareTo(namedValue2)==1);
		
		
		Assert.assertTrue(namedValue1.compareTo(namedValue4)==0);
	}
	
	@Test
	public void testNamedValueEquals() {
		final NamedValue namedValue1 = new NamedValue(NAME, VALUE);
		final NamedValue namedValue2 = new NamedValue(NAME, VALUE);
		final NamedValue namedValue3 = new NamedValue(NAME, OTHER_VALUE);
		final NamedValue namedValue4 = new NamedValue(OTHER_VALUE, VALUE);
		
		Assert.assertTrue(namedValue1.equals(namedValue1));
		Assert.assertTrue(namedValue1.equals(namedValue2));
		Assert.assertTrue(namedValue2.equals(namedValue1));
		
		Assert.assertFalse(namedValue1.equals(namedValue3));
		Assert.assertFalse(namedValue1.equals(namedValue4));
		Assert.assertFalse(namedValue4.equals(namedValue3));
		Assert.assertFalse(namedValue4.equals(new Object()));
		
		Assert.assertTrue(namedValue1.hashCode() == namedValue2.hashCode());
	}
}
