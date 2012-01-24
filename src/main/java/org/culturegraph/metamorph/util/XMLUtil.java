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

import org.w3c.dom.Node;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class XMLUtil {
	
	private static final String UNEXPECTED_TRANSFORMATION_ERROR =
			"This should not have happened";
	
	private XMLUtil() {
		// No instances allowed
	}
	
	public static String nodeToString(final Node node) {
		final StringWriter writer = new StringWriter();
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerException e) {
			throw new RuntimeException(UNEXPECTED_TRANSFORMATION_ERROR, e);
		}
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		try {
			transformer.transform(new DOMSource(node), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new RuntimeException(UNEXPECTED_TRANSFORMATION_ERROR, e);
		}
		
		return writer.toString();
	}

}
