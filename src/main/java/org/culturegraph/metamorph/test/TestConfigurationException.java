/**
 * 
 */
package org.culturegraph.metamorph.test;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public class TestConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -4980848442374614262L;
	
	/**
	 * @param message
	 */
	TestConfigurationException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	TestConfigurationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	TestConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
