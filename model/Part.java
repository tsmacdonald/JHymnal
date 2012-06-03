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
