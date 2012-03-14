/**
 * 
 */
package org.culturegraph.metamorph.core.functions;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Markus Michael Geipel
 * 
 */
public final class Unique extends AbstractStatefulFunction{

	
	private static final String ENTITY = "entity";
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private final Set<String> set = new HashSet<String>();
	private boolean uniqueInEntity;
	private KeyGenerator keyGenerator = new KeyGenerator() {
		@Override
		public String createKey(final String name, final String value) {
			return name + "\0" + value;
		}
	};

	@Override
	public String process(final String value) {
		final String key = keyGenerator.createKey(getLastName(), value);
		if (set.contains(key)) {
			return null;
		}
		set.add(key);
		return value;
	}

	@Override
	protected void reset() {
		set.clear();
	}

	@Override
	protected boolean doResetOnEntityChange() {
		return uniqueInEntity;
	}

	public void setIn(final String scope) {
		uniqueInEntity = ENTITY.equals(scope);
	}

	public void setPart(final String part) {
		if (NAME.equals(part)) {
			keyGenerator = new KeyGenerator() {
				@Override
				public String createKey(final String name, final String value) {
					return name;
				}
			};
		} else if (VALUE.equals(part)) {
			keyGenerator = new KeyGenerator() {
				@Override
				public String createKey(final String name, final String value) {
					return value;
				}
			};
		}
	}

	/**
	 * to implement different uniqueness strategies
	 */
	private interface KeyGenerator {
		String createKey(String name, String value);
	}
}
