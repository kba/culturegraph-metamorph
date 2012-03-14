/**
 * 
 */
package org.culturegraph.metamorph.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.culturegraph.metamorph.core.exceptions.ShouldNeverHappenException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class XMLUtil {
	
	private static final String APPLICATION_XML_MIME_TYPE = "application/xml";
	private static final String TEXT_XML_MIME_TYPE = "text/xml";
	private static final String XML_BASE_MIME_TYPE = "+xml";
	
	private XMLUtil() {
		// No instances allowed
	}
	
	public static String nodeToString(final Node node) {
		return nodeToString(node, false);
	}
	
	public static String nodeToString(final Node node, final boolean omitXMLDecl) {
		final StringWriter writer = new StringWriter();
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerException e) {
			throw new ShouldNeverHappenException(e);
		}
		
		if (omitXMLDecl) {
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} else {
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");			
		}
		
		try {
			transformer.transform(new DOMSource(node), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new ShouldNeverHappenException(e);
		}
		
		return writer.toString();
	}

	public static String nodeListToString(final NodeList nodes) {
		final StringBuilder builder = new StringBuilder();
		
		for (int i=0; i < nodes.getLength(); ++i) {
			builder.append(nodeToString(nodes.item(i), i != 0));
		}
		
		return builder.toString();
	}
	
	public static boolean isXmlMimeType(final String mimeType) {
		if (mimeType == null) {
			return false;
		}
		return APPLICATION_XML_MIME_TYPE.equals(mimeType) || 
				TEXT_XML_MIME_TYPE.equals(mimeType) || 
				mimeType.endsWith(XML_BASE_MIME_TYPE);
	}
}
