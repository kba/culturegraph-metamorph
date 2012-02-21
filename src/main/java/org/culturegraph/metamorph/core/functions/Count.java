/**
 * 
 */
package org.culturegraph.metamorph.core.functions;

import org.culturegraph.metamorph.core.EntityEndIndicator;
import org.culturegraph.metamorph.core.EntityEndListener;



/**
 * @author Markus Michael Geipel
 *
 */
public final class Count extends AbstractStatefulFunction implements EntityEndListener {

	private int count;
	
	@Override
	public String process(final String value) {
		++count;
		return null;
	}
	
	@Override
	protected void reset() {
		count=0;
	}

	@Override
	protected boolean doResetOnEntityChange() {
		return false;
	}
	
	@Override
	public void setEntityEndIndicator(final EntityEndIndicator indicator) {
		indicator.addEntityEndListener(this, EntityEndIndicator.RECORD_KEYWORD);
	}

	@Override
	public void onEntityEnd(final String name, final int recordCount, final int entityCount) {
		getNamedValueReceiver().receive(getLastName(), String.valueOf(count), getNamedValueSource(), getRecordCount(), getEntityCount());		
	}
}
