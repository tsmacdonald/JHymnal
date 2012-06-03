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
 * Utility.java
 * 
 * Abstract class for a Utility.
 * Looking ahead, we may add
 * additional file types whose
 * typical utility classes may share
 * a lot of code, which can be pulled up
 * into this class.
 * 
 * @author Jason Petersen
 */

package io;

import java.util.StringTokenizer;

public abstract class Utility {

	/**
	 * Determine whether the number of tokens on a line in the input file (where musical
	 * information is expected) matches the expected number of tokens (aka syllables)
	 * according to the meter.
	 * @param line The line from the input file.
	 * @param i The expected number of tokens based on the meter.
	 * @return True if the line fits the meter, false otherwise.
	 * @author Spring 2010
	 */
	protected boolean lineFitsMeter(String line, int i) {
		StringTokenizer s = new StringTokenizer(line);
		return s.countTokens() == i;
	}
	
	/* ---------- Helper methods to create integers, doubles and booleans from Strings ---------- */
	
	/**
	 * Helper method to create an integer from a String.
	 * @param s A String containing the integer representation to be parsed.
	 * @return The integer value represented by the argument in decimal.
	 * @throws BadInputException The string does not contain a parsable integer.
	 * @author Jason Petersen
	 */
	protected int makeStringInteger(String s) throws BadInputException {
		if(s == null) throw new BadInputException(s + " is not an integer.");
		try{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException nfe){
			throw new BadInputException(s + " is not an integer.");
		}
	}
	
	/**
	 * Helper method to create a double from a String.
	 * @param s A String containing the double representation to be parsed.
	 * @return The double value represented by the argument in decimal.
	 * @throws BadInputException The string does not contain a parsable double.
	 * @author Jason Petersen
	 */
	protected double makeStringDouble(String s) throws BadInputException {
		if(s == null) throw new BadInputException(s + " is not a double.");
		try{
			return Double.parseDouble(s);
		}
		catch (NumberFormatException nfe){
			throw new BadInputException(s + " is not a double.");
		}
	}
	
	/**
	 * Helper method to create a boolean from a String.
	 * @param s A String containing the boolean representation to be parsed.
	 * @return True if the lowercase input is "true", otherwise false.
	 * @author Jason Petersen
	 */
	protected boolean makeStringBoolean(String s) {
		if(s == null) return false;
		return s.trim().toLowerCase().equals("true");
	}

}
