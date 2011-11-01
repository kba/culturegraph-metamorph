package org.culturegraph.metamorph.functions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public final class FunctionFactoryTest {
	
	
	private static final String VALUE = "test";

	@Test
	public void testInitialization() {
		final FunctionFactory factory = new FunctionFactory();
		Assert.assertFalse(factory.availableFunctions().isEmpty());
		
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("value", VALUE);
		final Function function = factory.newFunction("constant", attributes);
		Assert.assertEquals(VALUE, function.process("slkdfjlskjf"));
	}
}
