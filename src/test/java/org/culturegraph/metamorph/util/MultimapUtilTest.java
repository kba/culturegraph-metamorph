package org.culturegraph.metamorph.util;

import java.io.IOException;

import junit.framework.Assert;

import org.culturegraph.metamorph.multimap.MultiMap;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.junit.Test;

/**
 * tests {@link StringUtil}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class MultimapUtilTest {
	
	private static final String MAP1 = "map1";
	private static final String MAP2 = "map2";
	
	@Test
	public void testFormat() throws IOException {
		final SimpleMultiMap multiMap = new MultiMap();
		MultimapMapsLoader.load(multiMap, "src/test/resources/data/multimapTestData1.txt", "src/test/resources/data/multimapTestData2.txt");
		
		Assert.assertEquals("A", multiMap.getValue(MAP1, "a"));
		Assert.assertEquals("B", multiMap.getValue(MAP1, "b"));
		Assert.assertEquals("C", multiMap.getValue(MAP2, "c"));
		Assert.assertEquals("D", multiMap.getValue(MAP2, "d"));
		
		Assert.assertTrue(2==multiMap.getMap(MAP1).size());
		Assert.assertTrue(2==multiMap.getMap(MAP2).size());
		
	}

}
