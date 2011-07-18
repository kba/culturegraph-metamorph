package org.culturegraph.metamorph.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Corresponds to the <code>collect-entity</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class CollectEntity extends AbstractCollect {


	private final Set<Literal> literals = new HashSet<Literal>();

	@Override
	protected void emit() {
		getTransformer().getOutputStreamReceiver().startEntity(getName());
		for(Literal literal:literals){
			if(literal.getName()!=null && literal.getValue()!=null){
				getTransformer().getOutputStreamReceiver().literal(literal.getName(), literal.getValue());
			}
		}
		getTransformer().getOutputStreamReceiver().endEntity();
	}
	

	@Override
	protected void receive(final Literal literal) {
		literals.add(literal);
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
