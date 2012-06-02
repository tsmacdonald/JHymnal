/**
 * Words.java
 * 
 * Class to model a Syllable of String.
 * Used to create Lyrics. 
 * 
 * @author Jason Petersen
 */

package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public final class Word implements Syllable, Iterable<String> {

	/** The String indicating a skip */
	public static final String skip = "#";
	
	/** The delimiter String */
	public static final String delimiter = "+";
	
	/** The Strings in this Word, to be treated as one syllable */
	private ArrayList<String> strings;
	
	/**
	 * Constructor.
	 * The input String is split according to the delimiter.
	 * Valid inputs look like the following:
	 * Hello
	 * Hel-+lo
	 * lo
	 * Hel-
	 * Hello+#
	 * #
	 * Invalid input:
	 * Hel - lo
	 * Hel -
	 * @param input The String to make into a Word.
	 * @author Jason Petersen
	 */
	public Word(String input) { 
		strings = new ArrayList<String>();
		
		if(input != null) {
			StringTokenizer token = new StringTokenizer(input, delimiter);
			while(token.hasMoreTokens())
				strings.add(token.nextToken());
		}
	}
	
	@Override
	public String toString() {
		String toReturn = "";
		if(strings.size() != 0) {
			for(String s : strings)
				toReturn += s + delimiter;
			toReturn = toReturn.substring(0, toReturn.length()-delimiter.length());
		}
		return toReturn;
	}
	
	@Override
	public Iterator<String> iterator() { 
		return strings.iterator(); 
	}
	
	@Override 
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Word) {
			Word word = (Word) other;
			boolean toReturn = word.strings.size() == this.strings.size();
			if(toReturn) {
				for(int i=0; i<this.strings.size(); i++)
					toReturn = toReturn && (word.strings.get(i).equals(this.strings.get(i)));
			}
			return toReturn;
		}
		return false;
	}
	
	/**
	 * Get the number of Strings in this Word.
	 * @return The number of Strings in this Word.
	 * @author Jason Petersen
	 */
	public int numberOfStrings() { 
		return strings.size(); 
	}
	
}
