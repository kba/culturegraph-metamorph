package org.culturegraph.metamorph.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.culturegraph.metastream.annotation.Description;
import org.culturegraph.metastream.annotation.In;
import org.culturegraph.metastream.annotation.Out;
import org.culturegraph.metastream.framework.StreamPipe;
import org.culturegraph.metastream.framework.StreamReceiver;
import org.culturegraph.util.MultiMap;
import org.culturegraph.util.SimpleMultiMap;

/**
 * Transforms a data stream send via the {@link StreamReceiver} interface. Use
 * {@link MetamorphBuilder} to create an instance based on an xml description
 * 
 * @author Markus Michael Geipel
 */
@Description("applies a metamorph transformation to the event stream. Morph definition is given in brackets.")
@In(StreamReceiver.class)
@Out(StreamReceiver.class)
public final class Metamorph implements StreamPipe<StreamReceiver>, NamedValueReceiver, SimpleMultiMap, EntityEndIndicator {

	public static final String ID_NAME = "_id";
	public static final String ELSE_KEYWORD = "_else";
	public static final char FEEDBACK_CHAR = '@';
	public static final String METADATA = "__meta";
	public static final String WILDCARD = "*";


	private static final String ENTITIES_NOT_BALANCED = "Entity starts and ends are not balanced";
	private static final char DEFAULT_ENTITY_MARKER = '.';

	private final Registry<Data> dataRegistry = new WildcardRegistry<Data>();
	private final List<Data> elseSources = new ArrayList<Data>();
	private final Registry<EntityEndListener> entityEndListenerRegistry = new WildcardRegistry<EntityEndListener>();
	// private final Map<String, String> entityMap = new HashMap<String,
	// String>();
	private final SimpleMultiMap multiMap = new MultiMap();

	private final Deque<String> entityStack = new LinkedList<String>();
	private final StringBuilder entityPath = new StringBuilder();
	private String currentEntityPath = "";

	private final Deque<Integer> entityCountStack = new LinkedList<Integer>();
	private int entityCount;
	private int currentEntityCount;

	private StreamReceiver outputStreamReceiver;
	private MetamorphErrorHandler errorHandler = new DefaultErrorHandler();
	private int recordCount;
	private char entityMarker = DEFAULT_ENTITY_MARKER;

	protected Metamorph() {
		// package private
	}
	
	public Metamorph(final String morphDef) {
		final MetamorphBuilder builder = new MetamorphBuilder(this);
		builder.buildIntern(morphDef);
	}
	
	protected void setEntityMarker(final char entityMarker) {
		this.entityMarker = entityMarker;
	}

	public void setErrorHandler(final MetamorphErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	protected void registerData(final Data data) {

		final String path = data.getSource();

		if (ELSE_KEYWORD.equals(path)) {
			elseSources.add(data);
		} else {
			dataRegistry.register(path, data);
		}
	}

	@Override
	public void startRecord(final String identifier) {
		entityCountStack.clear();
		entityStack.clear();
		currentEntityPath = "";
		if (entityPath.length() != 0) {
			entityPath.delete(0, entityPath.length());
		}
		entityCount = 0;
		currentEntityCount = 0;

		++recordCount;
		recordCount %= Integer.MAX_VALUE;

		entityCountStack.add(Integer.valueOf(entityCount));

		final String identifierFinal;
		if (identifier == null) {
			identifierFinal = String.valueOf(recordCount);
		} else {
			identifierFinal = identifier;
		}
		outputStreamReceiver.startRecord(identifierFinal);
		dispatch(ID_NAME, identifierFinal, null);
	}

	@Override
	public void endRecord() {

		notifyEntityEndListeners(RECORD_KEYWORD);

		outputStreamReceiver.endRecord();
		entityCountStack.removeLast();
		if (!entityCountStack.isEmpty()) {
			throw new IllegalStateException(ENTITIES_NOT_BALANCED);
		}
		currentEntityPath = "";
	}

	@Override
	public void startEntity(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("Entity name must not be null.");
		}

		++entityCount;
		currentEntityCount = entityCount;
		entityCountStack.push(Integer.valueOf(entityCount));

		entityStack.push(name);
		entityPath.append(name);
		dispatch(entityPath.toString(), name, null);

		entityPath.append(entityMarker);
		currentEntityPath = entityPath.toString();

		// final String toEntity = entityMap.get(name);
		// if (toEntity != null) {
		// outputStreamReceiver.startEntity(toEntity);
		// }
	}

