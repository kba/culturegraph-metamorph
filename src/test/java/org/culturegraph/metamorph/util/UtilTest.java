package org.culturegraph.metamorph.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * tests {@link StringUtil}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class UtilTest {
	
	@Test
	public void testFormat() {
		final Map<String, String> vars = new HashMap<String, String>();
		vars.put("a", "Aloha");
		vars.put("b", "Hawaii");
		vars.put("bb", "Hula");

		Assert.assertEquals("Aloha Hawaii", StringUtil.format("${a} ${b}", vars));
		Assert.assertEquals("AlohaHawaii", StringUtil.format("${a}${b}", vars));
		Assert.assertEquals("Aloha${b", StringUtil.format("${a}${b", vars));
		Assert.assertEquals("XAloha${b", StringUtil.format("X${a}${b", vars));
		Assert.assertEquals("XX", StringUtil.format("X${ab}X", vars));
		Assert.assertEquals("XHulaXHulaX", StringUtil.format("X${bb}X${bb}X", vars));
		Assert.assertEquals("{a}Hawaii", StringUtil.format("{a}${b}", vars));
		Assert.assertEquals("Hula$Hula", StringUtil.format("${bb}$${bb}", vars));
		
	}
}
