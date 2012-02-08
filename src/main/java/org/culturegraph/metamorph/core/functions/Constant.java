package org.culturegraph.metamorph.core.functions;

/**
 * @author Markus Michael Geipel
 */
public final class Constant extends AbstractSimpleStatelessFunction {

	private String constValue;

	@Override
	public String process(final String value) {
			return constValue;
	}

	/**
	 * @param string
	 */
	public void setValue(final String string) {
		this.constValue = string;
	}
}
