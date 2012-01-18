package org.culturegraph.metamorph.core2;

import org.culturegraph.metamorph.core2.collectors.ChooseLiteral;
import org.culturegraph.metamorph.core2.collectors.Collect;
import org.culturegraph.metamorph.core2.collectors.CollectEntity;
import org.culturegraph.metamorph.core2.collectors.CollectLiteral;
import org.culturegraph.metamorph.core2.collectors.Group;
import org.culturegraph.metamorph.util.ObjectFactory;

public final class CollectFactory extends ObjectFactory<Collect> {
	public CollectFactory() {
		registerClass("combine", CollectLiteral.class);
		registerClass("choose", ChooseLiteral.class);
		registerClass("group", Group.class);
		registerClass("entity", CollectEntity.class);
	}
}
