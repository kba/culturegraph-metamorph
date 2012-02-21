/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class WellformednessException extends RuntimeException {

	private static final long serialVersionUID = 3427046328020964145L;

	public WellformednessException(final String message) {
		super(message);
	}

}
