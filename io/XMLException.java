/**
 * XMLException.java
 * 
 * Exception class for invalid XML input.
 * Basically this acts to wrap a bunch of
 * XML exceptions, e.g. SAXException
 * This is really a terrible idea in general,
 * but it makes the throwing much less cluttered.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class XMLException extends IOException {
	/** Useful if we were going to serialize this class, but this mostly just keeps Eclipse from complaining */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param sax SAXExeception to be wrapped as an XMLException.
	 * @author Jason Petersen
	 */
	XMLException(SAXException sax) {
		super("SAXException: " + sax.getMessage());
	}
	
	/**
	 * Constructor.
	 * @param pce ParserConfigurationException to be wrapped as an XMLException.
	 * @author Jason Petersen
	 */
	XMLException(ParserConfigurationException pce) {
		super("ParserConfigurationException: " + pce.getMessage());
	}

	/**
	 * Constructor.
	 * @param tce TransformerConfigurationException to be wrapped as an XMLException.
	 * @author Jason Petersen
	 */
	XMLException(TransformerConfigurationException tce) {
		super("TransformerConfigurationException: " + tce.getMessage());
	}
	
	/**
	 * Constructor.
	 * @param te TransformerException to be wrapped as an XMLException.
	 * @author Jason Petersen
	 */
	XMLException(TransformerException te) {
		super("TransformerException: " + te.getMessage());
	}
	
}
