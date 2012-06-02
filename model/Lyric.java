/**
 * Lyric.java
 * 
 * Class to represent a syllabified verse
 * or chorus.
 * 
 * @author Jason Petersen
 */

package model;


import java.util.ArrayList;
import java.util.Iterator;

public final class Lyric implements Section, Iterable<Line<Word>> {
	
	/** The lines in this Lyric */
	private ArrayList<Line<Word>> lines;
	
	/**
	 * Constructor.
	 * @author Jason Petersen
	 */
	public Lyric() {
		lines = new ArrayList<Line<Word>>();
	}
	
	@Override
	public Iterator<Line<Word>> iterator() {
		return lines.iterator();
	}
	
	@Override
	public String toString() {
		String s = "\tVerse:\n";
		for(Line<Word> l : lines) {
			s += "\t\t";
			for(Word w : l)
				s += w;
			s += "\n";
		}
		return s;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Lyric){
			Lyric lyric = (Lyric) other;
			boolean toReturn = lyric.lines.size() == this.lines.size();
			if(toReturn){
				for(int i=0; i<this.lines.size(); i++)
					toReturn = toReturn && (lyric.lines.get(i).equals(this.lines.get(i)));
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
		for(Line<Word> l : lines)
			i += l.numberOfSyllables();
		return i;
	}
	
	/**
	 * Add a Line to this Lyric.
	 * @param line The Line to add to this Lyric.
	 * @author Jason Petersen
	 */
	public void addLine(Line<Word> line) {
		if(line != null) lines.add(line);
	}

}
