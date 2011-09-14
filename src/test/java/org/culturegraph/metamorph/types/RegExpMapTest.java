package org.culturegraph.metamorph.types;

import org.culturegraph.metamorph.core.MetamorphDefinitionException;
import org.junit.Test;

public final class RegExpMapTest {

	@Test(expected=MetamorphDefinitionException.class)
	public void testKeyConstraints(){
		final RegExpMap<String> expMap = new RegExpMap<String>();
		expMap.put("df(a)", "");
	
	}
	
}
