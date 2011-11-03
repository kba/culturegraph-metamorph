package org.culturegraph.metamorph.stream;

import java.util.HashSet;
import java.util.Set;

import org.culturegraph.metamorph.types.NamedValue;

/**
 * Collects {@link NamedValue}s in a {@link Set}. So there will not be duplicates.
 * @author Markus Michael Geipel
 */
public final class SetCollector extends DefaultStreamReceiver {

	private final Set<NamedValue> set;

	public SetCollector() {
		super();
		set=new HashSet<NamedValue>();
	}
	
	/**
	 * @param set is filled with the received results.
	 */
	public SetCollector(final Set<NamedValue> set) {
		super();
		this.set = set;
	}
	
	@Override
	public void startRecord(final String identifier){
		set.clear();
	}

	@Override
	public void literal(final String name, final String value) {
		set.add(new NamedValue(name, value));
	}

	/**
	 * @return set with received results.
	 */
	public Set<NamedValue> getSet() {
		return set;
	}
	
	@Override
	public String toString() {
		return set.toString();
	}

}
