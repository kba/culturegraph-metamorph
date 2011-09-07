package org.culturegraph.metamorph.core;

/**
 * @author Markus Michael Geipel
 */
public final class Literal  implements Comparable<Literal>{
	private static final int MAGIC1 = 23;
	private static final int MAGIC2 = 31;
	private final String name;
	private final String value;
	private final int preCompHashCode;
	
	public Literal(final String name, final String value){
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
		if (obj instanceof Literal) {
			final Literal literal = (Literal) obj;
			return literal.preCompHashCode==preCompHashCode && literal.name.equals(name) && literal.value.equals(value);
		}
		return false;
	}

	@Override
	public int compareTo(final Literal literal) {
		final int first = name.compareTo(literal.name);
		if(first == 0){
			return value.compareTo(literal.value);
		}
		return first;
	}
	
	@Override
	public String toString() {
		return name + "=" + value;
	}
}