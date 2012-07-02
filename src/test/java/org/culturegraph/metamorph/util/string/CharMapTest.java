package org.culturegraph.metamorph.util.string;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * tests {@link CharMap}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class CharMapTest {
	
	private static final String UML = "umlaut";

	@Test
	public void testEmptyMap() {
		final Map<Character, Integer> map = new CharMap<Integer>();
		
		for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; ++i) {
			Assert.assertNull(map.get(Byte.valueOf(i)));
		}
		
	}
	
	@Test
	public void testSingleEntry() {
		final Map<Character, String> map = new CharMap<String>();
		final char beite = 'ü';
		map.put(Character.valueOf(beite), UML);
		
		Assert.assertEquals(UML,map.get(Character.valueOf(beite)));
		
	}
	
	@Test
	public void testFullMap() {
		final Map<Character, Integer> map = new CharMap<Integer>();
		
		for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i) {
			map.put(Character.valueOf(i), Integer.valueOf(i));
		}
		
		for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; ++i) {
			Assert.assertEquals(Integer.valueOf(i),map.get(Character.valueOf(i)));
		}
		
	}
	
	@Test
	public void testMixedMap() {
		final Map<Character, Integer> map = new CharMap<Integer>();
		
		for (char i = 0; i < Character.MAX_VALUE-1; i+=2) {
			map.put(Character.valueOf(i), Integer.valueOf(i));
		}
		
		for (char i = 0; i < Character.MAX_VALUE-1; i+=2) {
			Assert.assertEquals(Integer.valueOf(i),map.get(Character.valueOf(i)));
		}
	}
}
