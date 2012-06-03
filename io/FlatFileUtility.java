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
 * FlatFileUtility.java
 * 
 * Contains common methods for dealing with
 * a FlatFile.  Note this is a concrete class
 * because other parts of the input package
 * may want to process a FlatFile using these
 * methods.
 * 
 * @author Jason Petersen
 * @author Spring 2010
 */

package io;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import model.Meter;

final class FlatFileUtility extends Utility {
	
	/** Singleton */
	private static FlatFileUtility instance = new FlatFileUtility();
	
	/** 
	 * Constructor for Singleton.
	 * @author Jason Petersen 
	 */
	private FlatFileUtility() { }
	
	/**
	 * Accessor method for the singleton of FlatFileUtility
	 * @return The singleton of FlatFileUtility
	 * @author Jason Petersen
	 */
	static FlatFileUtility getInstance() {
		return instance;
	}
   	
   	/* ---------- Package-scoped methods ---------- */
	
	/**
	 * Get the next line from the given Scanner.
	 * @param sc The Scanner from which to get the next line.
	 * @param lineNumber Indicates the current line number, used to create the InputFileException.
	 * @return The next line that is not whitespace or a comment.
	 * @throws InputFileException The Scanner has no more input.
	 * @author Spring 2010
	 */
   	String getNextLine(Scanner sc, int lineNumber) throws BadInputException {
		String s = null;
		while(sc.hasNextLine()) {
			s = sc.nextLine();
			if (isWhiteSpace(s)) continue;
			else if (isComment(s)) continue;
			else break;
		}
		if (s != null) return s;
		else throw new BadInputException(lineNumber, "Cannot getNextLine - no more input");
	}
   	
   	/**
   	 * Build a Meter using a period-delimited String of integers.
   	 * @param meterNumbers A period-delimited list of
   	 * integers representing the meter.
   	 * @param lineNumber Used when creating the InputFileException.
   	 * @return The Meter built from the input String.
   	 * @throws InputFileException The String contained a value
   	 * other than an integer or a period.
   	 * @author Spring 2010
   	 */
   	Meter buildMeter(String meterNumbers, int lineNumber) throws BadInputException {
		StringTokenizer st = new StringTokenizer(meterNumbers, ".");
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (st.hasMoreTokens()) {
			try {
				int i = Integer.parseInt(st.nextToken());
				list.add(i);
			} catch (NumberFormatException nfe) {
				throw new BadInputException(lineNumber, "Invalid character in meter description");
			}
		}
		return new Meter(list);
	}
   	
   	/* ---------- Helper methods ---------- */
	
   	/**
   	 * Test if a String is WhiteSpace, i.e. is null, has length zero,
   	 * or begins with a whitespace character.
   	 * @param s The String to test.
   	 * @return True if the String is WhiteSpace, otherwise false.
   	 * @author Spring 2010
   	 */
   	private boolean isWhiteSpace(String s) {
		return (s == null || s.length() == 0 || Character.isWhitespace(s.charAt(0)));
	}
	
   	/**
   	 * Test is a String is a comment per the FlatFile specifications.
   	 * @param s The String to test.
   	 * @return True if the String is a comment, otherwise false.
   	 * @author Spring 2010
   	 */
   	private boolean isComment(String s) {
		if (s == null || s.length() == 0) return false;
		return (Character.toString(s.charAt(0)).equals("#"));
	}
	
}
