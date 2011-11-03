package org.culturegraph.metamorph.rdf;

import java.util.Map;

import org.culturegraph.metamorph.stream.StreamReceiver;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This is unfinished code!
 * 
 * @author Markus Michael Geipel
 *
 */
public final class JenaWriter implements StreamReceiver {

	private static final int DEFAULT_BATCH_SIZE = 1000;
	private final Model model;
	private Resource currentResource;
	private int batchSize = DEFAULT_BATCH_SIZE;
	private long count;
	private BatchFinishedListener batchFinishedListener;
	
	
	public JenaWriter() {
		model = ModelFactory.createDefaultModel();
	}
	
	public JenaWriter(final Model model) {
		this.model = model;
	}
	
	public Model getModel(){
		return model;
	}
	
	public void setNsPrefixes(final Map<String, String> prefixes){
		model.setNsPrefixes(prefixes);
	}
	
	@Override
	public void startRecord(final String identifier) {
		currentResource = model.createResource("http://" + identifier);

	}

	@Override
	public void endRecord() {
		++count;
		if(count%batchSize==0){
			if(null!=batchFinishedListener){
				batchFinishedListener.onBatchFinished(model);
			}
			model.removeAll();
		}
	}

	@Override
	public void startEntity(final String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void literal(final String name, final String value) {
		if(!name.isEmpty() && name.charAt(0)=='+'){
			currentResource.addProperty(model.createProperty(name), model.createResource(value));
		}else{
			currentResource.addProperty(model.createProperty(name), value);
		}
	}

	/**
	 * @param batchSize
	 */
	public void setBatchSize(final int batchSize) {
		this.batchSize = batchSize;
	}

	/**
	 * @return
	 */
	public int getBatchSize() {
		return batchSize;
	}
	
	/**
	 * @param batchFinishedListener
	 */
	public void setBatchFinishedListener(final BatchFinishedListener batchFinishedListener) {
		this.batchFinishedListener = batchFinishedListener;
	}


	
	public interface BatchFinishedListener {
		void onBatchFinished(final Model model);
	}
}
