/**
 * 
 */
package org.culturegraph.metamorph.core.functions;

import java.util.ArrayList;
import java.util.List;

import org.culturegraph.metamorph.core.EntityEndIndicator;
import org.culturegraph.metamorph.core.EntityEndListener;
import org.culturegraph.metamorph.core.NamedValueReceiver;
import org.culturegraph.metamorph.core.NamedValueSource;

/**
 * @author Markus Michael Geipel
 * 
 */
public final class Buffer extends AbstractFunction implements EntityEndListener {

	private final List<Receipt> receipts = new ArrayList<Receipt>();

	private String flushWith = EntityEndIndicator.RECORD_KEYWORD;

	@Override
	public void receive(final String name, final String value, final NamedValueSource source, final int recordCount,
			final int entityCount) {
		receipts.add(new Receipt(name, value, source, recordCount, entityCount));

	}

	@Override
	public void onEntityEnd(final String name, final int recordCount, final int entityCount) {
		for (Receipt receipt : receipts) {
			receipt.send(getNamedValueReceiver());
			//getNamedValueReceiver().receive(receipt.name, receipt.value, receipt.source, receipt.recordCount, receipt.entityCount);
		}
	}

	@Override
	public void setEntityEndIndicator(final EntityEndIndicator indicator) {
		indicator.addEntityEndListener(this, flushWith);
	}
	
	public void setFlushWith(final String flushWith){
		this.flushWith = flushWith;
	}

	/**
	 * buffer element
	 */
	private static final class Receipt {
		private final String name;
		private final String value;
		private final NamedValueSource source;
		private final int recordCount;
		private final int entityCount;
			
		protected Receipt(final String name, final String value, final NamedValueSource source, final int recordCount,
				final int entityCount) {
			this.name = name;
			this.value = value;
			this.source = source;
			this.recordCount = recordCount;
			this.entityCount = entityCount;
		}
		
		protected void send(final NamedValueReceiver receiver){
			receiver.receive(name, value, source, recordCount, entityCount);
		}
	}
}
