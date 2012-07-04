package org.culturegraph.metamorph.util;

import java.io.IOException;

import junit.framework.Assert;

import org.culturegraph.util.MultiMap;
import org.culturegraph.util.MultimapMaps;
import org.culturegraph.util.SimpleMultiMap;
import org.junit.Test;

/**
 * tests {@link MultimapMaps}
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class MultimapsTest {

	private static final String MAP1 = "map1";
	private static final String MAP2 = "map2";
	private static final String[] FILES = { "src/test/resources/data/multimapTestData1.txt",
			"src/test/resources/data/multimapTestData2.txt",};

	@Test
	public void testLoad() throws IOException {
		final SimpleMultiMap multiMap = new MultiMap();
		MultimapMaps.load(multiMap, FILES);

		Assert.assertEquals("A", multiMap.getValue(MAP1, "a"));
		Assert.assertEquals("B", multiMap.getValue(MAP1, "b"));
		Assert.assertEquals("C", multiMap.getValue(MAP2, "c"));
		Assert.assertEquals("D", multiMap.getValue(MAP2, "d"));

		Assert.assertTrue(2 == multiMap.getMap(MAP1).size());
		Assert.assertTrue(2 == multiMap.getMap(MAP2).size());
	}

	@Test
	public void testJsonExport() throws IOException {
		final SimpleMultiMap multiMap = new MultiMap();
		MultimapMaps.load(multiMap, FILES);

		Assert.assertEquals("{\"map2\":{\"d\":\"D\",\"c\":\"C\"},\"map1\":{\"b\":\"B\",\"a\":\"A\"}}",
				MultimapMaps.asJsonString(multiMap));
	}

}
