package org.culturegraph.metamorph.util.string;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * tests {@link ByteMap}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class ByteMapTest {
	
	private static final String TWO = "two";

	@Test
	public void testEmptyMap() {
		final Map<Byte, Integer> map = new ByteMap<Integer>();
		
		for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; ++i) {
			Assert.assertNull(map.get(Byte.valueOf(i)));
		}
		
	}
	
	@Test
	public void testSingleEntry() {
		final Map<Byte, String> map = new ByteMap<String>();
		final byte beite = 2;
		map.put(Byte.valueOf(beite), TWO);
		
		Assert.assertEquals(TWO,map.get(Byte.valueOf(beite)));
		
	}
	
	@Test
	public void testFullMap() {
		final Map<Byte, Integer> map = new ByteMap<Integer>();
		
		for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; ++i) {
			map.put(Byte.valueOf(i), Integer.valueOf(i));
		}
		
		for (byte i = 0; i < Byte.MAX_VALUE; ++i) {
			Assert.assertEquals(Integer.valueOf(i),map.get(Byte.valueOf(i)));
		}
		
	}
	
	@Test
	public void testMixedMap() {
		final Map<Byte, Integer> map = new ByteMap<Integer>();
		
		for (byte i = 0; i < Byte.MAX_VALUE-1; i+=2) {
			map.put(Byte.valueOf(i), Integer.valueOf(i));
		}
		
		for (byte i = 0; i < Byte.MAX_VALUE-1; i+=2) {
			Assert.assertEquals(Integer.valueOf(i),map.get(Byte.valueOf(i)));
		}
	}
}
