 /**
 * XMLTextParser.java
 * 
 * Instance of a TextParser.
 * Reads in files formatted as per the
 * XML schema for a Text.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import model.Line;
import model.Lyric;
import model.Meter;
import model.Text;
import model.Word;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

final class XMLTextParser implements TextParser {

	/** The root element */
	private Element root;

	/** Instance of XMLUtility, used to perform basic XML functions */
	private static XMLUtility util = XMLUtility.getInstance();
	
	/** Singleton */
	private static XMLTextParser instance = new XMLTextParser();
	
	/**
	 * Constructor for Singleton.
	 * @author Jason Petersen
	 */
	private XMLTextParser(){ }
	
	/**
	 * Accessor for Singleton of XMLTextParser.
	 * @return Singleton of XMLTextParser.
	 * @author Jason Petersen
	 */
	static XMLTextParser getInstance() {
		return instance;
	}
	
	@Override
	public Text parse(File file) throws IOException {
		if(file == null)
			throw new IOException("File cannot be null");
		
		Document dom = util.getDocument(file, util.textSchema);
		this.root = dom.getDocumentElement();
		
		// The minimum required to make a Text
		Meter meter = util.parseMeter(root);
		String name = util.getString(root, "title");
		
		Text text = new Text(name, meter);
		
		// These fields are metadata and are not required.
		String author = util.getMetadataString(root, "author");
		int year = util.getMetadataInteger(root, "year");
		text.setAuthor(author);
		text.setYear(year);
		
		// Add the verses
		ArrayList<Lyric> list = parseVerses();
		text.setVerses(list);
		
		return text;
	}
	
	/* ---------- Helper methods ---------- */
	
	/**
	 * Parse all the verses for this Text.
	 * The ArrayList may be empty, but it will not
	 * be null.
	 * @return An ArrayList of parsed verses.
	 * @author Jason Petersen
	 */
	private ArrayList<Lyric> parseVerses() {
		ArrayList<Lyric> list = new ArrayList<Lyric>();
		
		try {
			Element v = util.getFirstElement(root, "verses");
			NodeList verses = util.getNodeList(v, "verse");
		
			if(verses != null){
				for(int i=0; i<verses.getLength(); i++){
					Element verse = (Element) verses.item(i);
					ArrayList<String> lines = util.getStringArray(verse, "line");
					list.add(createLyric(lines));
				}
			}
		} catch(BadInputException bie) {
			System.err.println("XMLTextParser.parseVerses - " + bie.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Create a Lyric from an ArrayList of lines,
	 * which are space-delimited syllable strings.
	 * @return The parsed Lyric.
	 * @author Jason Petersen
	 */
	private Lyric createLyric(ArrayList<String> lines) {
		Lyric lyric = new Lyric();
		
		for(String s : lines){
			// Split the string around the spaces
			StringTokenizer token = new StringTokenizer(s);
			
			// Add the syllables
			Line<Word> line = new Line<Word>();
			while(token.hasMoreTokens())
				line.addSyllable(new Word(token.nextToken()));
			lyric.addLine(line);
		}
		
		return lyric;
	}
	
}
