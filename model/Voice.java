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
