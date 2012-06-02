/**
 * XMLUtility.java
 * 
 * Class to hold methods and
 * values which need to be available
 * to both parsers and writers of
 * XML files.
 * In general, it is assumed that
 * if an element/node/etc. is asked for, an
 * exception should be thrown if it is not found.
 * This is only not the case for the "metadata" methods.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import model.KeySignature;
import model.Line;
import model.Lyric;
import model.Meter;
import model.Music;
import model.Note;
import model.Refrain;
import model.Slur;
import model.Text;
import model.TimeSignature;
import model.Tune;
import model.Unit;
import model.Voice;
import model.Word;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

final class XMLUtility extends Utility {

	/** Location of the schema for a tune */
	private final String tuneSchemaLocation = "io/schema/tune.xsd";
	
	/** Location of a schema for a text */
	private final String textSchemaLocation = "io/schema/text.xsd";
	
	/** Schema for Text files */
	Schema textSchema;
	
	/** Schema for Tune files */
	Schema tuneSchema;
	
	/**
	 * Dummy class which throws the SAXParseExceptions
	 * instead of just printing the error. Used to
	 * throw an error if an XML file doesn't match
	 * the given schema.
	 * @author Jason Petersen
	 */
	private class EH implements ErrorHandler {
		private EH(){}
		@Override
		public void error(SAXParseException arg0) throws SAXException { throw arg0; }
		@Override
		public void fatalError(SAXParseException arg0) throws SAXException { throw arg0;}
		@Override
		public void warning(SAXParseException arg0) throws SAXException { throw arg0; }
	}
	
	/** Singleton */
	private static XMLUtility instance = new XMLUtility();
	
	/** 
	 * Constructor for singleton.
	 * @author Jason Petersen
	 */
	private XMLUtility() {
		// Create the text schema
		try {
			textSchema = createSchema(textSchemaLocation);
		}
		catch (XMLException ife){
			System.err.println("XMLUtility - Failed to create Text schema: " + ife.getMessage());
			textSchema = null;			
		}
		// Create the tune schema
		try {
			tuneSchema = createSchema(tuneSchemaLocation);
		}
		catch (XMLException ife){
			System.err.println("XMLUtility - Failed to create Tune schema: " + ife.getMessage());
			tuneSchema = null;			
		}
	}
	
	/**
	 * Accessor method for the singleton of XMLUtility
	 * @return The singleton of XMLUtility
	 * @author Jason Petersen
	 */
	static XMLUtility getInstance() {
		return instance;
	}
	
	/* ---------- Methods for Document processing ---------- */
	
	/**
	 * Get a Document from a file name.  
	 * @param filename The name of the file from which to get the Document.
	 * @return The Document retrieved from the file.
	 * @throws InputFileException
	 * @author Jason Petersen
	 */
	Document getDocument(File file) throws IOException {
		return getDocument(file, null);
	}

	/**
	 * Get a Document from a file name, using the given Schema for validation.  
	 * @param filename The name of the file from which to get the Document.
	 * @param schema The schema used to validate the XML file.
	 * @return The Document retrieved from the file.
	 * @throws InputFileException Failed to get the document. The InputFileException wraps
	 * either an IOException, SAXException or ParserConfigurationException.
	 * @author Jason Petersen
	 */
	Document getDocument(File file, Schema schema) throws IOException {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			if(schema != null)
				dbf.setSchema(schema);
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new EH()); //Explicitly set the error handler to throw the error, not just print it

			return db.parse(file);
		}
		catch(SAXException sax){
			throw new XMLException(sax);			
		}
		catch(ParserConfigurationException pce){
			throw new XMLException(pce);			
		}
	}
	
	/* ---------- Methods for String creation ---------- */
	
	/**
	 * Write out Text to XML-formatted String.
	 * @param tune Text to write out to XML-formatted String.
	 * @return The Text as an XML-formatted String.
	 * @author Jason Petersen
	 */
	String createString(Text text) {
		if(text == null) return "";
		
		// The minimum required for a Text
		String name = text.getName();
		Meter meter = text.getMeter();
		
		// These have default values
		String author = text.getAuthor();
		int year = text.getYear();
		
		String toReturn = "";
		toReturn += "<text>\n";
			toReturn += "\t<title>" + name + "</title>\n";
			toReturn += "\t<author>" + author + "</author>\n";
			toReturn += "\t<year>" + year + "</year>\n";
			toReturn += makeMeter(meter);
			toReturn += "\t<verses>\n";
				for(Iterator<Lyric> verses = text.iterator(); verses.hasNext(); ) {
					toReturn += "\t\t<verse>\n";
					for(Iterator<Line<Word>> lines = verses.next().iterator(); lines.hasNext(); ) {
						toReturn += "\t\t\t<line>";
						for(Iterator<Word> words = lines.next().iterator(); words.hasNext(); )
							toReturn += words.next().toString() + " ";
						toReturn += "</line>\n";
					}
					toReturn += "\t\t</verse>\n";
				}
			toReturn += "\t</verses>\n";
		toReturn += "</text>\n";
		return toReturn;
	}
	
	/**
	 * Write out Tune to XML-formatted String.
	 * @param tune Tune to write out to XML-formatted String.
	 * @return The Tune as an XML-formatted String.
	 * @author Jason Petersen
	 */
	String createString(Tune tune) {
		if(tune == null) return "";
		
		// Required to make a Tune
		String name = tune.getName();
		Meter meter = tune.getMeter();
		KeySignature key = tune.getKeySignature();
		TimeSignature timeSig = tune.getTimeSignature();
		
		// These have default values, safe to get them
		int year = tune.getYear();
		String author = tune.getAuthor();
		double startingBeat = tune.getStartingBeat();
		String melody = tune.getMelody().toString().toUpperCase();
		
		// Get the voices
		Music music = tune.getMusic();
		Voice soprano = (music != null) ? music.getSoprano() : null;
		Voice alto = (music != null) ? music.getAlto() : null;
		Voice tenor = (music != null) ? music.getTenor() : null;
		Voice bass = (music != null) ? music.getBass() : null;
		
		String toReturn = "";
		toReturn += "<tune>";
			toReturn += "\t<title>" + name + "</title>\n";
			toReturn += "\t<author>" + author + "</author>\n";
			toReturn += "\t<year>" + year + "</year>\n";
			toReturn += makeMeter(meter) + "\n";
			toReturn += "\t<melody>" + melody + "</melody>\n";
			toReturn += makeKeySignature(key) + "\n";
			toReturn += makeTimeSignature(timeSig) + "\n";
			toReturn += "\t<start>" + startingBeat + "</start>\n";
			toReturn += "\t<parts>\n";
				toReturn += (soprano != null) ? makeVoice(soprano) : "\t\t<soprano>\n\t\t</soprano>\n";
				toReturn += (alto != null) ? makeVoice(alto) : "\t\t<alto>\n\t\t</alto>\n";
				toReturn += (tenor != null) ? makeVoice(tenor) : "\t\t<tenor>\n\t\t</tenor>\n";
				toReturn += (bass != null) ? makeVoice(bass) : "\t\t<bass>\n\t\t</bass>\n";
			toReturn += "\t</parts>\n";
			Refrain chorus = tune.getChorus();
			if(chorus != null){
				Lyric l = chorus.getLyric();
				toReturn += "\t<chorus syllables=\"" + l.numberOfSyllables() + "\">\n";
					toReturn += makeRefrain(chorus);
				toReturn += "\t</chorus>\n";
			}
			boolean first = true;
			for(int i=0; i < tune.getMeter().numberOfLines(); i++){
				Refrain alleluia = tune.getAlleluiaForLine(i+1);
				if(alleluia != null){
					if(first) toReturn += "\t<alleluias>\n";
						toReturn += "\t\t<alleluia line=\"" + (i+1) + "\">\n";
							toReturn += makeRefrain(alleluia);
						toReturn += "\t\t</alleluia>\n";
					if(first) toReturn += "\t</alleluias>\n";
					first = false;
				}
			}
		toReturn += "</tune>";
		return toReturn;
	}
	
	/* ---------- Methods to get metadata values for Document Elements ---------- */
	
	/**
	 * Get the String value for an element, return a default value
	 * if nothing was found. Use this method for non-essential (i.e. metadata)
	 * fields for tunes and texts.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get a value.
	 * @return The found value as a String or an empty String if nothing was found.
	 * @author Jason Petersen
	 */
	String getMetadataString(Element e, String tag) {
		try{ return getString(e, tag); }
		catch(BadInputException bie){ return ""; }
	}
	
	/**
	 * Get the integer value for an element, return a default value
	 * if nothing was found. Use this method for non-essential (i.e. metadata)
	 * fields for tunes and texts.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get a value.
	 * @return The found value as an integer or 0 if nothing was found.
	 * @author Jason Petersen
	 */
	int getMetadataInteger(Element e, String tag) {
		try{ return getInteger(e, tag);	}
		catch(BadInputException bie){ return 0; }
	}
	
	/* ---------- Methods to get values for Document Elements ---------- */
	
	/**
	 * Get the Meter for a Text or Tune from an XML file.
	 * @param root The root XML element from which to get the meter element.
	 * @return The Text's Meter as found in the XML file.
	 * @throws BadInputException The Meter could not be parsed.
	 * @author Jason Petersen
	 */
	Meter parseMeter(Element root) throws BadInputException {
		Element meter = getFirstElement(root, "meter");
		ArrayList<Integer> lengths = getIntegerArray(meter, "count");
		String name = (meter.hasAttribute("name")) ? meter.getAttribute("name") : null;
		return new Meter(name, lengths);
	}
	
	/**
	 * Get an Element for a given tag.
	 * If the tag does not exist, simply returns null.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get the Element.
	 * @return The Element, if it exists, otherwise null.
	 * @author Jason Petersen
	 */
	Element getOptionalElement(Element e, String tag) {
		try { 
			return getFirstElement(e, tag); 
		}
		catch (BadInputException bie){ 
			return null;
		}
	}
	
	/**
	 * Get the integer value of an attribute for a given element.
	 * @param e The element from which to get the attribute.
	 * @param attr The name of the attribute to retrieve.
	 * @return The attribute's value as an integer.
	 * @throws BadInputException The given element does not have
	 * the given attribute.
	 * @author Jason Petersen
	 */
	int getAttributeInteger(Element e, String attr) throws BadInputException {
		if(e.hasAttribute(attr)){
			String s = e.getAttribute(attr);
			return makeStringInteger(s);
		}
		else
			throw new BadInputException(e.toString() + " does not have an attribute \"" + attr + "\".");
	}
	
	/**
	 * Get the boolean value of an attribute for a given element.
	 * @param e The element from which to get the attribute.
	 * @param attr The name of the attribute to retrieve.
	 * @return The attribute's value as an boolean.
	 * @throws BadInputException The given element does not have
	 * the given attribute.
	 * @author Jason Petersen
	 */
	boolean getAttributeBoolean(Element e, String attr) throws BadInputException {
		if(e.hasAttribute(attr)){
			String s = e.getAttribute(attr);
			return makeStringBoolean(s);
		}
		else
			throw new BadInputException(e.toString() + " does not have an attribute \"" + attr + "\".");
	}

	/**
	 * Get the String value for the given tag.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get a value.
	 * @return The found value as a String.
	 * @throws BadInputException The String could not be retrieved.
	 * @author Jason Petersen
	 */
	String getString(Element e, String tag) throws BadInputException {
		Element l = getFirstElement(e, tag);
		return getFirstChildValue(l);
	}
	
	/**
	 * Get the Integer value for the given tag.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get a value.
	 * @return The found value as an integer.
	 * @throws BadInputException The found value could not be made into an integer.
	 * @author Jason Petersen
	 */
	int getInteger(Element e, String tag) throws BadInputException {
		return makeStringInteger(getString(e, tag));
	}
	
	/**
	 * Get the double value for the given tag.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get a value.
	 * @return The found value as a double.
	 * @throws BadInputException The found value could not be made into a double.
	 * @author Jason Petersen
	 */
	double getDouble(Element e, String tag) throws BadInputException {
		return makeStringDouble(getString(e, tag));
	}
	
	/**
	 * Get the boolean value for the given tag.
	 * @param e The XML element in which to search for the given tag.
	 * @param tag The name of the tag for which to get a value.
	 * @return The found value as a boolean, empty if nothing was found.
	 * @throws BadInputException The given tag does not have a value.
	 * @author Jason Petersen
	 */
	boolean getBoolean(Element e, String tag) throws BadInputException {
		String s = getString(e, tag);
		if(s != null && s.length() > 0)
			return makeStringBoolean(s);
		throw new BadInputException("The tag " + tag + " has no value.");
	}
	
	/**
	 * Get an ArrayList of String for the given tag.
	 * @param e The XML element in which to search for instances of the given tag.
	 * @param tag The name of the tag for which to get values.
	 * @return The ArrayList of String, which may be empty.
	 * @author Jason Petersen
	 */
	ArrayList<String> getStringArray(Element e, String tag) {
		ArrayList<String> strings = new ArrayList<String>();	
		NodeList n = getNodeList(e, tag);
		if(n != null){
			for(int i = 0; i < n.getLength(); i++){
				try {
					String s = getFirstChildValue((Element) n.item(i));
					strings.add(s);
				} catch(BadInputException bie) {
					System.err.println("XMLUtility.getStringArray - " + bie.getMessage());
				}
			}
		}
		return strings;
	}
	
	/**
	 * Get an ArrayList of Integers for the given tag.
	 * @param e The XML element in which to search for instances of the given tag.
	 * @param tag The name of the tag for which to get values.
	 * @return The ArrayList of Integers, which may be empty.
	 * @throws BadInputException The integer array could be be created.
	 * @author Jason Petersen
	 */
	ArrayList<Integer> getIntegerArray(Element e, String tag) throws BadInputException {
		ArrayList<Integer> integers = new ArrayList<Integer>();
		ArrayList<String> strings = getStringArray(e, tag);
		for(String s : strings){
			int i = makeStringInteger(s);
			integers.add(i);
		}
		return integers;
	}
	
	/**
	 * Get a NodeList of elements for the given tag.
	 * @param e The XML element in which to obtain the NodeList.
	 * @param tag The name of the tag for which to get a NodeList.
	 * @return The NodeList consisting of Elements with the given tag. May be null.
	 * @author Jason Petersen
	 */
	NodeList getNodeList(Element e, String tag) {
		return e.getElementsByTagName(tag);		
	}
	
	/**
	 * Get an element for a given tag.
	 * @param e The XML element in which to search for an Element with the given tag.
	 * @param tag The name of the tag for which to get an Element.
	 * @return The Element with the given tag.
	 * @throws BadInputException The given Element does not have an instance of the given tag.
	 * @author Jason Petersen
	 */
	Element getFirstElement(Element e, String tag) throws BadInputException {
		NodeList n = getNodeList(e, tag);
		if(n != null && n.getLength() > 0){
			return (Element) n.item(0);
		}
		else
			throw new BadInputException("The element " + e.toString() + " does not have a \"" + tag + "\" tag.");
	}
	
	/* ---------- Helper methods to write out Objects from the Model to XML-formatted String ---------- */
	
	/**
	 * Write out Refrain to XML-formatted String.
	 * @param refrain Refrain to write out to XML-formatted String.
	 * @return The Refrain as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeRefrain(Refrain refrain) {
		String toReturn = "";
		for(Iterator<Voice> voices = refrain.getMusic().iterator(); voices.hasNext(); ){
			Voice v = voices.next();
			String name = v.getPart().toString().toLowerCase();
			toReturn += "\t\t<" + name + ">\n";
				for(Iterator<Line<Unit>> lines = v.iterator(); lines.hasNext(); )
					for(Iterator<Unit> units = lines.next().iterator(); units.hasNext(); )
						toReturn += makeUnit(units.next()) + "\n";
			toReturn += "\t\t</" + name + ">\n";
		}
		toReturn += "\t\t<lyrics>\n";
		for(Iterator<Line<Word>> lines = refrain.getLyric().iterator(); lines.hasNext(); )
			for(Iterator<Word> words = lines.next().iterator(); words.hasNext(); ){
				Word w = words.next();
				for(Iterator<String> strings = w.iterator(); strings.hasNext(); )
					toReturn += strings.next() + " ";
		}
		return toReturn.trim() + "\n\t\t</lyrics>\n";
	}
	
	/**
	 * Write out Voice to XML-formatted String.
	 * @param voice Voice to write out to XML-formatted String.
	 * @return The Voice as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeVoice(Voice voice) {
		String toReturn = "";
		if(voice != null) {
			String name = voice.getPart().toString().toLowerCase();
			
			toReturn += "\t\t<" + name + ">\n";
				for(Iterator<Line<Unit>> lines = voice.iterator(); lines.hasNext(); ) {
					toReturn += "\t\t\t<line>\n";
					for(Iterator<Unit> units = lines.next().iterator(); units.hasNext(); ) {
						toReturn += makeUnit(units.next());
					}
					toReturn += "\t\t\t</line>\n";
				}
			toReturn += "\t\t</" + name + ">\n";
		}
		return toReturn;
	}
	
	/**
	 * Write out KeySignature to XML-formatted String.
	 * @param key KeySignature to write out to XML-formatted String.
	 * @return The KeySignature as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeKeySignature(KeySignature key) {
		return "\t<key>" + key.name() + "</key>\n";
	}
	
	/**
	 * Write out TimeSignature to XML-formatted String.
	 * @param time TimeSignature to write out to XML-formatted String.
	 * @return The TimeSignature as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeTimeSignature(TimeSignature time) {
		int numBeats = time.getNumBeats();
		int beatLength = time.getBeatLength();
		
		String toReturn = "";
		toReturn += "\t<time>\n";
			toReturn += "\t\t<beats>" + numBeats + "</beats>\n";
			toReturn += "\t\t<length>" + beatLength + "</length>\n";
		toReturn += "\t</time>";
		return toReturn;
	}

	/**
	 * Write out Meter to XML-formatted String.
	 * @param meter Meter to write out to XML-formatted String.
	 * @return The Meter as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeMeter(Meter meter) {
		String toReturn = "";
		toReturn += "\t<meter name=\"" + meter.getName() + "\">\n";
			for(Iterator<Integer> counts = meter.iterator(); counts.hasNext(); )
				toReturn += "\t\t<count>" + counts.next() + "</count>\n";
		toReturn += "\t</meter>\n";
		return toReturn;
	}
	
	/**
	 * Write out Unit to XML-formatted String.
	 * @param unit Unit to write out to XML-formatted String.
	 * @return The Unit as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeUnit(Unit unit) {
		String toReturn = "";
		toReturn += "\t\t\t\t<syllable repeat=\"" + unit.getWordRepeat() + "\">\n";
			for(Iterator<Note> notes = unit.iterator(); notes.hasNext(); ) 
				toReturn += makeNote(notes.next());
		toReturn += "\t\t\t\t</syllable>\n";
		return toReturn;
	}
	
	/**
	 * Write out Note to XML-formatted String.
	 * @param note Note to write out to XML-formatted String.
	 * @return The Note as an XML-formatted String.
	 * @author Jason Petersen
	 */
	private String makeNote(Note note) {
		boolean dotted = note.isDotted();
		boolean tie = note.startsTie();
		boolean rest = note.isRest();
		Slur slurStatus = note.slurStatus();
		String pitch = note.getPitchName();
		int durationDenom = note.getDurationDenom();
		
		String toReturn = "";
		toReturn += "\t\t\t\t\t<note dotted=\"" + dotted + "\" tie=\"" + tie + "\" slur=\"" + slurStatus + "\" rest=\"" + rest +"\">\n";
			toReturn += "\t\t\t\t\t\t<pitch>" + pitch + "</pitch>\n";
			toReturn += "\t\t\t\t\t\t<duration>" + durationDenom + "</duration>\n";
		toReturn += "\t\t\t\t\t</note>\n";
		return toReturn;
	}
	
	/* ---------- Helper methods ---------- */
		
	/**
	 * Helper method to get the String value for the first child of an XML element.
	 * @param e The XML element from which to get the value of the first child node.
	 * @return The value for the first child node as a String.
	 * @throws BadInputException The XML element does not have a first child.
	 * @author Jason Petersen
	 */
	private String getFirstChildValue(Element e) throws BadInputException {
		Node n = e.getFirstChild();
		if(n != null)
			return n.getNodeValue();
		else
			throw new BadInputException("The element " + e.toString() + "does not have a FirstChild");
	}
	
	/**
	 * Helper method to create a schema from a given file.
	 * @param filename The file from which to make a Schema.
	 * @return The Schema created from the file.
	 * @throws InputFileException
	 * @author Jason Petersen
	 */
	private Schema createSchema(String filename) throws XMLException {
		try{
			File file = new File(filename);
			SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			return sf.newSchema(file);
		}
		catch (SAXException sax){
			throw new XMLException(sax);
		}
	}

}
