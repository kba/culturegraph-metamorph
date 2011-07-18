package org.culturegraph.metamorph.functions;

import junit.framework.Assert;

import org.culturegraph.metamorph.core.MetamorphException;
import org.junit.Test;



/**
 * @author Markus Michael Geipel
 * @status Prototype
 */

public final class ISBNTest {
	
	private static final String ISBN13 = "9783150197882"; 
	private static final String ISBN10 = "3150197880"; 
	private static final String ISBN_DIRTY = "ISBN: 31.501-978-80, EUro 17"; 
	private static final String ISBN_INCORRECT_CHECK = "9783150197881"; 
	private static final String ISBN_INCORRECT_SIZE = "97830197881"; 
	
	@Test
	public void testTo13() {
		Assert.assertEquals(ISBN13, ISBN.isbn10to13(ISBN10));
	}
	
	@Test
	public void testTo10() {
		Assert.assertEquals(ISBN10,ISBN.isbn13to10(ISBN13));
	}
	
	@Test
	public void testCleanse() {
		Assert.assertEquals(ISBN10,ISBN.cleanse(ISBN_DIRTY));
	}
	
	@Test(expected = MetamorphException.class)
	public void testInvalidInputTo13() {
		ISBN.isbn10to13(ISBN_INCORRECT_SIZE);
	}
	
	@Test(expected = MetamorphException.class)
	public void testInvalidInputTo10() {
		ISBN.isbn13to10(ISBN_INCORRECT_SIZE);
	}
	
	@Test
	public void testIsValid() {
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_CHECK));
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_SIZE));
		Assert.assertTrue(ISBN.isValid(ISBN10));
		Assert.assertTrue(ISBN.isValid(ISBN13 ));
	}
}
