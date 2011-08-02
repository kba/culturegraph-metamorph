package org.culturegraph.metamorph.functions;

import junit.framework.Assert;

import org.junit.Test;



/**
 * @author Markus Michael Geipel
 */

public final class ISBNTest {
	
	private static final String ISBN13A = "9781933988313"; 
	private static final String ISBN10A = "1933988312"; 
	private static final String ISBN10B = "3406548407";
	
	private static final String ISBN10A_DIRTY = "ISBN: 1-.93.3-988-31-2 EUro 17.70"; 
	private static final String ISBN10C_DIRTY = "ISBN 3-7691-3150-9 1. Aufl. 2006";
	private static final String ISBN13D_DIRTY = "ISBN 978-3-608-91086-5 (Klett-Cotta) ab der 7. Aufl.";
	private static final String ISBN10F_DIRTY = "ISBN 88-7336-210-9 35.00 EUR";
	
	private static final String ISBN_INCORRECT_CHECK13 = "9781933988314"; 
	private static final String ISBN_INCORRECT_CHECK10 = "1933988311"; 
	
	
	private static final String ISBN_INCORRECT_SIZE1 = "12345678901234"; 
	private static final String ISBN_INCORRECT_SIZE2 = "123456789012"; 
	private static final String ISBN_INCORRECT_SIZE3 = "123456789"; 
	
	@Test
	public void testTo13() {
		Assert.assertEquals(ISBN13A, ISBN.isbn10to13(ISBN10A));
	}
	
	@Test
	public void testTo10() {
		Assert.assertEquals(ISBN10A,ISBN.isbn13to10(ISBN13A));
	}
	
	@Test
	public void testCleanse() {
		Assert.assertEquals(ISBN10A,ISBN.cleanse(ISBN10A_DIRTY));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInputTo13() {
		ISBN.isbn10to13(ISBN_INCORRECT_SIZE3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInputTo10() {
		ISBN.isbn13to10(ISBN_INCORRECT_SIZE2);
	}
	
	@Test
	public void testIsValid() {
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_CHECK13));
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_CHECK10));
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_SIZE1));
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_SIZE2));
		Assert.assertFalse(ISBN.isValid(ISBN_INCORRECT_SIZE3));
		
		Assert.assertTrue(ISBN.isValid(ISBN10B));
		Assert.assertTrue(ISBN.isValid(ISBN10A));
		Assert.assertTrue(ISBN.isValid(ISBN13A ));
		Assert.assertTrue(ISBN.isValid(ISBN.cleanse(ISBN10C_DIRTY)));
		Assert.assertTrue(ISBN.isValid(ISBN.cleanse(ISBN13D_DIRTY)));
		Assert.assertTrue(ISBN.isValid(ISBN.cleanse(ISBN10F_DIRTY)));
	}
	
	@Test(expected = ISBN.InvalidISBNLengthException.class)
	public void testCleanseException1(){
		final ISBN isbn = new ISBN();
		isbn.process(ISBN_INCORRECT_SIZE3);
	}
	
	@Test(expected = ISBN.InvalidISBNLengthException.class)
	public void testCleanseException2(){
		final ISBN isbn = new ISBN();
		isbn.process(ISBN_INCORRECT_SIZE1);
	}

}
