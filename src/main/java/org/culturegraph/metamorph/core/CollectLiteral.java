package org.culturegraph.metamorph.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.Data.Mode;

/**
 *  Corresponds to the <code>&lt;collect-literal&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
final class CollectLiteral extends AbstractCollect {

	private final Map<String, String> variables = new HashMap<String, String>();
	private final Set<String> variableNames = new HashSet<String>();

	private static String format(final String format,
			final Map<String, String> variables) {
		String result = format;
		for (Entry<String, String> variable : variables.entrySet()) {
			final Pattern pattern = Pattern.compile("\\$\\{"
					+ variable.getKey() + "\\}");
			result = pattern.matcher(result).replaceAll(variable.getValue());
		}
		return result;
	}

	@Override
	protected void emit() {
		final String name = format(getName(), variables);
		final String value = format(getValue(), variables);
		getStreamReceiver().literal(name, value);
	}
	
	@Override
	public void onEntityEnd(final String entityName) {
		variables.put(".*?", "");
		emit();
	};

	@Override
	protected boolean isComplete() {
		return variables.size() == variableNames.size();
	};

	@Override
	protected void receive(final String name, final String value) {
		variables.put(name, value);
	};

	@Override
	protected void onAddData(final Data data) {
		data.setMode(Mode.AS_VALUE);
		if (data.getDefaultName() == null) {
			data.setDefaultName(String.valueOf(getDataCount()));
		}
		variableNames.add(data.getDefaultName());
	}

	@Override
	protected void clear() {
		variables.clear();
	};
}
