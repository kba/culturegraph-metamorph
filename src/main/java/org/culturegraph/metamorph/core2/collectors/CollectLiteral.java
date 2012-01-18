package org.culturegraph.metamorph.core2.collectors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.culturegraph.metamorph.core2.NamedValueReceiver;
import org.culturegraph.metamorph.core2.NamedValueSource;
import org.culturegraph.metamorph.core2.ValueProcessor;
import org.culturegraph.metamorph.core2.ValueProcessorImpl;
import org.culturegraph.metamorph.core2.functions.Function;
import org.culturegraph.metamorph.util.StringUtil;

/**
 * Corresponds to the <code>&lt;collect-literal&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class CollectLiteral extends AbstractCollect implements ValueProcessor, NamedValueSource{

	private final Map<String, String> variables = new HashMap<String, String>();
	private final Set<String> variableNames = new HashSet<String>();
	private final ValueProcessorImpl valueProcessor = new ValueProcessorImpl();
//	private DataReceiver dataReceiver;
	private NamedValueReceiver namedValueReceiver;

	public CollectLiteral() {
		super();
	}

	public CollectLiteral(final NamedValueReceiver metamorph) {
		super();
		setNamedValueReceiver(metamorph);
	}

	@Override
	protected void emit() {
		final String name = StringUtil.format(getName(), variables);
		String value = StringUtil.format(getValue(), variables);

		value = valueProcessor.applyFunctions(value);
		if (value == null) {
			return;
		}
		namedValueReceiver.receive(name, value, getRecordCount(), getEntityCount());
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
	protected void onAddData(final NamedValueSource data) {
		//data.setMode(Mode.VALUE);
		
		if (data.getName() == null) {
			data.setName(String.valueOf(getDataCount()));
		}
		variableNames.add(data.getName());
	}

	@Override
	protected void clear() {
		variables.clear();
	}

	@Override
	public void addFunction(final Function function) {
		valueProcessor.addFunction(function);
	}

	@Override
	public void setNamedValueReceiver(final NamedValueReceiver namedValueReceiver) {
		this.namedValueReceiver = namedValueReceiver;
		
	}
}
