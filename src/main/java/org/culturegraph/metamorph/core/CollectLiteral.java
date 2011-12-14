package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core.Data.Mode;
import org.culturegraph.metamorph.functions.Function;
import org.culturegraph.metamorph.util.StringUtil;

/**
 * Corresponds to the <code>&lt;collect-literal&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class CollectLiteral extends AbstractCollect implements DataProcessor {

	private final Map<String, String> variables = new HashMap<String, String>();
	private final Set<String> variableNames = new HashSet<String>();
	private final DataProcessorImpl dataProcessor = new DataProcessorImpl();
//	private DataReceiver dataReceiver;

	public CollectLiteral(final Metamorph metamorph) {
		super(metamorph);
	}

	@Override
	protected void emit() {
		final String name = StringUtil.format(getName(), variables);
		String value = StringUtil.format(getValue(), variables);

		value = dataProcessor.applyFunctions(value);
		if (value == null) {
			return;
		}
		getMetamorph().data(name, value, getRecordCount(), getEntityCount());
	}

	@Override
	public void onEntityEnd(final String entityName) {
		emit();
	}

	@Override
	protected boolean isComplete() {
		return variables.size() == variableNames.size();
	}

	@Override
	protected void receive(final String name, final String value) {
		variables.put(name, value);
	}

	@Override
	protected void onAddData(final Data data) {
		data.setMode(Mode.VALUE);
		if (data.getDefaultName() == null) {
			data.setName(String.valueOf(getDataCount()));
		}
		variableNames.add(data.getDefaultName());
	}

	@Override
	protected void clear() {
		variables.clear();
	}

	@Override
	public void addFunction(final Function function) {
		dataProcessor.addFunction(function);
	}
}
