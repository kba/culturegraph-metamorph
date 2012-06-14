package org.culturegraph.metamorph.util.string;

import junit.framework.Assert;

import org.culturegraph.metamorph.util.string.SetMatcher.Match;
import org.junit.Test;

/**
 * tests {@link SetMatcher}
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class SetMatchTest {

	@Test
	public void testSetMatch() {
		final SetMatcher<String> setMatch = new SetMatcher<String>();

		final String[] cities = { "Perth", "York", "York Town", "München", "New York City", "New York", "Petersburg",
				"ert", };
		final int[] matches = { 7, 0, 7, 5, 1, 4, 1, 1, 2, 3 };
		final String text = "Pexrt Perth Peerth New York City York York Town München";

		for (int i = 0; i < cities.length; ++i) {
			final String city = cities[i];
			setMatch.put(city, city);
		}
		int index = 0;

		//System.out.println(text);
		for (Match<String> match : setMatch.match(text)) {
			//System.out.println(match.getValue() + " " + match.getStart());
			Assert.assertEquals(cities[matches[index]], match.getValue());
			++index;
		}
		// setMatch.printDebug(System.err);

		Assert.assertEquals("missing matches", matches.length, index);
	}
}
