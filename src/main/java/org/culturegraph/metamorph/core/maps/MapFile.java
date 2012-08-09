package org.culturegraph.metamorph.core.maps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.MetamorphException;
import org.culturegraph.util.ResourceUtil;

/**
 * Provides a {@link Map} based on a file. The file is supposed to be UTF-8
 * encoded. The separator is by default \t. <strong>Important:</strong> Lines that are not split in two
 * parts by the separator are ignored!
 * 
 * @author "Markus Michael Geipel"
 * 
 */
public final class MapFile implements Map<String, String> {
	private final Map<String, String> map = new HashMap<String, String>();
	private Pattern split = Pattern.compile("\t", Pattern.LITERAL);

	public void setFiles(final String files) {
		final String[] parts = files.split("\\s*,\\s*");
		for (String part : parts) {
			setFile(part);
		}
	}

	public void setFile(final String file) {
		final BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(ResourceUtil.getStream(file), "UTF-8"));

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.isEmpty()) {
						continue;
					}
					final String[] parts = split.split(line);
					if (parts.length == 2) {
						map.put(parts[0], parts[1]);
					}
				}

			} finally {
				reader.close();
			}

		} catch (UnsupportedEncodingException e) {
			throw new MetamorphException(e);
		} catch (FileNotFoundException e) {
			throw new MetamorphException("resource '" + file + "' not found", e);
		} catch (IOException e) {
			throw new MetamorphException(e);
		}

	}

	public void setSeparator(final String delimiter) {
		split = Pattern.compile(delimiter, Pattern.LITERAL);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	@Override
	public String get(final Object key) {
		return map.get(key);
	}

	@Override
	public String put(final String key, final String value) {
		return map.put(key, value);
	}

	@Override
	public String remove(final Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(final Map<? extends String, ? extends String> map) {
		this.map.putAll(map);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<String> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return map.entrySet();
	}

	@Override
	public boolean equals(final Object obj) {
		return map.equals(obj);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

}
