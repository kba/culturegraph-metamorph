package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.MetamorphBuilder;
import org.culturegraph.metamorph.util.ObjectFactory;

/**
 * Factory for all collectors used by {@link Metamorph} and {@link MetamorphBuilder}
 * 
 * @author Markus Michael Geipel
 *
 */
public final class CollectFactory extends ObjectFactory<AbstractCollect> {
	public CollectFactory() {
		super();
		registerClass("combine", Combine.class);
		registerClass("choose", Choose.class);
		registerClass("group", Group.class);
		registerClass("entity", Entity.class);
		registerClass("concat", Concat.class);
	}
}
