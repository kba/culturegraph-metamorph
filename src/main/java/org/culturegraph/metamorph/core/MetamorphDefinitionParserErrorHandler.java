package org.culturegraph.metamorph.core;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class MetamorphDefinitionParserErrorHandler implements ErrorHandler {
	private static final String PARSE_ERROR = "Error parsing metamorph definition: ";
	
	MetamorphDefinitionParserErrorHandler() {
		// to avoid synthetic accessor methods
	}

	@Override
	public void warning(final SAXParseException exception) throws SAXException {
		handle(exception);
	}

	@Override
	public void fatalError(final SAXParseException exception) throws SAXException {
		handle(exception);
	}

	@Override
	public void error(final SAXParseException exception) throws SAXException {
		handle(exception);
	}
	
	private void handle(final SAXParseException exception) {
		throw new MetamorphDefinitionException(PARSE_ERROR + exception.getMessage(), exception);
	}
}