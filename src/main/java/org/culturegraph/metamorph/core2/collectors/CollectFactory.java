package org.culturegraph.metamorph.core2.collectors;

import org.culturegraph.metamorph.util.ObjectFactory;

public final class CollectFactory extends ObjectFactory<Collect> {
	public CollectFactory() {
		super();
		registerClass("combine", CollectLiteral.class);
		registerClass("choose", ChooseLiteral.class);
		registerClass("group", Group.class);
		registerClass("entity", CollectEntity.class);
	}
}
