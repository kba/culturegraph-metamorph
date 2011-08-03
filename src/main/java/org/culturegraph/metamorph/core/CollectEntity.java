package org.culturegraph.metamorph.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Corresponds to the <code>&lt;collect-entity&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class CollectEntity extends AbstractCollect {


	private final Set<Literal> literals = new HashSet<Literal>();

	@Override
	protected void emit() {
		getStreamReceiver().startEntity(getName());
		for(Literal literal:literals){
			if(literal.getName()!=null && literal.getValue()!=null){
				getStreamReceiver().literal(literal.getName(), literal.getValue());
			}
		}
		getStreamReceiver().endEntity();
	}
	

	@Override
	protected void receive(final String name, final String value) {
		literals.add(new Literal(name, value));
	}


	@Override
	protected boolean isComplete() {
		return literals.size() == getDataCount();
	}

	@Override
	protected void clear() {
		literals.clear();
	}
}
