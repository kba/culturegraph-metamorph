package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.types.ListMap;

/**
 * Collects the received results in a {@link ListMap}.
 * 
 * @author Markus Michael Geipel
 */
public final class ListMapWriter extends DefaultStreamReceiver {

	private ListMap<String, String> listMap;
	private String currentId;
	
	public ListMapWriter() {
		super();
		listMap = new ListMap<String, String>();
	}	
	
	public ListMapWriter(final ListMap<String,String> listMap){
		super();
		this.listMap = listMap;
	}
	
	public ListMap<String, String> getListMap() {
		return listMap;
	}

	public void setListMap(final ListMap<String, String> listMap) {
		this.listMap = listMap;
	}

	@Override
	public void startRecord(final String identifier){
		listMap.clear();
		currentId = identifier;
	}
	
	@Override
	public void literal(final String name, final String value) {
		listMap.put(name, value);
	}

	public String getCurrentId() {
		return currentId;
	}
	
	@Override
	public String toString() {
		return listMap.toString();
	}
}
