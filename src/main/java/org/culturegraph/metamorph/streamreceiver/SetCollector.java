package org.culturegraph.metamorph.streamreceiver;

import java.util.HashSet;
import java.util.Set;

import org.culturegraph.metamorph.core.Literal;

/**
 * Collects {@link Literal}s in a {@link Set}. So there will not be duplicates.
 * @author Markus Michael Geipel
 */
public final class SetCollector extends DefaultStreamReceiver {

	private final Set<Literal> set;

	public SetCollector() {
		super();
		set=new HashSet<Literal>();
	}
	
	/**
	 * @param set is filled with the received results.
	 */
	public SetCollector(final Set<Literal> set) {
		super();
		this.set = set;
	}
	
	@Override
	public void startRecord(){
		set.clear();
	}

	@Override
	public void literal(final String name, final String value) {
		set.add(new Literal(name, value));
	}

	/**
	 * @return set with received results.
	 */
	public Set<Literal> getSet() {
		return set;
	}
	
	@Override
	public String toString() {
		return set.toString();
	}

}
