package org.culturegraph.metamorph.util.string;

import junit.framework.Assert;

import org.junit.Test;

/**
 * tests {@link SetReplacer}
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class SetReplaceTest {



	@Test
	public void testReplece() {
	
		final SetReplacer setReplace = new SetReplacer();
		final String text = "auf sylt mit super krabben entsafter und apfel";
		final String target = "auf hawaii mit super shirt entsafter und surfboard";
		
		setReplace.addReplacement("sylt", "hawaii");
		setReplace.addReplacement("apfel", "surfboard");
		setReplace.addReplacement("krabben", "shirt");
		setReplace.addReplacement("super krabben entsafter", "Mai Tai");
		
		Assert.assertEquals(target, setReplace.replaceIn(text));

		
	}

}
