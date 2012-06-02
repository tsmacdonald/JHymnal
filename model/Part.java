/**
 * Part.java
 * 
 * Class to model the
 * Voice parts.
 * 
 * @author Jason Petersen, Tim Macdonald 
 */

package model;

import io.BadInputException;


public enum Part {
	SOPRANO {
		@Override
		public String toString(){ return "Soprano"; }
	},
	ALTO {
		@Override
		public String toString(){ return "Alto"; }
	},
	TENOR {
		@Override
		public String toString(){ return "Tenor"; }
	},
	BASS {
		@Override
		public String toString(){ return "Bass"; }
	};
	
    /**
     * Get the Part for this String.
     * Valid input are any capitalization variation
     * on soprano, alto, tenor, bass.
     * @param s The String for which to get the Part.
     * @return The Part associated with the input String.
     * @throws BadInputException The String was null or
     * the input was invalid.
     * @author Jason Petersen
     */
    public static final Part get(String s) throws BadInputException {
    	if(s == null)
    		throw new BadInputException("Part input cannot be null");
    	
    	s = s.toUpperCase();
    		
    	if(s.equals("SOPRANO")) return SOPRANO;
    	else if(s.equals("ALTO")) return ALTO;
    	else if(s.equals("TENOR"))	return TENOR;
    	else if(s.equals("BASS")) return BASS;
    	else throw new BadInputException("Part input invalid: " + s);	
    }
}
