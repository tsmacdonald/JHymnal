/**
 * Database.java
 * 
 * Class that contains all the Texts/Tunes
 * that we display in the hymnal.
 * 
 * @author Jason Petersen, Nathan Bliss
 */

package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.Hymn;
import model.Text;
import model.Tune;

public class Database {
	/** The String indicating the folder location of the database */
	static final String dbFolder = "data/xml";
	
	/** The String indicating the folder location of the images */
	static final String imgCache = "data/images";
	
	private static XMLWriter writer = XMLWriter.getInstance();
	
	private ArrayList<Text> allTexts;
	
	private ArrayList<Tune> allTunes;
	
	/** The Texts in the database */
	private ArrayList<Text> validTexts;

	/** The Tunes in the database */
	private ArrayList<Tune> validTunes;

	/** Singleton for Database */
	private static Database instance;
	static {
		try {
			instance = new Database();
		} catch(IOException e) {
			System.err.println("Database - " + e.getMessage());
			instance = null;
		}
	}
	
	/**
	 * Constructor for singleton.
	 * @throws InputFileException Could not create the database.
	 * @author Jason Petersen
	 */
	private Database() throws IOException {
		allTexts = new ArrayList<Text>();
		allTunes = new ArrayList<Tune>();
		
		validTexts = new ArrayList<Text>();
		validTunes = new ArrayList<Tune>();
		
		File db = new File(dbFolder);
		if(db.isDirectory()) {
			for(File file : db.listFiles()) {
				if(file.isFile()) {
					try {
						Text text = ParserEngine.parseText(file);
						allTexts.add(text);
						if(text.isValid())
							validTexts.add(text);
					} catch(UnsupportedFileException e){
						try {
							Tune tune = ParserEngine.parseTune(file);
							allTunes.add(tune);
							if(tune.isValid())
								validTunes.add(tune);
						} catch(UnsupportedFileException ioe){
							System.err.println("Database - Failed to parse " + file.getName());
							ioe.printStackTrace();
						}
					}
				}
			}
		}
		else throw new IOException("The Database location is not a directory");
	}

	/**
	 * Accessor method for singleton of Database.
	 * Will be null if the Database encountered an error when instantiating.
	 * @return The singleton of Database.
	 * @author Jason Petersen
	 */
	public static Database getInstance() {
		return instance;
	}
	
	/**
	 * Delete all the images lilypond created
	 * @author Jason Petersen
	 */
	public void clearCache() {
		File cache = new File(imgCache);
		if(cache.isDirectory())
			for(File file : cache.listFiles())
				if(file.isFile())
					file.delete();
	}
	
	/**
	 * Get an iterable list of all the valid Texts in the Database.
	 * @return An iterable list of all the valid Texts in the Database.
	 * @author Jason Petersen
	 */
	public Iterable<Text> allTextIterator() {
		return allTexts;
	}
	
	/**
	 * Get an iterable list of all the valid Tunes in the Database.
	 * @return An iterable list of all the valid Tunes in the Database.
	 * @author Jason Petersen
	 */
	public Iterable<Tune> allTuneIterator() {
		return allTunes;
	}
	
	/**
	 * Get an iterable list of all the valid Texts in the Database.
	 * @return An iterable list of all the valid Texts in the Database.
	 * @author Tim Macdonald
	 */
	public Iterable<Text> validTextIterator() {
		return validTexts;
	}
	
	/**
	 * Get an iterable list of all the valid Tunes in the Database.
	 * @return An iterable list of all the valid Tunes in the Database.
	 * @author Tim Macdonald
	 */
	public Iterable<Tune> validTuneIterator() {
		return validTunes;
	}

	/**
	 * Get the list of Texts that can be combined
	 * with the given Tune.
	 * @param tune Tune for which to get the relevant Texts.
	 * @return An ArrayList of relevant Texts.
	 * @author Jason Petersen
	 */
	public ArrayList<Text> relevantTexts(Tune tune) {
		ArrayList<Text> relevant = new ArrayList<Text>();
		if(tune == null) return relevant;
		for(Text text : validTexts)
			if(Hymn.canCombine(text, tune))
				relevant.add(text);
		return relevant;
	}
	
	/**
	 * Get the list of Tunes that can be combined
	 * with the given Text.
	 * @param text Text for which to get the relevant Tunes.
	 * @return An ArrayList of relevant Tunes.
	 * @author Jason Petersen
	 */
	public ArrayList<Tune> relevantTunes(Text text) {
		ArrayList<Tune> relevant = new ArrayList<Tune>();
		for(Tune tune : validTunes)
			if(Hymn.canCombine(text, tune))
				relevant.add(tune);
		return relevant;
	}
	
	/**
	 * Add a Text to the Database.
	 * This will also write out the XML
	 * to the Database location.
	 * @param text The Text to add to the Database.
	 * @throws IOException Failed to write out
	 * the Text to disk.
	 * @author Jason Petersen
	 */
	public void add(Text text) throws IOException {
		if(text == null)
			throw new IOException("Text cannot be null");
		
		Text old = null;
		
		for(Text t : allTexts) {
			if(t.equals(text)) {
				return; // The Text wasn't changed
			}
			if(t.getName().equals(text.getName())) {
				old = t; // Grab the old Text
				break;
			}
		}
		
		// Remove old Text
		if(old != null) {
			allTexts.remove(old);
			if(validTexts.contains(old)) {
				validTexts.remove(old);
			}
		}
		
		// Write out the Text, save it to ArrayLists
		writer.write(text, new File(dbFolder + "/" + text.getFilename() + ".xml"));
		allTexts.add(text);
		if(text.isValid())
			validTexts.add(text);
	}
	
	/**
	 * Add a Tune to the Database.
	 * This will also write out the XML
	 * to the Database location.
	 * @param tune The Tune to add to the Database.
	 * @throws IOException Failed to write out
	 * the Tune to disk.
	 * @author Jason Petersen
	 */
	public void add(Tune tune) throws IOException {
		if(tune == null)
			throw new IOException("Tune cannot be null");
		
		Tune old = null;
		
		for(Tune t : allTunes) {
			if(t.equals(tune)) {
				return; // The Tune wasn't changed
			}
			if(t.getName().equals(tune.getName())) {
				old = t; // Grab the old Tune
				break;
			}
		}
		
		// Remove the old Tune
		if(old != null) {
			allTunes.remove(old);
			if(validTunes.contains(old)) {
				validTunes.remove(old);
			}
		}
		
		// Write out the Tune, save it to ArrayLists
		writer.write(tune, new File(dbFolder + "/" + tune.getFilename() + ".xml"));
		allTunes.add(tune);
		if(tune.isValid())
			validTunes.add(tune);
	}

}
