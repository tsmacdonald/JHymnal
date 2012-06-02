 /**
 * XMLTuneParser.java
 * 
 * Instance of a TuneParser.
 * Reads in files formatted as per the
 * XML schema for a Tune.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import model.KeySignature;
import model.Line;
import model.Lyric;
import model.Meter;
import model.Music;
import model.Note;
import model.Part;
import model.Refrain;
import model.Slur;
import model.TimeSignature;
import model.Tune;
import model.Unit;
import model.UnsupportedNoteException;
import model.Voice;
import model.Word;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

final class XMLTuneParser implements TuneParser {

	/** The root element */
	private Element root;

	/** Instance of XMLUtility, used to perform basic XML functions */
	private static XMLUtility util = XMLUtility.getInstance();
	
	/** Singleton */
	private static XMLTuneParser instance = new XMLTuneParser();
	
	/**
	 * Constructor for Singleton.
	 * @author Jason Petersen
	 */
	private XMLTuneParser() { }
	
	/**
	 * Accessor for Singleton of XMLTuneParser.
	 * @return Singleton of XMLTuneParser.
	 * @author Jason Petersen
	 */
	static XMLTuneParser getInstance() {
		return instance;
	}
	
	@Override
	public Tune parse(File file) throws IOException {
		if(file == null)
			throw new IOException("File cannot be null");
		
		Document dom = util.getDocument(file, util.tuneSchema);
		this.root = dom.getDocumentElement();
		
		// The minimum required to make a new Tune
		String name = util.getString(root, "title");
		Meter meter = util.parseMeter(root);
		TimeSignature time = parseTime();
		Tune tune = new Tune(name, meter, parseKey(), time);
		
		// These fields are metadata and are not required.
		tune.setAuthor(util.getMetadataString(root, "author"));
		tune.setYear(util.getMetadataInteger(root, "year"));

		// These fields have default values
		try {
			tune.setStartingBeat(util.getString(root, "start"));
		} catch(BadInputException bie) {
			System.err.println("XMLTuneParser.parse - " + bie.getMessage());
		}
		try {
			tune.setMelody(Part.get(util.getString(root, "melody")));
		} catch(BadInputException bie) {
			System.err.println("XMLTuneParser.parse - " + bie.getMessage());
		}
		
		// Set the music for the Tune
		tune.setMusic(parseMusic());

		// Add the chorus, if any
		Element chorus = util.getOptionalElement(root, "chorus");
		if(chorus != null){
			Refrain refrain = parseRefrain(chorus);						
			tune.setChorus(refrain);
		}
		
		// Add the alleluias, if any
		Element alleluias  = util.getOptionalElement(root, "alleluias");
		if(alleluias != null){
			NodeList list = util.getNodeList(alleluias, "alleluia");
			if(list != null){
				for(int i=0; i<list.getLength(); i++){
					Element alleluia = (Element) list.item(i);
					int line = util.getAttributeInteger(alleluia, "line");
					Refrain refrain = parseRefrain(util.getFirstElement(alleluia, "refrain"));
					tune.addAlleluia(line, refrain);
				}
			}
		}
		
		return tune;
	}

	/* ---------- Helper methods ---------- */
	
	/**
	 * Parse the Music for this Tune.
	 * The Music may not have any voices or
	 * syllables, but we don't care because
	 * the Tune can be "invalid" but still
	 * a proper object.
	 * @return The parsed Music for this Tune.
	 * @author Jason Petersen
	 */
	private Music parseMusic() {
		Music music = new Music();
		try {
			music.setSoprano(parseVoice(Part.SOPRANO));
		} catch(BadInputException bie) {
			System.err.println("XMLTuneParser.parseMusic() - " + bie.getMessage());
		}
		try {
			music.setAlto(parseVoice(Part.ALTO));
		} catch(BadInputException bie) {
			System.err.println("XMLTuneParser.parseMusic() - " + bie.getMessage());
		}
		try {
			music.setTenor(parseVoice(Part.TENOR));
		} catch(BadInputException bie) {
			System.err.println("XMLTuneParser.parseMusic() - " + bie.getMessage());
		}
		try {
			music.setBass(parseVoice(Part.BASS));
		} catch(BadInputException bie) {
			System.err.println("XMLTuneParser.parseMusic() - " + bie.getMessage());
		}
		return music;
	}
	
	/**
	 * Get the KeySignature for a Tune from an XML file.
	 * @return The Tune's KeySignature as found in the XML file.
	 * @throws BadInputException
	 * @author Jason Petersen
	 */
	private KeySignature parseKey() throws BadInputException {
		String k = util.getString(root, "key");
		return KeySignature.getKeySignatureFor(k);
	}
	
	/**
	 * Get the TimeSignature for a Tune from an XML file.
	 * @return The Tune's TimeSignature as found in the XML file.
	 * @throws BadInputException Couldn't parse the TimeSignature.
	 * @author Jason Petersen
	 */
	private TimeSignature parseTime() throws BadInputException {
		Element time = util.getFirstElement(root, "time");
		int beats = util.getInteger(time, "beats");
		int length = util.getInteger(time, "length");
		return new TimeSignature(beats, length);
	}
	
	/**
	 * Parse a Voice for this Tune.
	 * The Voice may not have lines or
	 * syllables, but we don't care because
	 * the Tune can be "invalid" but still
	 * a proper object.
	 * @return The parsed Voice for this Tune.
	 * @author Jason Petersen
	 */
	private Voice parseVoice(Part p) throws BadInputException {
		Voice part = new Voice(p);
		String name = p.toString().toLowerCase();
		
		// Get the NodeList of lines.
		Element parts = util.getFirstElement(root, "parts"); // Parts tag with tag for all the voices are required
		Element prt = util.getFirstElement(parts, name);
		NodeList lines = util.getNodeList(prt, "line");
		
		if(lines != null){
			for(int i=0; i<lines.getLength(); i++){
				Line<Unit> l = new Line<Unit>();
				
				// Get the syllables for a "line" element.
				Element line = (Element) lines.item(i);
				NodeList syllables = util.getNodeList(line, "syllable");
				
				// Add the syllables to the part, only incrementing numberOfSyllables if it's not a repeat
				if(syllables != null) {
					for(int j=0; j<syllables.getLength(); j++){
						Element syllable = (Element) syllables.item(j);
						Unit unit = parseUnit(syllable); // This will only throw an exception if something is incorrect with the Note
						boolean repeat = false;
						try {
							repeat = util.getAttributeBoolean(syllable, "repeat");
						} catch (BadInputException bie) {
							System.err.println("XMLTuneParser.parseVoice - " + bie.getMessage());
						}
						unit.setWordRepeat(repeat);
						l.addSyllable(unit); // Add the Unit to the Line
					}
				}
				part.addLine(l);
			}
		}
		
		return part;
	}
		
	/**
	 * Get the Refrain from an XML file.
	 * @param refrain The root element of the Refrain.
	 * @return The Refrain as found in the XML file.
	 * @throws BadInputException Couldn't parse the Refrain.
	 * @author Jason Petersen
	 */
	private Refrain parseRefrain(Element refrain) throws BadInputException {
		Music music = new Music();
		music.setSoprano(parseRefrainVoice(refrain, Part.SOPRANO));
		music.setAlto(parseRefrainVoice(refrain, Part.ALTO));
		music.setTenor(parseRefrainVoice(refrain, Part.TENOR));
		music.setBass(parseRefrainVoice(refrain, Part.BASS));
		
		Lyric lyric = parseRefrainLyric(refrain);	
		
		return new Refrain(music, lyric);
	}
	
	/**
	 * Get the Refrain Voice from an XML file for the given name.
	 * @param refrain The root element of the Refrain.
	 * @param name The name of the Voice to parse.
	 * @return The Refrain Voice as found in the XML file.
	 * @throws BadInputException Couldn't parse the Refrain Voice.
	 * @author Jason Petersen
	 */
	private Voice parseRefrainVoice(Element refrain, Part p) throws BadInputException {
		Voice part = new Voice(p);
		int expectedSyllables = util.getAttributeInteger(refrain, "syllables");
		String name = p.toString().toLowerCase();
		
		Element prt = util.getFirstElement(refrain, name);
		NodeList syllables = util.getNodeList(prt, "syllable");
		
		// Number of syllables didn't match the expected number
		if(syllables.getLength() != expectedSyllables) 
			throw new BadInputException("Incorrect number of syllables in refrain: " +
                                        "expected " + expectedSyllables + ", found " + syllables.getLength());
		
		Line<Unit> line = new Line<Unit>();
		for(int i=0; i<expectedSyllables; i++){ // Refrains don't have repeats, so we can use this guard
			Element syllable = (Element) syllables.item(i);
			Unit unit = parseUnit(syllable);
			line.addSyllable(unit);
		}
		part.addLine(line); // Only one line in a refrain
		
		return part;
	}
	
	/**
	 * Get the Refrain Lyric from an XML file.
	 * @param refrain The root element of the Refrain.
	 * @return The Refrain Lyric as found in the XML file.
	 * @throws BadInputException Couldn't parse the Refrain Lyric.
	 * @author Jason Petersen
	 */
	private Lyric parseRefrainLyric(Element refrain) throws BadInputException {
		Lyric lyric = new Lyric();
		int expectedSyllables = util.getAttributeInteger(refrain, "syllables");
		String words = util.getString(refrain, "lyrics");
		
		Line<Word> line = new Line<Word>();
		StringTokenizer token = new StringTokenizer(words.trim());
		while(token.hasMoreTokens()) line.addSyllable(new Word(token.nextToken()));
		lyric.addLine(line); // Only one line in a refrain
		
		if(expectedSyllables != lyric.numberOfSyllables())
			throw new BadInputException("Incorrect number of syllables in refrain lyrics: " +
                    "expected " + expectedSyllables + ", found " + lyric.numberOfSyllables());
		
		return lyric;
	}
	
	/**
	 * Create a Unit from an XML element.
	 * @param list The XML element containing a list of Note nodes from
	 * which to create a Unit.
	 * @return The Unit created from the given XML element.
	 * @throws BadInputException The Unit could not be parsed from the given Element.
	 * @author Jason Petersen
	 */
	private Unit parseUnit(Element list) throws BadInputException {
		Unit unit = new Unit();
		NodeList notes = util.getNodeList(list, "note");
		if(notes != null) {
			for(int i=0; i<notes.getLength(); i++){
				Note n = parseNote((Element) notes.item(i));
				unit.addNote(n);
			}
		}
		return unit;
	}
	
	/**
	 * Create a Note from an XML element.
	 * @param note The XML element from which to make a Note.
	 * @return The Note created from the XML element.
	 * @throws BadInputException Transformed UnsupportedNoteException (i.e. something
	 * was wrong with the Note input).
	 * @author Jason Petersen
	 */
	private Note parseNote(Element note) throws BadInputException {
		String d = (note.hasAttribute("dotted")) ? note.getAttribute("dotted") : "false";
		String s = (note.hasAttribute("slur")) ? note.getAttribute("slur") : "NONE";
		String t = (note.hasAttribute("tie")) ? note.getAttribute("tie") : "false";
	 	String r = (note.hasAttribute("rest")) ? note.getAttribute("rest") : "false";
		boolean dotted = util.makeStringBoolean(d);
		Slur slur = Slur.get(s);
		boolean tie = util.makeStringBoolean(t);
		String pitch = util.getString(note, "pitch");
		int duration = util.getInteger(note, "duration");
		boolean rest = util.makeStringBoolean(r);

		try{
			return new Note(pitch, duration, dotted, tie, slur, rest);
		}
		catch(UnsupportedNoteException usne){
			throw new BadInputException(usne.getMessage());
		}
	}
	
}
