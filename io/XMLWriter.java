/**
 * XMLWriter.java
 * 
 * Class to write out Texts, Tunes
 * and Songs to an XML file.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import model.Hymn;
import model.Text;
import model.Tune;

public final class XMLWriter implements Writer {

	/** Used to create the XML schema and validate files */
	private static XMLUtility util = XMLUtility.getInstance();
	
	/** Singleton */
	private static XMLWriter instance = new XMLWriter();
	
	/**
	 * Constructor for singleton.
	 * @author Jason Petersen
	 */
	private XMLWriter() { }
	
	/**
	 * Accessor for the singleton of XMLWriter.
	 * @return The singleton of XMLWriter.
	 * @author Jason Petersen
	 */
	public static XMLWriter getInstance(){
		return instance;
	}
	
	@Override
	public void write(Tune tune, File file) throws IOException {
		String asString = util.createString(tune);
		Document toWrite = createDocument(asString, util.tuneSchema);
		createFileFrom(toWrite, file);
	}

	@Override
	public void write(Text text, File file) throws IOException {
		String asString = util.createString(text);
		Document toWrite = createDocument(asString, util.textSchema);
		createFileFrom(toWrite, file);
	}
	
	@Override
	public void write(Hymn hymn, File file) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("XML does not have a schema for Hymns");
	}
	
	/* ---------- Helper methods ---------- */
	
	/**
	 * Create a dom Document from an XML String using the given schema.
	 * @param asXML The Text or Tune as an XML-formatted String.
	 * @param schema The schema used to validate the input String.
	 * @return The Document created from the given String, validated against
	 * the given schema.
	 * @throws InputFileException An IOException, SAXException or ParserConfigurationException occurred.
	 * @author Jason Petersen
	 */
	private Document createDocument(String asXML, Schema schema) throws IOException, XMLException {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			if(schema != null){
				dbf.setSchema(schema);
			}
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(new InputSource(new StringReader(asXML)));
		}
		catch(SAXException sax){
			throw new XMLException(sax);			
		}
		catch(ParserConfigurationException pce){
			throw new XMLException(pce);			
		}
	}
	
	/**
	 * Write out dom Document to a file. 
	 * @param dom Document to write to out to a file. 
	 * @throws InputFileException The Transformer used
	 * to indent/clean up the XML encountered an error.
	 * @author Jason Petersen
	 */
	private void createFileFrom(Document dom, File file) throws IOException {
		if(file == null)
			throw new IOException("File to write to cannot be null");
		
		try{
			if(file.exists()) file.delete();
			
			// Create transformer to make our XML look pretty.
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			t.setOutputProperty(OutputKeys.INDENT, "yes");

			// Write out the XML file.
			DOMSource src = new DOMSource(dom);
			StreamResult rslt = new StreamResult(file);
			t.transform(src, rslt);
			
			System.out.println("Created file " + file);
		}
		catch(TransformerConfigurationException tce){
			throw new XMLException(tce);
		} catch (TransformerException te) {
			throw new XMLException(te);
		}
	}
	
}
