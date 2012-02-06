package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * Corresponds to the <code>&lt;collect-literal&gt;</code> tag.
 * 
 * @author Markus Michael Geipel
 */
public final class Concat extends AbstractCollect{
	private final StringBuilder builder = new StringBuilder();
	private String prefix = "";
	private String postfix = "";
	private String delimiter = "";
	
	public Concat(final Metamorph metamorph) {
		super(metamorph);
		setNamedValueReceiver(metamorph);
	}
	
	
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	public void setPostfix(final String postfix) {
		this.postfix = postfix;
	}

	public void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	protected void emit() {
		getNamedValueReceiver().receive(getName(), prefix + getConcat() + postfix, this, getRecordCount(), getEntityCount());
	}


	private String getConcat() {
		return builder.substring(0, builder.length()-delimiter.length());
	}

	@Override
	protected boolean isComplete() {
		return false;
	}

	@Override
	protected void receive(final String name, final String value, final NamedValueSource source) {
		builder.append(value);
		builder.append(delimiter);
	}


	@Override
	protected void clear() {
		builder.delete(0, builder.length());
	}

}
