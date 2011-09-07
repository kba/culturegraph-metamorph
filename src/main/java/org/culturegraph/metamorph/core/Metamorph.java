package org.culturegraph.metamorph.core;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.culturegraph.metamorph.streamreceiver.StreamReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class Metamorph implements StreamReceiver, KeyValueStoreAggregator, DataReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(Metamorph.class);

	private static final String ENTITIES_NOT_BALANCED = "Entity starts and ends are not balanced";

	private static final char DEFUALT_ENTITY_MARKER = '.';

	private static final char FEEDBACK_CHAR = '@';

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

	private char entityMarker = DEFUALT_ENTITY_MARKER;

	public void setEntityMarker(final char entityMarker) {
		this.entityMarker = entityMarker;
	}

	public void setErrorHandler(final MetamorphErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	protected void registerDataSource(final Data entityHandler, final String path) {
		assert entityHandler != null && path != null;

		List<Data> matchingDataSources = dataSources.get(path);
		if (matchingDataSources == null) {
			matchingDataSources = new LinkedList<Data>();
			dataSources.put(path, matchingDataSources);
		}
		matchingDataSources.add(entityHandler);
	}

	@Override
	public void startRecord() {
		++recordCount;
		entityCountStack.add(Integer.valueOf(entityCount));
		outputStreamReceiver.startRecord();
		if (LOG.isTraceEnabled()) {
			LOG.trace("#" + recordCount);
		}
	}

	@Override
	public void endRecord() {
		entityCount = 0;
		entityCountStack.removeLast();
		if (entityCountStack.size() != 0) {
			throw new MetamorphException(ENTITIES_NOT_BALANCED);
		}

		outputStreamReceiver.endRecord();
	}

	@Override
	public void startEntity(final String name) {
		++entityCount;
		entityCountStack.add(Integer.valueOf(entityCount));
		entityStack.add(name);
		entityPath.append(name + entityMarker);
		if (LOG.isTraceEnabled()) {
			LOG.trace(entityCount + "> " + entityPath.toString() + " (" + entityStack.size() + ")");
		}
		final String toEntity = entityMap.get(name);
		if (toEntity != null) {
			outputStreamReceiver.startEntity(toEntity);
		}
	}

	@Override
	public void endEntity() {
		if (LOG.isTraceEnabled()) {
			LOG.trace("< " + entityPath.toString());
		}

		final int end = entityPath.length();
		try {
			entityPath.delete(end - entityStack.getLast().length() - 1, end);

			final String name = entityStack.removeLast();
			entityCountStack.removeLast();

			final List<EntityEndListener> matchingListeners = entityEndListeners.get(name);
			if (null != matchingListeners) {
				for (EntityEndListener listener : matchingListeners) {
					listener.onEntityEnd(name);
				}
			}

			final String toEntity = entityMap.get(name);
			if (toEntity != null) {
				outputStreamReceiver.endEntity();
			}

		} catch (NoSuchElementException exc) {
			throw new MetamorphException(ENTITIES_NOT_BALANCED, exc);
		}
	}

	@Override
	public void literal(final String name, final String value) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("\t- " + name + "=" + value);
		}

		final String path = entityPath.toString() + name;
		dispatch(path, value);

	}

	private void dispatch(final String key, final String value) {
		final List<Data> matchingReceiver = dataSources.get(key);

		if (null != matchingReceiver) {
			for (Data receiver : matchingReceiver) {
				if (entityCountStack.isEmpty()) {
					throw new IllegalStateException("Cannot receive literals outside of records");
				}
				try {
					receiver.data(key, value, recordCount, entityCountStack.getLast().intValue());
				} catch (MetamorphException e) {
					errorHandler.error(e);
				}
			}
		}
	}

	/**
	 * @param streamReceiver
	 *            the outputHandler to set
	 */
	protected void setOutputStreamReceiver(final StreamReceiver streamReceiver) {

		if (streamReceiver == null) {
			throw new IllegalArgumentException("'streamReceiver' must not be null");
		}
		this.outputStreamReceiver = streamReceiver;
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
		if (keyValueStore == null) {
			return null;
		}
		return keyValueStore.get(key);
	}

	/**
	 * @param mapName
	 * @param keyValueStore
	 */
	public void addKeyValueStore(final String mapName, final KeyValueStore keyValueStore) {
		keyValueStores.put(mapName, keyValueStore);
	}

	@Override
	public void data(final String name, final String value, final int recordCount, final int entityCount) {
		if (name == null || value == null) {
			LOG.warn("Empty data received. This is not suposed to happen. Please file a bugreport");
		}else{
			if(name.length()!=0 && name.charAt(0)==FEEDBACK_CHAR){
				dispatch(name, value);
			}else{
				outputStreamReceiver.literal(name, value);
			}
		}
	}

	/**
	 * @param from
	 * @param to
	 */
	protected void addEntityMapping(final String from, final String toParam) {
		entityMap.put(from, toParam);
	}

	protected void addEntityEndListener(final EntityEndListener entityEndListener, final String entityName) {
		assert entityEndListener != null && entityName != null;

		List<EntityEndListener> matchingListeners = entityEndListeners.get(entityName);
		if (matchingListeners == null) {
			matchingListeners = new LinkedList<EntityEndListener>();
			entityEndListeners.put(entityName, matchingListeners);
		}
		matchingListeners.add(entityEndListener);
	}
}
