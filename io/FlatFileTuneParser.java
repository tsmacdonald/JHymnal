/**
 * FlatFileTuneParser.java
 * 
 * Instance of a TuneParser.
 * Reads in files formatted as per the
 * Spring 2010's specifications.
 * 
 * @author Spring 2010
 * @author Jason Petersen
 */

package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.StringTokenizer;

import model.KeySignature;
import model.Line;
import model.Meter;
import model.Music;
import model.Note;
import model.Part;
import model.Slur;
import model.TimeSignature;
import model.Tune;
import model.Unit;
import model.UnsupportedNoteException;
import model.Voice;

@Deprecated
final class FlatFileTuneParser implements TuneParser {

	/** Scanner for reading in the file */
	private Scanner sc = null;
	
	/** The current line of the file */
	private int lineNumber = 0;
	
	/** Helper utility with methods for FlatFiles */
	private static FlatFileUtility util = FlatFileUtility.getInstance();
	
	/** Singleton */
	private static FlatFileTuneParser instance = new FlatFileTuneParser();
	
	/**
	 * Constructor for Singleton.
	 * @author Jason Petersen
	 */
	private FlatFileTuneParser() { }
	
	/**
	 * Accessor for Singleton of FlatFileTuneParser.
	 * @return Singleton of FlatFileTuneParser.
	 * @author Jason Petersen
	 */
	static FlatFileTuneParser getInstance() {
		return instance;
	}

	@Override
	public Tune parse(File file) throws FileNotFoundException, BadInputException {
		sc = new Scanner(new BufferedReader(new FileReader(file)));
		if (this.sc == null) throw new BadInputException(lineNumber, "Scanner for input file not initialized");

		String name = getNextLine().trim();
        String author = getNextLine().trim();
        
        int date;
        try {
            date = Integer.parseInt(getNextLine().trim());
        } catch (NumberFormatException nfe) {
            throw new BadInputException(lineNumber, "Invalid date, must one an integer, the year the tune was written");
        }
        
		String meterNumbers = getNextLine().trim();
		Meter meter = util.buildMeter(meterNumbers, lineNumber);
			
		KeySignature key = null;
		try {
			key = KeySignature.getKeySignatureFor(getNextLine().trim());
		} catch(BadInputException bie) {
			throw new BadInputException(lineNumber, bie.getMessage());
		}
		
		String ts = getNextLine().trim();
		TimeSignature time = buildTimeSignature(ts);

		String sb = getNextLine().trim();
		
		Voice s = buildVoice(meter, Part.SOPRANO);
		Voice a = buildVoice(meter, Part.ALTO);
		Voice t = buildVoice(meter, Part.TENOR);
		Voice b = buildVoice(meter, Part.BASS);
		
		Tune tune = new Tune(name, meter, key, time);
		
		// Add voices
		Music music = new Music();
		music.setSoprano(s);
		music.setAlto(a);
		music.setTenor(t);
		music.setBass(b);
		tune.setMusic(music);
		
		// Fill out the Tune with additional information
		tune.setAuthor(author);
		tune.setYear(date);
		tune.setStartingBeat(sb);
			
		sc.close();
		return tune;
	}
	
	/* ---------- Helper methods ---------- */
	
	/**
	 * Build a TimeSignature from the given String.
	 * @param t The String from which to build the TimeSignature
	 * @return The TimeSignature built from the given String.
	 * @throws InputFileException The number of beats was not a valid
	 * integer, the beat length was not a valid integer, or there
	 * was an error in creating the TimeSignature.
	 * @author Spring 2010
	 */
	private TimeSignature buildTimeSignature(String t) throws BadInputException {
		StringTokenizer s = new StringTokenizer(t);
		if (s.countTokens() != 2) throw new BadInputException(lineNumber, "Invalid Time Signature - " +
				"should have 2 integers (space-delimited)");
		int numBeats = 0, beatLength = 0;
		try {
			numBeats = Integer.parseInt(s.nextToken());
			beatLength = Integer.parseInt(s.nextToken());
		} catch (NumberFormatException n) {
			throw new BadInputException(lineNumber, "Invalid Time Signature - not 2 integers");
		}
		TimeSignature toReturn;
		try {
			toReturn = new TimeSignature(numBeats, beatLength);
		} catch(BadInputException bie) {
			throw new BadInputException(lineNumber, bie.getMessage());
		}
		return toReturn;
	}

