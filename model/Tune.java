// Copyright 2012 Jason Petersen and Timothy Macdonald
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Tune.java
 * 
 * This class represents the musical portion of a simple 4-part hymn.  
 * It does not include the corresponding text.
 * 
 * @author Spring 2010
 */

package model;

import io.BadInputException;

import java.util.HashMap;
import java.util.Iterator;

public final class Tune implements HymnComponent {
	/** The name of this Tune */
	private String name;

	/** The meter for this Tune */
	private Meter meter;

	/** The KeySignature for this Tune */
	private KeySignature key;

	/** The TimeSignature for this Tune */
	private TimeSignature time;
	
	/** The author of this Tune */
	private String author = "Unknown";

	/** The year the Tune was written */
	private int year = 0;

	/** The starting beat in the measure (where 0 is the first) */
	private double startingBeat = 1;

	/** The Music for this Tune */
	private Music music;
	
	/** The melody for this Tune, typically the soprano Voice */
	private Part melody = Part.SOPRANO;

	/** The Refrain for this Tune */
	private Refrain chorus;
		
	/** Maps line number to a Refrain which should be added at the end of that line */
	private HashMap<Integer, Refrain> alleluias = new HashMap<Integer, Refrain>();
	
	/** Determine if the Tune is valid */
	private boolean valid;

	/**
	 * Constructor.
	 * @param name The name of this Tune.
	 * @param meter The meter for this Tune.
	 * @param key The key signature for this Tune.
	 * @param time The time signature for this Tune.
	 * @throws BadInputException The imput was null or the name length was 0.
	 * @author Jason Petersen
	 */
	public Tune(String name, Meter meter, KeySignature key, TimeSignature time) throws BadInputException {
		if(name == null || name.length() == 0 || meter == null || key == null || time == null)
			throw new BadInputException("Input for Tune cannot be null");
		
		this.name = name;
		this.meter = meter;
		this.key = key;
		this.time = time;
		this.valid = false;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public Meter getMeter() { 
		return this.meter;
	}
	
	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public String getFilename() { 
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
		String name = getName();
		
		String filename = "";
		for(char c : name.toCharArray()){
			if(alphabet.contains(c + "")) filename += c;
		}
		
		return filename;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Tune){
			Tune tune = (Tune) other;
			return (tune.name == null ? this.name == null : tune.name.equals(this.name)) &&
			       (tune.author == null ? this.author == null : tune.author.equals(this.author)) &&
			       tune.year == this.year &&
			       tune.startingBeat == this.startingBeat &&
			       (tune.meter == null ? this.meter == null : tune.meter.equals(this.meter)) &&
			       (tune.key == null ? this.key == null : tune.key.equals(this.key)) &&
			       (tune.time == null ? this.time == null : tune.time.equals(this.time)) &&
			       (tune.melody == null ? this.melody == null : tune.melody.equals(this.melody)) &&
			       (tune.chorus == null ? this.chorus == null : tune.chorus.equals(this.chorus)) &&
			       (tune.music == null ? this.music == null : tune.music.equals(this.music)) &&
			       (equalAlleluias(tune));
		}
		return false;
	}
	
	/* ---------- Setter methods ---------- */
	
	/**
	 * Set the Music for this Tune
	 * Will not be set if Music is null.
	 * @param music The Music for this Tune.
	 * @author Jason Petersen
	 */
	public void setMusic(Music music) {
		if(music != null) {
			this.music = music;
			this.valid = meter.numberOfSyllables() == music.numberOfSyllables() && music.isValid();
		}
	}
	
	/**
	 * Set the melody for this Tune.
	 * @param The Part that is the melody for this Tune.
	 * @author Jason Petersen
	 */
	public void setMelody(Part part) {
		if(part != null)
			this.melody = part;
	}

	/**
	 * Set the chorus for this Tune.
	 * Will not be updated if input is null.
	 * @param chorus The Refrain that is the chorus for this Tune.
	 * @author Jason Petersen
	 */
	public void setChorus(Refrain chorus) {
		if(chorus != null)
			this.chorus = chorus;
	}

	/**
	 * Set the author of this Tune.
	 * Will not be updated if input is null.
	 * @param author The author of this Tune.
	 * @author Jason Petersen
	 */
	public void setAuthor(String author) {
		if(author != null && author.length() > 0)
			this.author = author;
	}

	/**
	 * Set the year this Tune was written.
	 * @param year The yaer this Tune was written.
	 * @author Jason Petersen
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Set the starting beat for this Tune.
	 * Will not be set if input is null or of length 0.
	 * @param input The starting beat double as a String.
	 * @throws BadInputException The input didn't contain
	 * a valid starting beat.
	 * @author Jason Petersen
	 */
	public void setStartingBeat(String input) throws BadInputException {
		if(input != null && input.length() > 0) {
			startingBeat = parseStartingBeat(input);
		}
	}

