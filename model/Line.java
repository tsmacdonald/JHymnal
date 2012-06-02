/**
 * Line.java
 * 
 * Represents a line in the meter
 * of a Text or Tune.
 * 
 * @author Jason Petersen 
 */

package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Line<T extends Syllable> implements Iterable<T> {

	/** The list of syllables for this Line */
	ArrayList<T> syllables;
	
	/**
	 * Constructor.
	 * @author Jason Petersen
	 */
	public Line() {
		this.syllables = new ArrayList<T>();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Line<?>) {
			Line<?> line = (Line<?>) other;
			boolean toReturn = line.syllables.size() == this.syllables.size();
			if(toReturn) {
				for(int i=0; i<this.syllables.size(); i++)
					toReturn = toReturn && line.syllables.get(i).equals(this.syllables.get(i));
			}
			return toReturn;
		}
		return false;
	}
	
	@Override
	public Iterator<T> iterator() {
		return syllables.iterator();
	}
	
	/**
	 * Add a Syllable to this Line.
	 * @param syllable The Syllable to add
	 * to this Line.
	 * @author Jason Petersen
	 */
	public void addSyllable(T syllable) {
		if(syllable != null) syllables.add(syllable);
	}
	
	/**
	 * Get the number of Syllables in this Line.
	 * @return The number of Syllables in this Line.
	 * @author Jason Petersen
	 */
	public int numberOfSyllables() {
		return syllables.size();
	}
}
