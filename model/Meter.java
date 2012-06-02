/**
 * Meter.java
 * 
 * This class represents the meter of either the text or tune of a hymn.  The
 * meter is the number of syllables per line of text, which requires
 * corresponding phrasing in the tune.
 * 
 * @author Neile Havens
 */

package model;

import java.util.ArrayList;
import java.util.Iterator;

public final class Meter implements Iterable<Integer> {
	/** The name for this Meter */
	private String name = null;
	
	/** The integer counts for this Meter */
	private ArrayList<Integer> counts = new ArrayList<Integer>();
	
	/**
	 * Constructor.
	 * @param name The name of the Meter.
	 * @param counts The list of counts for the Meter.
	 * @author Neile Havens
	 */
	public Meter(String name, ArrayList<Integer> counts) {
		if(name != null && name.length() > 0 && !name.equals("null")) this.name = name;
		if(counts != null) this.counts = counts;
	}

	/**
	 * Constructor.
	 * @param counts The list of counts for the Meter.
	 * @author Neile Havens
	 */
	public Meter(ArrayList<Integer> counts) {
		if(counts != null) this.counts = counts;
	}
	
	@Override
	public String toString() {
		String s = (hasName()) ? name : "";
		if(counts.size() != 0) {
			boolean addParen = s.length() > 0;
			s += (addParen) ? " (" : "";
			for(int i=0; i<counts.size(); i++) {
				s += counts.get(i);
				if(i != counts.size()-1) s += ".";
			}
			s += (addParen) ? ")" : "";
		}
		return s;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Meter) {
			Meter meter = (Meter) other;
			boolean toReturn = (meter.name == null ? this.name == null : meter.name.equals(this.name)) &&
			                   (meter.counts.size() == this.counts.size());
			if(toReturn) {
				for(int i=0; i<this.counts.size(); i++)
					toReturn = toReturn && (meter.counts.get(i).equals(this.counts.get(i)));
				return toReturn;
			}
		}
		return false;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return counts.iterator();
	}	
	
	/* ---------- Getter methods ---------- */
	
	/**
	 * Determine if this Meter has a name.
	 * @return True if the Meter has a name, otherwise false.
	 * @author Jason Petersen
	 */
	public boolean hasName() {
		return name != null;
	}
	
	/**
	 * Get the name of this Meter.
	 * May be null.
	 * @return The name of this Meter. 
	 * @author Jason Petersen
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the number of lines for this Meter.
	 * @return The number of lines for this Meter.
	 * @author Jason Petersen
	 */
	public int numberOfLines(){
		return counts.size();
	}
	
	/**
	 * Get the number of syllables
	 * expected for this Meter.
	 * @return The number of syllables expected
	 * for this Meter.
	 * @author Jason Petersen
	 */
	public int numberOfSyllables() {
		int total = 0;
		for(int i : counts) total += i;
		return total;
	}
	
}
