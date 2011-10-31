package org.culturegraph.metamorph.stream;

import org.culturegraph.metamorph.types.ListMap;

/**
 * Collects the received results in a {@link ListMap}.
 * 
 * @author Markus Michael Geipel
 */
public final class ListMapCollector extends DefaultStreamReceiver {

	private ListMap<String, String> listMap;
	
	public ListMapCollector() {
		super();
		listMap = new ListMap<String, String>();
	}	
	
	public ListMapCollector(final ListMap<String,String> listMap){
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
	public void startRecord(){
		listMap.clear();
	}
	
	@Override
	public void literal(final String name, final String value) {
		listMap.put(name, value);
	}
}
