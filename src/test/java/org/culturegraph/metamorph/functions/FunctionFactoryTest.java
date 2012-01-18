package org.culturegraph.metamorph.functions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public final class FunctionFactoryTest {
	
	
	private static final String INPUT = "testKiwi";
	private static final String OUTPUT = "Kiwi";

	@Test
	public void testInitialization() {
		final FunctionFactory factory = new FunctionFactory();
		Assert.assertFalse(factory.getAvailableFunctions().isEmpty());
		
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("pattern", "test");
		attributes.put("with", "");
		final Function function = factory.newFunction("replace", attributes);
		Assert.assertEquals(OUTPUT, function.process(INPUT));
	}
}
