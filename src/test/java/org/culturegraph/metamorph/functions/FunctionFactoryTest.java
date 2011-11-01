package org.culturegraph.metamorph.functions;

import junit.framework.Assert;

import org.junit.Test;

public final class FunctionFactoryTest {
	@Test
	public void testInitialization() {
		final FunctionFactory factory = new FunctionFactory();
		Assert.assertFalse(factory.availableFunctions().isEmpty());

	}
}
