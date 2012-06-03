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
 * TimeSignature.java
 * 
 * Class to represent the time signature of a song.
 * 
 * @author Spring 2010
 */
package model;

import io.BadInputException;

public final class TimeSignature {
	
	/** The number of beats per measure */
	private int numBeats;
	
	/** Note durationDenom that represents the length of one beat,
	 	i.e. 4 = quarter note, 8 = eighth note is one beat */
	private int beatLength;
	
	/**
	 * Constructor.
	 * @param numBeats The number of beats per measure in this TimeSignature.
	 * @param beatLength The length of a beat in this TimeSignature.
	 * @throws The TimeSignature was not valid, either because the beat length was negative,
	 * not divisible by 2 or greater than Note.SMALLESTNOTE, or the number of beats
	 * was negative.
	 * @author Spring 2010
	 */
	public TimeSignature(int numBeats, int beatLength) throws BadInputException {
		if (beatLength <= 0 || (beatLength & (beatLength - 1)) != 0 || beatLength > Note.SMALLESTNOTE)
			throw new BadInputException("Time Signature beatLength not valid");
		if (numBeats <= 0) 
			throw new BadInputException("Time Signature must have a positive number of beats per measure");
		
		this.numBeats = numBeats;
		this.beatLength = beatLength;	
	}
	
	@Override
	public String toString() {
		return numBeats + "/" + beatLength; 
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof TimeSignature){
			TimeSignature time = (TimeSignature) other;
			return (time.numBeats == this.numBeats) && (time.beatLength == this.beatLength);
		}
		return false;
	}
	
	/**
	 * Calculate the number of Note.SMALLESTNOTE in
	 * each measure of this TimeSignature, e.g. if 3/4,
	 * SMALLESTNOTE 16, this would return 12.
	 * @return The number of Note.SMALLESTNOTE in
	 * each measure of this TimeSignature.
	 * @author Jason Petersen
	 */
	public int smallestNotesPerMeasure() {
		return numBeats*(Note.SMALLESTNOTE/beatLength);
	}
	
	/* ---------- Getter methods ---------- */
	
	/**
	 * Get the number of beats per measure of this TimeSignature.
	 * @return The number of beats per measure of this TimeSignature.
	 * @author Spring 2010
	 */
	public int getNumBeats() {
		return numBeats;
	}
	
	/**
	 * Get the length of a beat for this TimeSignature.
	 * @return The length of the beat for this TimeSignature.
	 * @author Spring 2010
	 */
	public int getBeatLength() {
		return beatLength;
	}
	
}