	/**
	 * Build a Voice, e.g. alto
	 * @param m Meter used to validate the number of syllables in a line
	 * of the Voice.
	 * @param name The name of the Voice to build.
	 * @return The built Voice
	 * @throws InputFileException The number of syllables did not match the given meter.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	private Voice buildVoice(Meter m, Part p) throws BadInputException {
		Voice part = new Voice(p);
		for (Integer i: m) { // iterate through the lines for this part
			String line = getNextLine().trim(); // this checks for EOF
			if (util.lineFitsMeter(line,i)) {
				StringTokenizer lineTokenizer = new StringTokenizer(line);
				Line<Unit> l = new Line<Unit>();
				while (lineTokenizer.hasMoreTokens()) { // iterate through the syllables for this part
					l.addSyllable(buildUnit(lineTokenizer.nextToken()));
				}
				part.addLine(l);
			} else throw new BadInputException(lineNumber, "Number of syllables does not match meter: " +
                                                            "expected " + i + ", found " + (new StringTokenizer(line)).countTokens());
		}
		return part;
	}
	
	/**
	 * Build a Unit from the given String input.
	 * @param input The String from which to build the Unit.
	 * @return The Unit built from the given String.
	 * @throws InputFileException See buildNote for exception reason.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	private Unit buildUnit(String input) throws BadInputException {
		Unit unit = new Unit();
		StringTokenizer sylTokenizer = new StringTokenizer(input, ";");
		while (sylTokenizer.hasMoreTokens()) {
			unit.addNote(buildNote(sylTokenizer.nextToken()));
		}
		return unit;
	}
	
	/**
	 * Create a Note from the given String input.
	 * @param input The String from which to create a Note.
	 * @return The Note created from the given String.
	 * @throws InputFileException A whole slew of things could have
	 * possibly gone wrong. It's probably just best to print the output
	 * and figure it out for yourself. TODO Remove sarcasm
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	private Note buildNote(String input) throws BadInputException {
		StringTokenizer noteTokenizer = new StringTokenizer(input, ",");
		if (noteTokenizer.countTokens() != 5) 
				throw new BadInputException(lineNumber, "Bad note input - does not contain " +
						"exactly 5 parameters, " + input);
		
		String pitch = noteTokenizer.nextToken().toUpperCase();
		
		String durationString = noteTokenizer.nextToken();
		int durationDenom;
		try {
			durationDenom = Integer.parseInt(durationString);
		} catch (NumberFormatException nfe) {
			throw new BadInputException(lineNumber, "Duration denominator is not an integer");
		}
		
		boolean dotted = noteTokenizer.nextToken().contains(".");
		boolean rest = false;
		
		String tieStatusString = noteTokenizer.nextToken().toUpperCase();
		boolean startsTie;
		switch(tieStatusString.charAt(0)){
			// The old input files specify if a note is the start/middle/end
			// of a tie or has no tie: however, this is no longer needed
			// given the new model, hence we treat start/middle as starting a tie,
			// end/none as not starting a tie
			case '[':
			case '-': startsTie = true;
					  break;
			case ']':
			case 'N': startsTie = false;
					  break;
			default: throw new BadInputException(lineNumber, "Tie character " + tieStatusString.charAt(0) + " is invalid.");
		}
		
		String slurStatusString = noteTokenizer.nextToken().toUpperCase();
		Slur slurStatus;
        try {
            slurStatus = Slur.get(slurStatusString.charAt(0));
        } catch (BadInputException bie) {
            throw new BadInputException(lineNumber, bie.getMessage());
        }
		
		Note n;
		try {
			n = new Note(pitch, durationDenom, dotted, startsTie, slurStatus, rest);
		} catch (UnsupportedNoteException usne) {
			throw new BadInputException(lineNumber, "UnsupportedNoteException: " + 
					usne.getMessage());
		}
		return n;
	}
	
	/**
	 * Wrapper function to get the next line. This calls the getNextLine method
	 * from the FlatFileUtility and increments the line number for this
	 * FlatFileTextParser.
	 * @return The next line from this FlatFileTextParser's scanner.
	 * @throws InputFileException
	 * @author Jason Petersen
	 */
	private String getNextLine() throws BadInputException {
		String s = util.getNextLine(sc, lineNumber);
		lineNumber++;
		return s;
	}
	
}
