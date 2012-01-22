package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.util.ObjectFactory;

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
