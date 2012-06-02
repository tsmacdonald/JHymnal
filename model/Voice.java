/**
 * Voice.java
 * 
 * Class to represent a syllabified voice,
 * e.g. alto
 * 
 * @author Jason Petersen
 */

package model;


import io.BadInputException;

import java.util.ArrayList;
import java.util.Iterator;

public final class Voice implements Section, Iterable<Line<Unit>> {
	
	/** The lines in this Voice */
	private ArrayList<Line<Unit>> lines;
	
	/** The Part of this Voice, e.g. ALTO */
	private Part part;
	
	/**
	 * Constructor.
	 * @param part The enum Part for this Voice.
	 * @author Jason Petersen
	 */
	public Voice(Part part) throws BadInputException {
		if(part == null)
			throw new BadInputException("Part for Voice cannot be null");
		
		this.part = part;
		this.lines = new ArrayList<Line<Unit>>();
	}
	
	@Override
	public String toString() {
		String s = "\t" + part.toString().toUpperCase() + ": \n";
		for(Line<Unit> l : lines) {
			s += "\t\t";
			for(Unit u : l)
				s += u;
			s += "\n";
		}
		return s;
	}
	
	@Override
	public Iterator<Line<Unit>> iterator() { 
		return lines.iterator(); 
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Voice){
			Voice voice = (Voice) other;
			boolean toReturn = (voice.part == this.part) && (voice.lines.size() == this.lines.size());
			if(toReturn){
				for(int i=0; i<this.lines.size(); i++)
					toReturn = toReturn && voice.lines.get(i).equals(this.lines.get(i));
			}
			return toReturn;
		}
		return false;
	}
	
	@Override
	public int numberOfLines() {
		return lines.size();
	}
	
	@Override
	public int numberOfSyllables() {
		int i = 0;
		for(Line<Unit> l : lines) {
			for(Iterator<Unit> units = l.iterator(); units.hasNext(); ) {
				if(!units.next().getWordRepeat())
					i ++;
			}
		}
		return i;
	}
	
	/**
	 * Add a Line to this Voice.
	 * @param line The Line to add to this Voice.
	 * @author Jason Petersen
	 */
	public void addLine(Line<Unit> line) {
		if(line != null) lines.add(line);
	}
	
	/**
	 * Get the duration of this Voice.
	 * This is done by adding up all the durations
	 * for all the Units in all the Lines of this Voice.
	 * @return The duration for this Voice.
	 * @author Jason Petersen
	 */
	public int duration(){
		int i = 0;
		for(Line<Unit> l : lines) {
			for(Iterator<Unit> units = l.iterator(); units.hasNext(); ) {
				Unit u = units.next();
				if(!u.getWordRepeat())
					i += u.duration();
			}
		}
		return i;
	}
	
	/* ---------- Getter methods ---------- */
	
	/**
	 * Get the enum Part for this Voice.
	 * @return The enum Part for this Voice.
	 * @author Jason Petersen
	 */
	public Part getPart() {
		return this.part;
	}
}
