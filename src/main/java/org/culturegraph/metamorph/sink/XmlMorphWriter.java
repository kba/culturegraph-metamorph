package org.culturegraph.metamorph.sink;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.culturegraph.metamorph.core.Metamorph;
import org.culturegraph.metastream.annotation.Description;
import org.culturegraph.metastream.annotation.In;
import org.culturegraph.metastream.annotation.Out;
import org.culturegraph.metastream.framework.ObjectReceiver;
import org.culturegraph.metastream.framework.StreamPipe;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.metastream.sink.XmlWriter;


/**
 * applies a Metamorph transformation and writes the result to xml
 * @author Markus Michael Geipel
 *
 */
@Description("applies a Metamorph transformation and writes the result to xml")
@In(StreamReceiver.class)
@Out(String.class)
public final class XmlMorphWriter implements StreamPipe<ObjectReceiver<String>> {
	
	private final XmlWriter xmlWriter;
	private final Metamorph metamorph;
	
	public XmlMorphWriter(final String morphDef) throws ParserConfigurationException, TransformerException {
		super();
		metamorph = new Metamorph(morphDef);
		xmlWriter = new XmlWriter();
		xmlWriter.configure(metamorph);
		metamorph.setReceiver(xmlWriter);
	}
	
	public void setRecordTag(final String tag){
		xmlWriter.setRecordTag(tag);
	}
	
	@Override
	public void startRecord(final String identifier) {
		metamorph.startRecord(identifier);
	}
	@Override
	public void endRecord() {
		metamorph.endRecord();
	}
	@Override
	public void startEntity(final String name) {
		metamorph.startEntity(name);
	}
	@Override
	public void endEntity() {
		metamorph.endEntity();
	}
	@Override
	public void literal(final String name, final String value) {
		metamorph.literal(name, value);
	}
	@Override
	public void resetStream() {
		metamorph.resetStream();
	}
	@Override
	public void closeStream() {
		metamorph.closeStream();
	}

	@Override
	public <R extends ObjectReceiver<String>> R setReceiver(final R receiver) {
		xmlWriter.setReceiver(receiver);
		return receiver;
	}


}
