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
 * Accidental.java
 * 
 * Enum to model an accidental.
 * Used for both Notes and KeySignatures.
 * 
 * @author Spring 2010
 */

package model;

import io.BadInputException;

public enum Accidental {
	FLAT, NATURAL, SHARP;

	/**
	 * Get the Accidental for the given character.
	 * @param c The character for which to get the Accidental.
	 * @return The Accidental that corresponds to the given character.
	 * @throws BadInputException There is no Accidental associated with
	 * the given character.
	 * @author Spring 2010
	 */
    public static final Accidental getAccidentalFor(Character c) throws BadInputException {
        switch (c) {
            case 'f':
	    	case 'F': return FLAT;
	    	case 'n':
	    	case 'N': return NATURAL;
	    	case 's':
	    	case 'S': return SHARP;
	    	default: throw new BadInputException("Invalid Accidental: " + c);
        }
    }

}
