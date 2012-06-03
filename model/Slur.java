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
 * Slur.java
 * 
 * Enum to represent a note slur.
 * 
 * @author Spring 2010
 */

package model;

import io.BadInputException;

public enum Slur {
	BEGIN, MIDDLE, END, NONE;

    /**
     * Create a Slur from the given Character.
     * @param c The Character from which to make the Slur.
     * @returns The Slur created from the given Character.
     * @throws BadInputException The Character was not recognized as a valid Slur.
     * @author Spring 2010
     */
    public static final Slur get(Character c) throws BadInputException {
        switch (c) {
            case '[': return BEGIN;
	    	case '-': return MIDDLE;
	    	case ']': return END;
	    	case 'N': return NONE;
	    	default: throw new BadInputException("NoteSlur input invalid: " + c);
        }
    }
    
    /**
     * Create a Slur from the given String.
     * @param s The String from which to make the Slur.
     * @returns The Slur created from the given String.
     * @throws BadInputException The String was not recognized as a valid Slur.
     * @author Jason Petersen
     */
    public static final Slur get(String s) throws BadInputException {
    	if(s == null)
    		throw new BadInputException("NoteSlur input cannot be null");	
    		
    	if(s.equals("NONE")) return NONE;
    	else if(s.equals("BEGIN")) return BEGIN;
    	else if(s.equals("MIDDLE"))	return MIDDLE;
    	else if(s.equals("END")) return END;
    	else throw new BadInputException("NoteSlur input invalid: " + s);	
    }
}
