package org.culturegraph.metamorph.multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * loads text files into a {@link Map}. Keys and values are tab-separated. The
 * rationale is to load files generated with hadoop which contain statistics.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class MultimapMapProviderImpl implements MultiMapProvider{
	private static final Logger LOG = LoggerFactory.getLogger(MultimapMapProviderImpl.class);

	private final Map<String, Map<String, String>> multiMap = new HashMap<String, Map<String, String>>();
	
	private MultimapMapProviderImpl(final String... fileNames) throws IOException {
		load(multiMap, fileNames);
	}

	public static void load(final Map<String, Map<String, String>> multiMap, final String... fileNames) throws IOException {
		for (String fileName : fileNames) {
			final File file = new File(fileName);
			if (file.isDirectory()) {
				final File[] files = file.listFiles();
				for (File file2 : files) {
					load(file2, multiMap);
				}
			} else {
				load(file, multiMap);
			}
		}
	}

	private static void load(final File file, final Map<String, Map<String, String>> multiMap) throws IOException {

		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
				"UTF-8"));

		String line = bufferedReader.readLine();
		while (line != null) {
			if (!line.isEmpty()) {
				addRecord(line, multiMap);
			}
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
		LOG.info(file.getName() + " loaded into map.");
	}

	private static void addRecord(final String line, final Map<String, Map<String, String>> multiMap) {
		
		final String[] parts = line.split("\t");
		if(parts.length!=2){
			throw new IllegalArgumentException("Format error in line '" + line + "'.");
		}
		final String mapName;
		final String key;
		final int split = parts[0].indexOf('$');
		if(split<0){
			mapName = "";
			key = parts[0];
		}else{
			mapName = parts[0].substring(0, split);
			key = parts[0].substring(split+1);
		}
		put(mapName, key, parts[1], multiMap);
	}

	public static void put(final String mapName, final String key, final String value, final Map<String, Map<String, String>> multiMap){
		Map<String, String> map = multiMap.get(mapName);
		if (map == null) {
			map = new HashMap<String, String>();
			multiMap.put(mapName, map);
		}
		map.put(key, value);
	}

	@Override
	public Map<String, String> getMap(final String mapName) {
		final Map<String, String> map = multiMap.get(mapName);
		if (map == null) {
			return Collections.emptyMap();
		}
		return map;
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
	
	@Override
	public String toString() {
		return multiMap.toString();
	}
}
