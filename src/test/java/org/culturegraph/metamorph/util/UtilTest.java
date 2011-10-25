package org.culturegraph.metamorph.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public final class UtilTest {
	
	@Test
	public void testFormat() {
		final Map<String, String> vars = new HashMap<String, String>();
		vars.put("a", "Aloha");
		vars.put("b", "Hawaii");
		vars.put("bb", "Hula");

		Assert.assertEquals("Aloha Hawaii", Util.format("${a} ${b}", vars));
		Assert.assertEquals("AlohaHawaii", Util.format("${a}${b}", vars));
		Assert.assertEquals("Aloha${b", Util.format("${a}${b", vars));
		Assert.assertEquals("XAloha${b", Util.format("X${a}${b", vars));
		Assert.assertEquals("XX", Util.format("X${ab}X", vars));
		Assert.assertEquals("XHulaXHulaX", Util.format("X${bb}X${bb}X", vars));
		Assert.assertEquals("{a}Hawaii", Util.format("{a}${b}", vars));
	}
}
