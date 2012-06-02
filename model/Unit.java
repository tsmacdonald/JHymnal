/**
 * Unit.java
 * 
 * Class to model a Syllable of Notes.
 * Used to create Voices.
 * 
 * @author Jason Petersen
 */

package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Unit implements Syllable, Iterable<Note> {

	/** The Notes in this Unit Syllable */
	private ArrayList<Note> notes;
	
	/** Indicates the associated Word should be repeated */
	private boolean repeat = false;
	
	/**
	 * Constructor.
	 * @author Jason Petersen
	 */
	public Unit() {
		this.notes = new ArrayList<Note>();
	}
	
	@Override
	public String toString() {
		if (notes == null) return "{ NULL }";
		else {
			String s = "{ ";
			for (Note n: notes) s += n + ", ";
			return s + " }";
		}
	}

	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Unit) {
			Unit unit = (Unit) other;
			boolean toReturn = (unit.notes.size() == this.notes.size()) && (unit.repeat == this.repeat);
			if(toReturn) {
				for(int i=0; i<this.notes.size(); i++)
					toReturn = toReturn && (unit.notes.get(i).equals(this.notes.get(i)));
			}
			return toReturn;
		}
		return false;
	}
	
	/**
	 * Get the number of Notes in this Unit.
	 * @return The number of Notes in this Unit.
	 * @author Jason Petersen
	 */
	public int numberOfNotes() {
		return notes.size();
	}
	
	/**
	 * Get the duration of this Unit in terms
	 * of Note.SMALLESTNOTE.
	 * @see model.Note.getDuration()
	 * @return The duration of this Unit in terms
	 * of Note.SMALLESTNOTE.
	 * @author Jason Petersen
	 */
	public int duration() {
		int i = 0;
		for(Note n : notes) i += n.getDuration();
		return i;
	}

	/**
	 * Add a Note to this Unit. Notes are stored in
	 * the order they are given.
	 * A Note will not be added if it is null.
	 * @param note The Note to add to this Unit.
	 * @author Jason Petersen
	 */
	public void addNote(Note note) {
		if(note != null) notes.add(note);
	}
	
	/* ---------- Setter methods ---------- */
	
	/**
	 * Set if the yet-to-be associated Word should
	 * be repeated.
	 * @param repeat True if the Word should be repeated,
	 * otherwise false.
	 * @author Jason Petersen
	 */
	public void setWordRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	/* ---------- Getter methods ---------- */
	
	/**
	 * Get whether the yet-to-be associated Word should
	 * be repeated.
	 * Default value is false.
	 * @return True if the Word should be repeated,
	 * otherwise false.
	 * @author Jason Petersen
	 */
	public boolean getWordRepeat() {
		return this.repeat;
	}
	
}
