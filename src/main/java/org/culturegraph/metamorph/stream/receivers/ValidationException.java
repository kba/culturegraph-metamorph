/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -4446333800830109742L;

	public ValidationException(final String message) {
		super(message);
	}
}