	@Override
	public void endEntity() {

		try {
			final int end = entityPath.length();
			final String name = entityStack.pop();
			currentEntityCount = entityCountStack.pop().intValue();

			entityPath.delete(end - name.length() - 1, end);

			currentEntityPath = entityPath.toString();

			notifyEntityEndListeners(name);

			// final String toEntity = entityMap.get(name);
			// if (toEntity != null) {
			// outputStreamReceiver.endEntity();
			// }

		} catch (NoSuchElementException exc) {
			throw new IllegalStateException(ENTITIES_NOT_BALANCED + ": " + exc.getMessage(), exc);
		}
	}

	private void notifyEntityEndListeners(final String name) {
		final List<EntityEndListener> matchingListeners = entityEndListenerRegistry.get(name);

		for (EntityEndListener listener : matchingListeners) {
			listener.onEntityEnd(name, recordCount, currentEntityCount);
		}

	}

	@Override
	public void literal(final String name, final String value) {
		dispatch(currentEntityPath + name, value, elseSources);
	}
	
	@Override
	public void resetStream() {
		// TODO: Implement proper reset handling
		outputStreamReceiver.resetStream();
	}
	
	@Override
	public void closeStream() {
		outputStreamReceiver.closeStream();
	}
	
	/**
	 * @param path
	 * @param value
	 * @param fallback
	 */
	private void dispatch(final String path, final String value, final List<Data> fallback) {
		final List<Data> matchingData = findMatchingData(path, fallback);
		if (null != matchingData) {
			send(path, value, matchingData);
		}
	}

	/**
	 * @param path
	 * @param fallback
	 * @return
	 */
	private List<Data> findMatchingData(final String path, final List<Data> fallback) {
		final List<Data> matchingData = dataRegistry.get(path);
		if (matchingData == null || matchingData.isEmpty()) {
			return fallback;
		}
		return matchingData;
	}

	/**
	 * @param key
	 * @param value
	 * @param dataList
	 *            destination
	 */
	private void send(final String key, final String value, final List<Data> dataList) {
		for (Data data : dataList) {
			try {
				data.receive(key, value, null, recordCount, currentEntityCount);
			} catch (RuntimeException e) {
				errorHandler.error(e);
			}
		}
	}

	/**
	 * @param streamReceiver
	 *            the outputHandler to set
	 */
	@Override
	public <R extends StreamReceiver> R setReceiver(final R streamReceiver) {
		if (streamReceiver == null) {
			throw new IllegalArgumentException("'streamReceiver' must not be null");
		}
		this.outputStreamReceiver = streamReceiver;
		return streamReceiver;
	}

	/**
	 * @return the outputStreamReceiver
	 */
	public StreamReceiver getStreamReceiver() {
		return outputStreamReceiver;
	}

	@Override
	public void receive(final String name, final String value, final NamedValueSource source, final int recordCount,
			final int entityCount) {
		if (null == name) {
			throw new IllegalArgumentException(
					"encountered literal with name='null'. This indicates a bug in a function or a collector.");
		}

		if (name.length() != 0 && name.charAt(0) == FEEDBACK_CHAR) {
			dispatch(name, value, null);
		} else {
			outputStreamReceiver.literal(name, value);
		}

	}

	// /**
	// * @param from
	// * @param to
	// */
	// protected void addEntityMapping(final String from, final String toParam)
	// {
	// throw new NotImplementedException();
	// //entityMap.put(from, toParam);
	// }

	@Override
	public void addEntityEndListener(final EntityEndListener entityEndListener, final String entityName) {
		entityEndListenerRegistry.register(entityName, entityEndListener);
	}


	@Override
	public Map<String, String> getMap(final String mapName) {
		return multiMap.getMap(mapName);
	}

	@Override
	public String getValue(final String mapName, final String key) {
		return multiMap.getValue(mapName, key);
	}

	@Override
	public Map<String, String> putMap(final String mapName, final Map<String, String> map) {
		return multiMap.putMap(mapName, map);
	}

	@Override
	public String putValue(final String mapName, final String key, final String value) {
		return multiMap.putValue(mapName, key, value);
	}

	@Override
	public Collection<String> getMapNames() {
		return multiMap.getMapNames();
	}

}
