package org.culturegraph.metamorph.functions;

import junit.framework.Assert;

import org.junit.Test;



/**
 * @author Markus Michael Geipel
 */

public final class StringOperationsTest {
	
	
	private static final String VALUE1 = "Franz"; 
	private static final String VALUE2 = "Kafka";
	private static final String VALUE3 = "Josef"; 
	
	@Test
	public void testRegexp() {
		final Regexp regexp = new Regexp();
		regexp.setMatch(VALUE2);
		Assert.assertEquals(VALUE2, regexp.process(VALUE1+VALUE2));
		Assert.assertNull(regexp.process(VALUE3+VALUE1));
		regexp.setFormat(VALUE3 + " ${1}.");
		regexp.setMatch("((K)).*$");
		Assert.assertEquals(VALUE3 + " K.", regexp.process(VALUE1 + VALUE2));
	}
	
	@Test
	public void testCompose() {
		final Compose compose = new Compose();
		compose.setPrefix(VALUE1);
		Assert.assertEquals(VALUE1+VALUE2, compose.process(VALUE2));
		compose.setPrefix("");
		compose.setPostfix(VALUE1);
		Assert.assertEquals(VALUE2+VALUE1, compose.process(VALUE2));
		
	}
	

}
