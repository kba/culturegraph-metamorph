package org.culturegraph.metamorph.types;

/**
 * @author Markus Michael Geipel
 */
public final class NamedValue  implements Comparable<NamedValue>{
	private static final int MAGIC1 = 23;
	private static final int MAGIC2 = 31;
	private final String name;
	private final String value;
	private final int preCompHashCode;
	
	public NamedValue(final String name, final String value){
		this.name = name;
		this.value = value;
		int result = MAGIC1;
		result = MAGIC2 * result + value.hashCode();
		result = MAGIC2 * result + name.hashCode();
		preCompHashCode = result;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return preCompHashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof NamedValue) {
			final NamedValue namedValue = (NamedValue) obj;
			return namedValue.preCompHashCode==preCompHashCode && namedValue.name.equals(name) && namedValue.value.equals(value);
		}
		return false;
	}

	@Override
	public int compareTo(final NamedValue namedValue) {
		final int first = name.compareTo(namedValue.name);
		if(first == 0){
			return value.compareTo(namedValue.value);
		}
		return first;
	}
	
	@Override
	public String toString() {
		return name + ":" + value;
	}
}