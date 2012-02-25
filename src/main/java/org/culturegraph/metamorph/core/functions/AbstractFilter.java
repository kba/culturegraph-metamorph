package org.culturegraph.metamorph.core.functions;

/**
 * @author Markus Michael Geipel
 */
public abstract class AbstractFilter extends AbstractSimpleStatelessFunction {

	private String string;

	@Override
	public final String process(final String value) {
		if(accept(value)){
			return value;
		}
		return null;
	}
	
	protected abstract boolean accept(String value);

	protected final  String getString() {
		return string;
	}
	
	/**
	 * @param string
	 */
	public final void setString(final String string) {
		this.string = string;
	}
}
