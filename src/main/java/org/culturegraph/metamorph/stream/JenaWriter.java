package org.culturegraph.metamorph.stream;

import java.util.Map;


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

	private final Model model;
	private Resource currentResource;
	
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
	public void startRecord() {
		currentResource = model.createResource("hula");

	}

	@Override
	public void endRecord() {
		// nothing
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
}
