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
 * Text.java
 * 
 * This class represents the lyrical portion of a simple 4-part hymn.  
 * It does not include the corresponding tune. 
 * 
 * @author Spring 2010
 */

package model;

import io.BadInputException;

import java.util.ArrayList;
import java.util.Iterator;

public final class Text implements HymnComponent, Iterable<Lyric> {

	/** The name of this Text */
	private String name;

	/** The Meter for this Text */
	private Meter meter;
	
	/** The author of this Text */
	private String author = "Unknown";

	/** The year this Text was written */
	private int year = 0;
	
	/** Determine if this Text is valid */
	private boolean valid;

	/** The verses for this Text */
	private ArrayList<Lyric> verses = new ArrayList<Lyric>();

	/**
	 * Constructor.
	 * @param name The name for this Text.
	 * @param meter The meter for this Text.
	 * @throws BadInputException The input for the Text
	 * was null or the name length was 0.
	 * @author Jason Petersen
	 */
	public Text(String name, Meter meter) throws BadInputException {
		if(name == null || name.length() == 0 || meter == null)
			throw new BadInputException("Input for Text cannot be null");
		
		this.name = name;
		this.meter = meter;
		this.valid = false;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public Iterator<Lyric> iterator() {
		return verses.iterator();
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
		if(other instanceof Text) {
			Text text = (Text) other;
			boolean toReturn = 	(text.name.equals(this.name)) &&
								(text.author.equals(this.author)) &&
								(text.year == this.year) &&
								(text.meter.equals(this.meter)) &&
								(text.verses.size() == this.verses.size());
			if(toReturn) {
				for(int i=0; i<this.verses.size(); i++)
					toReturn = toReturn && (text.verses.get(i).equals(this.verses.get(i)));
			}
			return toReturn;
		}
		return false;
	}
	
	/**
	 * Get the number of verses in this Text.
	 * @return The number of verses in this Text.
	 * @author Spring 2010
	 */
	public int numberOfVerses() {
		return verses.size();
	}

	/* ---------- Setter methods ---------- */

	/**
	 * Set the Verses for this Text.
	 * Will not be set if input is null.
	 * @param verses ArrayList of Lyric representing the verses
	 * in order.
	 * @author Jason Petersen
	 */
	public void setVerses(ArrayList<Lyric> verses) {
		if(verses != null){
			this.verses = verses;
			this.valid = validate();
		}
	}
	
	/**
	 * Set the author of this Text.
	 * Will not be updated if input is null.
	 * @param author The author of this Text.
	 * @author Jason Petersen
	 */
	public void setAuthor(String author) {
		if(author != null && author.length() > 0)
			this.author = author;
	}

	/**
	 * Set the year this Text was written.
	 * @param year The year this Text was written.
	 * @author Jason Petersen
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/* ---------- Getter methods ---------- */

	/**
	 * Get the name for this Text.
	 * @return The name of this Text.
	 * @author Spring 2010
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the author for this Text.
	 * Default value is "Unknown".
	 * @return The author for this Text.
	 * @author Spring 2010
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Get the year this Text was written.
	 * Default value is 0.
	 * @return The year this Text was written.
	 * @author Jason Petersen
	 */
	public int getYear() {
		return this.year;
	}
	
	/* ---------- Helper methods ---------- */
	
	/**
	 * Text if this Text is valid.
	 * A Text is valid if all the lyrics have
	 * the same numer of syllables as
	 * the meter for this Text.
	 * @return True if the Text is valid, otherwise false. 
	 * @author Jason Petersen
	 */
	private boolean validate() {
		int syllables = meter.numberOfSyllables();
		
		for(Lyric l : verses)
			if(l.numberOfSyllables() != syllables)
				return false;
		
		return true;
	}

}
