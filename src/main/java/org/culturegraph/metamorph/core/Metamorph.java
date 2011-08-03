package org.culturegraph.metamorph.core;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Metamorph implements StreamReceiver, KeyValueStoreAggregator, DataReceiver{

	private static final Logger LOG = LoggerFactory
			.getLogger(Metamorph.class);
	
	private final Map<String, List<Data>> dataSources = new HashMap<String, List<Data>>();
	private final Map<String, List<EntityEndListener>> entityEndListeners = new HashMap<String, List<EntityEndListener>>();
	private final Map<String, String> entityMap = new HashMap<String, String>();
	
	private final Map<String, KeyValueStore> keyValueStores = new HashMap<String, KeyValueStore>();
	private final Deque<String> entityStack = new LinkedList<String>();
	private final StringBuilder entityPath = new StringBuilder();
	private final Deque<Integer> entityCountStack = new LinkedList<Integer>();
	
	private StreamReceiver outputStreamReceiver;
	private MetamorphErrorHandler errorHandler = new MetamorphErrorHandler() {
		@Override
		public void error(final Exception exception) {
			throw new MetamorphException("An unhandled exception occured", exception);
		}
	};
	
	private int recordCount;
	private int entityCount;
	
	public void setErrorHandler(final MetamorphErrorHandler errorHandler){
		this.errorHandler = errorHandler;
	}

	protected void registerDataSource(final Data entityHandler, final String path){
		assert entityHandler!=null && path !=null;
		
		List<Data> matchingDataSources = dataSources.get(path);
		if(matchingDataSources==null){
			matchingDataSources = new LinkedList<Data>();
			dataSources.put(path, matchingDataSources);
		}
		matchingDataSources.add(entityHandler);
	}

	public void startRecord() {
		++recordCount;
		entityCountStack.add(Integer.valueOf(entityCount));
		outputStreamReceiver.startRecord();
		if(LOG.isTraceEnabled()){
			LOG.trace("#" + recordCount);
		}
	}


	public void endRecord() {
		entityCount=0;
		outputStreamReceiver.endRecord();
	}


	public void startEntity(final String name) {
		++entityCount;
		entityCountStack.add(Integer.valueOf(entityCount));
		entityStack.add(name);
		entityPath.append(name+".");
		if(LOG.isTraceEnabled()){
			LOG.trace(entityCount + "> " + entityPath.toString() + " (" + entityStack.size() + ")");
		}
		final String toEntity = entityMap.get(name);
		if(toEntity!=null){
			outputStreamReceiver.startEntity(toEntity);
		}
	}


	public void endEntity() {
		if(LOG.isTraceEnabled()){
			LOG.trace("< " + entityPath.toString());
		}
		
		final int end = entityPath.length();
		entityPath.delete(end-entityStack.getLast().length()-1, end);
		final String name = entityStack.removeLast();
		entityCountStack.removeLast();
		
		final List<EntityEndListener> matchingListeners = entityEndListeners.get(name);
		if(null!=matchingListeners){
			for (EntityEndListener listener : matchingListeners) {
				listener.onEntityEnd(name);
			}
		}
		
		
		final String toEntity = entityMap.get(name);
		if(toEntity!=null){
			outputStreamReceiver.endEntity();
		}
	}


	public void literal(final String name, final String value) {
		if(LOG.isTraceEnabled()){
			LOG.trace("\t- " + name + "=" + value);
		}
		
		final List<Data> matchingReceiver = dataSources.get(entityPath.toString()+name);
		if(null!=matchingReceiver){
			for (Data receiver : matchingReceiver) {
				if(entityCountStack.isEmpty()){
					throw new IllegalStateException("Cannot receive literals outside of records");
				}else{
					try{
						receiver.data(value, recordCount, entityCountStack.getLast());
					}catch(MetamorphException e){
						errorHandler.error(e);
					}
				}
			}
		}
	}
	
	/**
	 * @param outputHandler the outputHandler to set
	 */
	protected void setOutputStreamReceiver(final StreamReceiver outputHandler) {
		Assert.notNull(outputHandler, "'outputHandler' must not be null");
		this.outputStreamReceiver = outputHandler;
	}
	
	/**
	 * @return the outputStreamReceiver
	 */
	protected StreamReceiver getOutputStreamReceiver() {
		return outputStreamReceiver;
	}

	@Override
	public String getValue(final String source, final String key) {
		final KeyValueStore keyValueStore = keyValueStores.get(source);
		if(keyValueStore==null){
			return null;
		}else{
			return keyValueStore.get(key);
		}
	}

	/**
	 * @param mapName
	 * @param keyValueStore
	 */
	public void addKeyValueStore(final String mapName, final KeyValueStore keyValueStore) {
		keyValueStores.put(mapName, keyValueStore);
	}


	@Override
	public void data(final String name, final String value, final DataSender sender, final int recordCount, final int entityCount) {
		if(name!=null && value!=null){
			outputStreamReceiver.literal(name, value);
		}
	}

	/**
	 * @param from
	 * @param to
	 */
	protected void addEntityMapping(final String from, final String toParam) {
		entityMap.put(from, toParam);
	}
	
	protected void addEntityEndListener(final EntityEndListener entityEndListener, final String entityName){
		assert entityEndListener!=null && entityName !=null;
		
		List<EntityEndListener> matchingListeners = entityEndListeners.get(entityName);
		if(matchingListeners==null){
			matchingListeners = new LinkedList<EntityEndListener>();
			entityEndListeners.put(entityName, matchingListeners);
		}
		matchingListeners.add(entityEndListener);
	}
}
