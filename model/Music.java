/**
 * Music.java
 * 
 * Parent class for Tunes and Refrains.
 * This gives an easy way of extending
 * to add lyrics or metering to the
 * traditional SATB voices that define
 * a piece of music.
 * 
 * @author Jason Petersen
 */

package model;

import java.util.ArrayList;
import java.util.Iterator;

public final class Music implements Iterable<Voice> {
	/** Determine if this Music is valid */
	private boolean valid;
	
	/** The alto Voice for this Music */
	private Voice alto;
	
	/** The soprano Voice for this Music */
	private Voice soprano;
	
	/** The tenor Voice for this Music */
	private Voice tenor;
	
	/** The bass Voice for this Music */
	private Voice bass;
	
	/**
	 * Constructor.
	 * @author Jason Petersen
	 */
	public Music() {
		this.valid = false;
	}
	
	@Override
	public Iterator<Voice> iterator() {
		ArrayList<Voice> voices = new ArrayList<Voice>();
		if(soprano != null) voices.add(soprano);
		if(alto != null) voices.add(alto);
		if(tenor != null) voices.add(tenor);
		if(bass != null) voices.add(bass);
		return voices.iterator();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Music) {
			Music music = (Music) other;
			return (music.soprano == null ? this.soprano == null : music.soprano.equals(this.soprano)) &&
			       (music.alto == null ? this.alto == null : music.alto.equals(this.alto)) &&
			       (music.tenor == null ? this.tenor == null : music.tenor.equals(this.tenor)) &&
			       (music.bass == null ? this.bass == null : music.bass.equals(this.bass));
		}
		return false;
	}
	
	/**
	 * Get the Voice corresponding to the given enum Part.
	 * If the input is null, default return is soprano.
	 * @param part The enum Part for which to get the corresponding Voice.
	 * @return The Voice corresponding to the given enum Part.
	 * @author Jason Petersen
	 */
	public Voice getVoiceFor(Part part) {
		if(part == null) return soprano;
		
		if(part == Part.SOPRANO) return soprano;
		else if(part == Part.ALTO) return alto;
		else if(part == Part.TENOR) return tenor;
		else return bass;
	}
	
	/**
	 * Determine if this Music is valid.
	 * @return True if the Music is valid, otherwise false.
	 * @author Jason Petersen
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Get the number of Syllables for this Music.
	 * This will return -1 if not valid, otherwise
	 * if it is valid (i.e. the number of Syllables for
	 * all Voices match), it will return the correct
	 * number of Syllables.
	 * @return The number of Syllables for this Music.
	 * @author Jason Petersen
	 */
	public int numberOfSyllables() {
		if(valid) return soprano.numberOfSyllables();
		return -1;
	}
	
	/**
	 * Get the duration for this Music.
	 * This will return -1 if not valid, otherwise
	 * if it is valid (i.e. the duration of Syllables for
	 * all Voices match), it will return the correct duration.
	 * @return The duration for this Music.
	 * @author Jason Petersen
	 */
	public int duration() {
		if(valid) return soprano.duration();
		return -1;
	}
	
	/* ---------- Setter methods --------- */
	
	/**
	 * Set the alto Voice for this Music.
	 * @param v The alto Voice for this Music.
	 * @author Jason Petersen
	 */
	public void setAlto(Voice v) {
		if(v != null) {
			this.alto = v;
			this.valid = validate();
		}
	}

	/**
	 * Set the soprano Voice for this Music.
	 * @param v The soprano Voice for this Music.
	 * @author Jason Petersen
	 */
	public void setSoprano(Voice v) {
		if(v != null) {
			this.soprano = v;
			this.valid = validate();
		}
	}

	/**
	 * Set the tenor Voice for this Music.
	 * @param v The tenor Voice for this Music.
	 * @author Jason Petersen
	 */
	public void setTenor(Voice v) {
		if(v != null) {
			this.tenor = v;
			this.valid = validate();
		}
	}
	
	/**
	 * Set the bass Voice for this Music.
	 * @param v The bass Voice for this Music.
	 * @author Jason Petersen
	 */
	public void setBass(Voice v) {
		if(v != null) {
			this.bass = v;
			this.valid = validate();
		}
	}
	
	/* ---------- Getter methods --------- */
	
	/**
	 * Get the alto Voice for this Music.
	 * May be null.
	 * @return the alto Voice for this Music.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	public Voice getAlto() {
		return this.alto;
	}
	
	/**
	 * Get the soprano Voice for this Music.
	 * May be null.
	 * @return the soprano Voice for this Music.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	public Voice getSoprano() {
		return this.soprano;
	}
	
	/**
	 * Get the tenor Voice for this Music.
	 * May be null.
	 * @return the tenor Voice for this Music.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	public Voice getTenor() {
		return this.tenor;
	}
	
	/**
	 * Get the bass Voice for this Music.
	 * May be null.
	 * @return The bass Voice for this Music.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	public Voice getBass() {
		return this.bass;
	}
	
	/* ---------- Private methods ---------- */
	
	/**
	 * Validate this piece of Music.
	 * Music is valid if all the Voices have
	 * the same number of Syllables and the total
	 * duration of the Syllables for all the Voices
	 * are equal.
	 * @return True if the Music is valid, otherwise false.
	 * @author Jason Petersen
	 */
	private boolean validate() {
		if(soprano == null || alto == null || tenor == null || bass == null)
			return false;
		
		int[] durations = { 0, 0, 0, 0 };
		int[] syllables = { 0, 0, 0, 0 };
		
		int count = 0;
		for(Iterator<Voice> voices = iterator(); voices.hasNext(); ){
			Voice v = voices.next();
			durations[count] = v.duration();
			syllables[count] = v.numberOfSyllables();
			count++;
		}
		
		return durations[0] == durations[1] && durations[1] == durations[2] && durations[2] == durations[3] &&
		syllables[0] == syllables[1] && syllables[1] == syllables[2] && syllables[2] == syllables[3];
	}
	
}
