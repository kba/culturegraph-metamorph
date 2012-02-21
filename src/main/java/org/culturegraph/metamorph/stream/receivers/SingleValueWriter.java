package org.culturegraph.metamorph.stream.receivers;

import java.util.Collection;

import org.culturegraph.metamorph.stream.Collector;
import org.culturegraph.metamorph.stream.StreamReceiver;

/**
 * just records the value of the last received literal.
 * @author Markus Michael Geipel
 *
 */
public final class SingleValueWriter implements StreamReceiver, Collector<String> {

	private String value = "";
	private Collection<String> collection;

	
	public SingleValueWriter() {
		collection=null;
	}
	
	public SingleValueWriter(final Collection<String> collection) {
		this.collection = collection;
	}
	
	@Override
	public void startRecord(final String identifier) {
		this.setValue("");

	}

	@Override
	public void endRecord() {
		if(collection!=null){
			collection.add(value);
		}
	}

	@Override
	public void startEntity(final String name) {
		// nothing to do
	}

	@Override
	public void endEntity() {
		// nothing to do
	}

	@Override
	public void literal(final String name, final String value) {
		this.setValue(value);
	}

	/**
	 * @return collected value, if nothing collected return ""
	 */
	public String getValue() {
		return value;
	}

	private void setValue(final String value) {
		this.value = value;
	}

	@Override
	public Collection<String> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(final Collection<String> collection) {
		this.collection = collection;		
	}

}
