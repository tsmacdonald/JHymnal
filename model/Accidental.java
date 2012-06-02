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
