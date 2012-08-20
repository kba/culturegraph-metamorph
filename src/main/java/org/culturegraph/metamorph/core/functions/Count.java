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
public final class Count extends AbstractStatefulFunction{

	private int count;
	//private boolean receivedData;

	@Override
	public String process(final String value) {
		//receivedData = true;
		++count;
		return String.valueOf(count);
	}

	@Override
	protected void reset() {
		count = 0;
	}

	@Override
	protected boolean doResetOnEntityChange() {
		return false;
	}

//	@Override
//	public void setEntityEndIndicator(final EntityEndIndicator indicator) {
//		indicator.addEntityEndListener(this, EntityEndIndicator.RECORD_KEYWORD);
//	}

//	@Override
//	public void onEntityEnd(final String name, final int recordCount, final int entityCount) {
//		if (receivedData) {
//			receivedData = false;
//			getNamedValueReceiver().receive(getLastName(), String.valueOf(count), getNamedValueSource(),
//					getRecordCount(), getEntityCount());
//		}
//
//	}
}
