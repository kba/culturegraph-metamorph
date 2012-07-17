package org.culturegraph.metamorph.core.collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;
import org.culturegraph.metastream.type.ListMap;

/**
 * Builds the cross product of the data sources.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class Tuples extends AbstractCollect {

	private final ListMap<String, String> listMap = new ListMap<String, String>();
	private int minN = 1;

	public Tuples(final Metamorph metamorph) {
		super(metamorph);
		setNamedValueReceiver(metamorph);
	}

	public void setMinN(final int minN) {
		this.minN = minN;
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		listMap.put(name, value);
	}

	@Override
	protected boolean isComplete() {
		return false;
	}

	@Override
	protected void clear() {
		listMap.clear();

	}

	@Override
	protected void emit() {

		if (listMap.size() < minN) {
			return;
		}
		final List<String> keys = new ArrayList<String>();
		keys.addAll(listMap.keySet());
		Collections.sort(keys);

		List<String> temp = new ArrayList<String>();
		List<String> nextTemp = new ArrayList<String>();
		temp.add("");

		for (String key : keys) {
			final List<String> values = listMap.get(key);
			nextTemp = new ArrayList<String>(temp.size() * values.size());
			for (String value : values) {
				for (String base : temp) {
					nextTemp.add(base + value);
				}
			}
			temp = nextTemp;
		}

		for (String string : temp) {
			getNamedValueReceiver().receive(getName(), string, this, getRecordCount(), getEntityCount());
		}
		clear();
	}
}
