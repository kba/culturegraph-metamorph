/**
 * 
 */
package org.culturegraph.metamorph.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.culturegraph.metamorph.core.exceptions.MetamorphException;
import org.culturegraph.metamorph.stream.readers.Reader;

/**
 * Utilities for using mime-types with streams.
 * 
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class MimeTypeUtil {

	private static final String APPLICATION_XML_MIME_TYPE = "application/xml";
	private static final String TEXT_XML_MIME_TYPE = "text/xml";
	private static final String XML_BASE_MIME_TYPE = "+xml";
	
	private static final String MIME_TYPE_MAPPING_FILE = "mime-type-mapping.properties";

	private static Properties mimeTypeMapping;
	
	private MimeTypeUtil() {
		// No instances allowed
	}
	
	/**
	 * Returns true if the data format described by mimeType is an xml-format.
	 * The following mime-types are considered to be xml-formats:
	 * <ul>
	 *   <li>application/xml</li>
	 *   <li>text/xml</li>
	 *   <li>any mime-type ending with +xml (e.g. image/svg+xml)</li>
	 * </ul> 
	 *   
	 * @param mimeType mime-type string such as "text/plain" or "text/xml".
	 * @return true if mimeType describes an xml-format otherwise false. 
	 *         If mimeType is null, false is returned.
	 */
	public static boolean isXmlMimeType(final String mimeType) {
		if (mimeType == null) {
			return false;
		}
		return APPLICATION_XML_MIME_TYPE.equals(mimeType) || 
				TEXT_XML_MIME_TYPE.equals(mimeType) || 
				mimeType.endsWith(XML_BASE_MIME_TYPE);
	}
	
	public static Reader getReaderForMimeType(final String mimeType) {
		loadMimeTypeMapping();
		
		final String classType = mimeTypeMapping.getProperty(mimeType);
		if (classType == null) {
			throw new MetamorphException("No reader class defined for mime-type: " + mimeType);
		}
		
		Class<? extends Reader> clazz;
		try {
			clazz = Class.forName(classType).asSubclass(Reader.class);
		} catch (ClassNotFoundException e) {
			throw new MetamorphException("Cannot find reader class: " + classType, e);
		}

		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new MetamorphException("Cannot instantiate reader class: " + classType, e);
		} catch (IllegalAccessException e) {
			throw new MetamorphException("Not allowed to instantiate reader class: " + classType, e);
		}
	}
	
	private static void loadMimeTypeMapping() {
		if (mimeTypeMapping == null) {
			final InputStream inputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(MIME_TYPE_MAPPING_FILE);
			mimeTypeMapping = new Properties();
			try {
				mimeTypeMapping.load(inputStream);
			} catch (IOException e) {
				throw new MetamorphException("Cannot load mime-type mapping", e);
			}
		}
	}

}
