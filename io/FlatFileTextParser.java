 /**
 * FlatFileTextParser.java
 * 
 * Instance of a TextParser.
 * Reads in files formatted as per the
 * Spring 2010's specifications.
 * 
 * @author Spring 2010
 * @author Jason Petersen
 */

package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import model.Lyric;
import model.Meter;
import model.Text;
import model.Word;
import model.Line;

@Deprecated
final class FlatFileTextParser implements TextParser {
	
	/** Scanner for reading in the file */
	private Scanner sc = null;
	
	/** The current line of the file */
	private int lineNumber = 0;
	
	/** Helper utility with methods for FlatFiles */
	private static FlatFileUtility util = FlatFileUtility.getInstance();
	
	/** Singleton */
	private static FlatFileTextParser instance = new FlatFileTextParser();
	
	/**
	 * Constructor for Singleton.
	 * @author Jason Petersen
	 */
    private FlatFileTextParser() { }
    
	/**
	 * Accessor for Singleton of FlatFileTextParser.
	 * @return Singleton of FlatFileTextParser.
	 * @author Jason Petersen
	 */
    static FlatFileTextParser getInstance() {
    	return instance;
    }
    
    @Override
    public Text parse(File file) throws IOException {
    	sc = new Scanner(new BufferedReader(new FileReader(file)));
		if (this.sc == null) throw new BadInputException(lineNumber, "Scanner for input file not initialized");

		String name = getNextLine().trim();
        String author = getNextLine().trim();
        
        // Get the date for the text
        int date = -1;
        try {
            date = Integer.parseInt(getNextLine().trim());
        } catch (NumberFormatException nfe) {
            throw new BadInputException(lineNumber, "Invalid date, must one an integer, the year the text was written");
        }
        
		String meterNumbers = getNextLine().trim();
		Meter meter = util.buildMeter(meterNumbers, lineNumber);

        // Get the expected number of verses
        int numberVerses = 0;
    	String numberVersesString = getNextLine().trim();
        try {
            numberVerses = Integer.parseInt(numberVersesString);
		} catch (NumberFormatException n) {
			throw new BadInputException(lineNumber, "Invalid number of Verses - must be an integer");
		}

        Text text = new Text(name, meter);
		
        // Add the verses
        ArrayList<Lyric> verses = new ArrayList<Lyric>();
        for (int i = 0; i < numberVerses; i++)
            verses.add(buildLyric(meter));
        text.setVerses(verses);

        
        // Add metadata
        text.setAuthor(author);
        text.setYear(date);

		sc.close();
		return text;
    }
    
    /* ---------- Helper functions ---------- */
    
    /**
     * Build a Lyric of the text for one verse. This is done on
     * a line by line basis, where a single line of input matches
     * a single line in the given Meter.
     * @param m The Meter used to determine if the number of syllables
     * in each line of the input is valid.
     * @return The newly created Lyric.
     * @throws InputFileException The number of syllables did not match the meter.
     * @author Spring 2010
     */
    private Lyric buildLyric(Meter m) throws BadInputException {
    	Lyric words = new Lyric();
		for (Integer i: m) { // iterate through the lines for this verse
			String line = getNextLine().trim(); // this checks for EOF
			if (util.lineFitsMeter(line,i)) {
				StringTokenizer lineTokenizer = new StringTokenizer(line);
				Line<Word> l = new Line<Word>();
				while (lineTokenizer.hasMoreTokens()) { // iterate through the syllables for this line
					l.addSyllable(new Word(lineTokenizer.nextToken()));
				}
				words.addLine(l);
			} else throw new BadInputException(lineNumber, "Number of syllables does not match meter");
		}
        return words;
    }
    
	/**
	 * Wrapper function to get the next line. This calls the getNextLine method
	 * from the FlatFileUtility and increments the line number for this
	 * FlatFileTextParser.
	 * @return The next line from this FlatFileTextParser's scanner.
	 * @throws InputFileException See exception description in FlatFileUtility.getNextLine(Scanner sc, int lineNumber)
	 * @author Jason Petersen
	 */
	private String getNextLine() throws BadInputException {
		String s = util.getNextLine(sc, lineNumber);
		lineNumber++;
		return s;
	}
}
