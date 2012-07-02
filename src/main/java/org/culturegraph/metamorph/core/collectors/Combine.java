package org.culturegraph.metamorph.core.collectors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metamorph.util.Util;

/**
 * Corresponds to the <code>&lt;collect-literal&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Combine extends AbstractCollect{
	private final Map<String, String> variables = new HashMap<String, String>();
	private final Set<NamedValueSource> sources = new HashSet<NamedValueSource>();
	private final Set<NamedValueSource> sourcesLeft = new HashSet<NamedValueSource>();
	
	public Combine(final Metamorph metamorph) {
		super(metamorph);
		setNamedValueReceiver(metamorph);
	}

	@Override
	protected void emit() {
		final String name = Util.format(getName(), variables);
		final String value = Util.format(getValue(), variables);
		getNamedValueReceiver().receive(name, value, this, getRecordCount(), getEntityCount());
	}


	@Override
	protected boolean isComplete() {
		return sourcesLeft.isEmpty();
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		variables.put(name, value);
		sourcesLeft.remove(source);
	}

	
	@Override
	public void addNamedValueSource(final NamedValueSource namedValueSource) {
		sources.add(namedValueSource);
		sourcesLeft.add(namedValueSource);
	}

	@Override
	protected void clear() {
		sourcesLeft.addAll(sources);
		variables.clear();
	}

}
