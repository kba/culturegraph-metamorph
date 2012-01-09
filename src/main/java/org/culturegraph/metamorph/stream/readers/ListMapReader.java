package org.culturegraph.metamorph.stream.readers;

import java.util.List;
import java.util.Map.Entry;

import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.stream.StreamSender;
import org.culturegraph.metamorph.types.ListMap;

public final class ListMapReader implements StreamSender {
	private StreamReceiver streamReceiver;

	@Override
	public <R extends StreamReceiver> R  setReceiver(final R streamReceiver) {
		this.streamReceiver = streamReceiver;
		return streamReceiver;
	}
	
	public void read(final ListMap<String, String> listMap){
		read(listMap, streamReceiver);
	}
	
	public static void read(final ListMap<String, String> listMap, final StreamReceiver streamReceiver){
		streamReceiver.startRecord(null);
		for(Entry<String, List<String>> entry: listMap.entrySet()){
			final String name = entry.getKey();
			for(String value:entry.getValue()){
				streamReceiver.literal(name, value);
			}
		}
		streamReceiver.endRecord();
	}
}
