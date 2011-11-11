package org.culturegraph.metamorph.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.culturegraph.metamorph.stream.StreamReceiver;
import org.culturegraph.metamorph.stream.StreamSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms a data stream send via the {@link StreamReceiver} interface. Use
 * {@link MetamorphBuilder} to create an instance based on an xml description
 * 
 * @author Markus Michael Geipel
 */
public final class Metamorph implements StreamReceiver, StreamSender,DataReceiver, MultiMapProvider {

	
	
	private static final Logger LOG = LoggerFactory.getLogger(Metamorph.class);

	private static final String ENTITIES_NOT_BALANCED = "Entity starts and ends are not balanced";
	private static final char DEFAULT_ENTITY_MARKER = '.';
	private static final char FEEDBACK_CHAR = '@';

	private final Map<String, List<Data>> dataSources = new HashMap<String, List<Data>>();
	private final Map<String, List<EntityEndListener>> entityEndListeners = new HashMap<String, List<EntityEndListener>>();

	private final Map<String, String> entityMap = new HashMap<String, String>();

	private final Map<String, Map<String, String>> multiMap = new HashMap<String, Map<String, String>>();
	private final Deque<String> entityStack = new LinkedList<String>();
	private final StringBuilder entityPath = new StringBuilder();
	private final Deque<Integer> entityCountStack = new LinkedList<Integer>();

	private StreamReceiver outputStreamReceiver;
	private MetamorphErrorHandler errorHandler = new DefaultErrorHandler();

	private int recordCount;
	private int entityCount;

	private char entityMarker = DEFAULT_ENTITY_MARKER;

	protected Metamorph() {
		//keep constructor in package
	}
	
	protected void setEntityMarker(final char entityMarker) {
		this.entityMarker = entityMarker;
	}

	public void setErrorHandler(final MetamorphErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	protected void registerDataSource(final Data data, final String path) {
		assert data != null && path != null;

		List<Data> matchingDataSources = dataSources.get(path);
		if (matchingDataSources == null) {
			matchingDataSources = new ArrayList<Data>();
			dataSources.put(path, matchingDataSources);
		}
		matchingDataSources.add(data);
	}

	@Override
	public void startRecord(final String identifier) {
		entityCountStack.clear();
		entityStack.clear();

		++recordCount;
		entityCountStack.add(Integer.valueOf(entityCount));
		
		final String identifierFinal;
		if(identifier==null){
			identifierFinal = String.valueOf(recordCount);
		}else{
			identifierFinal = identifier;
		}
		outputStreamReceiver.startRecord(identifierFinal);
		literal(StreamReceiver.ID_NAME, identifierFinal);
		
		if (LOG.isTraceEnabled()) {
			LOG.trace("#" + recordCount);
		}
	}

	@Override
	public void endRecord() {

		outputStreamReceiver.endRecord();

		entityCount = 0;
		entityCountStack.removeLast();
		if (!entityCountStack.isEmpty()) {

			throw new IllegalMorphStateException(ENTITIES_NOT_BALANCED);
		}
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
			throw new IllegalMorphStateException(ENTITIES_NOT_BALANCED + ": " + exc.getMessage(), exc);
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
					throw new IllegalMorphStateException("Cannot receive literals outside of records");
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
	@Override
	public void setStreamReceiver(final StreamReceiver streamReceiver) {
		if (streamReceiver == null) {
			throw new IllegalArgumentException("'streamReceiver' must not be null");
		}
		this.outputStreamReceiver = streamReceiver;
	}

	/**
	 * @return the outputStreamReceiver
	 */
	protected StreamReceiver getStreamReceiver() {
		return outputStreamReceiver;
	}

	/**
	 * @param mapName
	 * @param keyValueStore
	 */
	public void addMap(final String mapName, final Map<String, String> map) {
		multiMap.put(mapName, map);
	}

	@Override
	public void data(final String name, final String value, final int recordCount, final int entityCount) {
		if (name == null || value == null) {
			LOG.warn("Empty data received. This is not suposed to happen. Please file a bugreport");
		} else {
			if (name.length() != 0 && name.charAt(0) == FEEDBACK_CHAR) {
				dispatch(name, value);
			} else {
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

	
	/**
	 * 
	 * @param mapName
	 * @return map corresponding to mapName. Never <code>null</code>. If there is no corresponding {@link Map}, and empty one is returned
	 */
	@Override
	public Map<String, String> getMap(final String mapName) {
		final Map<String, String> map = multiMap.get(mapName);
		if(map==null){
			return Collections.emptyMap();
		}
		return map;
	}

	public Map<String, Map<String, String>> getMultiMap() {
		return multiMap;
	}

	@Override
	public String getValue(final String mapName, final String key) {
		final Map<String, String> map = getMap(mapName);
		final String value = map.get(key);
		if (value == null) {
			return map.get(MultiMapProvider.DEFAULT_MAP_KEY);
		}
		return value;
	}
}
