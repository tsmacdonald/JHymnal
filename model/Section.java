/**
 * Section.java
 * 
 * Supertype for Lyrics
 * and Voices.
 * 
 * @author Jason Petersen
 */

package model;

public interface Section {
	
	/**
	 * Get the number of Lines
	 * in this Section.
	 * @return The number of Lines
	 * in this Section.
	 * @author Jason Petersen
	 */
	public int numberOfLines();
	
	/**
	 * Get the number of Syllables
	 * in this Section.
	 * @return The number of Syllables
	 * in this Section.
	 * @author Jason Petersen
	 */
	public int numberOfSyllables();
}
