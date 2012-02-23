package org.culturegraph.metamorph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.culturegraph.metamorph.multimap.SimpleMultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * loads text files into a {@link SimpleMultiMap}. Keys and values are tab-separated. The
 * rationale is to load files generated with hadoop which contain statistics.
 * 
 * @author Markus Michael Geipel
 * 
 */
public final class MultimapMaps{
	private static final Logger LOG = LoggerFactory.getLogger(MultimapMaps.class);

	private MultimapMaps() {
		// no instances
	}
	
	public static String asJsonString(final SimpleMultiMap multiMap){
		final StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (String mapName : multiMap.getMapNames()) {
			builder.append("\"" + StringEscapeUtils.escapeJavaScript(mapName) + "\":{");
			final Map<String, String> map = multiMap.getMap(mapName);
			for (Entry<String, String> entry : map.entrySet()) {
				builder.append("\"" + StringEscapeUtils.escapeJavaScript(entry.getKey()) + "\")");
				builder.append(":");
				builder.append("\"" + StringEscapeUtils.escapeJavaScript(entry.getValue()) +"\"");
			}
			builder.append("}");
		}
		builder.append("}");
		return builder.toString();
	}
	
	public static void load(final SimpleMultiMap multiMap, final String... fileNames) throws IOException {
		for (String fileName : fileNames) {
			final File file = new File(fileName);
			if (file.isDirectory()) {
				final File[] files = file.listFiles();
				for (File file2 : files) {
					load(multiMap, file2);
				}
			} else {
				load(multiMap, file);
			}
		}
	}
 
	private static void load(final SimpleMultiMap multiMap, final File file) throws IOException {

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
		LOG.debug(file.getName() + " loaded into map.");
	}

	private static void addRecord(final String line, final SimpleMultiMap multiMap) {
		
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
		multiMap.putValue(mapName, key, parts[1]);
	}
}
