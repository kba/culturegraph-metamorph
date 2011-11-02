package org.culturegraph.metamorph.readers;

import java.util.List;
import java.util.Map.Entry;

import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.stream.StreamSender;
import org.culturegraph.metamorph.types.ListMap;

public final class ListMapReader implements StreamSender {
	private StreamReceiver streamReceiver;

	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		this.streamReceiver = streamReceiver;
	}
	
	public void read(final ListMap<String, String> listMap){
		read(listMap, streamReceiver);
	}
	
	public static void read(final ListMap<String, String> listMap, final StreamReceiver streamReceiver){
		streamReceiver.startRecord();
		for(Entry<String, List<String>> entry: listMap.entrySet()){
			final String name = entry.getKey();
			for(String value:entry.getValue()){
				streamReceiver.literal(name, value);
			}
		}
		streamReceiver.endRecord();
	}
}