	/**
	 * Add an Refrain at the end of the given line.
	 * @param line The line after which the provided Refrain
	 * should be added. Valid line numbers start at 1 and cannot
	 * exceed the number of lines in the meter for this Tune. 
	 * @param alleluia The Refrain to add to the given line.
	 * @throws BadInputException The line number is invalid or
	 * an alleluia has already been added for the given line.
	 * @author Jason Petersen
	 */
	public void addAlleluia(int line, Refrain alleluia) throws BadInputException {
		if(alleluia == null)
			throw new BadInputException("Alleluia cannot be null");
		
		if(line < 1 || line > meter.numberOfLines())
			throw new BadInputException("Alleluia cannot be added to line " + line);
		
		if(!alleluias.containsKey(line))
			alleluias.put(line, alleluia);
		else throw new BadInputException("An alleluia already exists for line " + line);
	}
	
	/* ---------- Getter methods ---------- */

	/**
	 * Get the name for this Tune.
	 * @return The name for this Tune.
	 * @author Spring 2010
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the Music for this Tune.
	 * May be null.
	 * @return The Music for this Tune.
	 * @author Spring 2010
	 */
	public Music getMusic() {
		return this.music;
	}
	
	/**
	 * Get the KeySignature for this Tune.
	 * @return The KeySignature for this Tune.
	 * @author Spring 2010
	 */
	public KeySignature getKeySignature() {
		return this.key; 
	}

	/**
	 * Get the TimeSignature for this Tune.
	 * @return The TimeSignature for this Tune.
	 * @author Spring 2010
	 */
	public TimeSignature getTimeSignature() {
		return this.time;
	}

	/**
	 * Get the author of this Tune.
	 * Default value is "Unknown".
	 * @return The author of this Tune.
	 * @author Spring 2010
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Get the year this Tune was written.
	 * Default value is 0.
	 * @return The year the Tune was written.
	 * @author Spring 2010
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Get the starting beat for this Tune.
	 * Default value is 1.
	 * @return The starting beat for this Tune.
	 * @author Jason Petersen
	 */
	public double getStartingBeat() {
		return this.startingBeat;
	}

	/**
	 * Get the name of melody for this Tune.
	 * @return The name of the melody for this Tune.
	 * @author Jason Petersen
	 */
	public Part getMelody() {
		return this.melody;
	}
	
	/**
	 * Test if this Tune has a chorus.
	 * @return True if the Tune has a chorus,
	 * otherwise false.
	 * @author Jason Petersen
	 */
	public boolean hasChorus() {
		return this.chorus != null;
	}

	/**
	 * Get the chorus for this Tune.
	 * May be null.
	 * @return The chorus for this Tune.
	 * @author Jason Petersen
	 */
	public Refrain getChorus() {
		return this.chorus;
	}

	/**
	 * Get the alleluia Refrain attached to the given line.
	 * May be null.
	 * @param line The line for which to get the attached Refrain.
	 * @return The Refrain attached to the given line,
	 * null if no Refrain has been added for the line.
	 * @author Jason Petersen
	 */
	public Refrain getAlleluiaForLine(int line) {
		if(alleluias.containsKey(line)) {
			return alleluias.get(line);
		}
		return null;
	}
	
	/* ---------- Helper methods --------- */
	
	/**
	 * Helper method to test if alleluias for another
	 * Tune matches this Tune's alleluias.
	 * @return True if all the alleluias match, otherwise false.
	 * @author Jason Petersen
	 */
	private boolean equalAlleluias(Tune other) {
		boolean toReturn = true;
		int line = 1;
		for(Iterator<Integer> m = meter.iterator(); m.hasNext(); m.next()) {
			Refrain oa = other.getAlleluiaForLine(line);
			Refrain ta = this.getAlleluiaForLine(line);
			toReturn = toReturn && (oa == null ? ta == null : oa.equals(ta));
			line++;
		}
		return toReturn;
	}
	
	/**
	 * Helper method to parse starting beat String input
	 * to a double. 
	 * @param input The starting beat double as a String.
	 * @return The String input as a double.
	 * @throws BadInputException The input was not able to be expressed
	 * as [Integer]/[Note.SMALLESTNOTE] or it did not fit the Tune's time signature.
	 * @author Jason Petersen
	 */
	private double parseStartingBeat(String input) throws BadInputException {
		double toReturn = makeStringDouble(input);
		
		if(Math.floor(toReturn) != toReturn) {
			double decimal = toReturn - Math.floor(toReturn);
			int powTwo = 0;
			int note = Note.SMALLESTNOTE*2;
			while(note/2 != 1){
				powTwo++;
				note = note/2;
			}
			
			boolean validBeat = false;
			while(powTwo > 0) {
				decimal = decimal*2.0;
				if(Math.floor(decimal) == decimal) {
					validBeat = true;
					break;
				}
				powTwo--;
			}
			if(!validBeat) throw new BadInputException("The double must be able to be expressed as [Integer]/[" + Note.SMALLESTNOTE + "]");
		}
		
		if (toReturn < 1 || toReturn > time.getNumBeats()) 
			throw new BadInputException("Invalid Starting Beat - does not fit Time Signature");
		
		return toReturn;
	}

	/**
	 * Helper method to create a double from a String.
	 * @param s A String containing the double representation to be parsed.
	 * @return The double value represented by the argument in decimal.
	 * @throws BadInputException The string does not contain a parsable double.
	 * @author Jason Petersen
	 */
	private double makeStringDouble(String s) throws BadInputException {
		try{
			return Double.parseDouble(s);
		}
		catch (NumberFormatException nfe){
			throw new BadInputException(s + " is not a double.");
		}
	}
	
}
