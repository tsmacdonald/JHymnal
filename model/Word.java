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
