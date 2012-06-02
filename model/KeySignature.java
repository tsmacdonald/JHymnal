/**
 * KeySignature.java
 * 
 * Enum to represent the key signature of a song.
 * 
 * @author Spring 2010
 */

package model;

import io.BadInputException;

public enum KeySignature {
	C{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, N, N, N, N, N, N}; }
		@Override
		public String majorName(){ return "C Major"; }
		@Override
		public String minorName(){ return "A Minor"; }
	},
	//SHARP KEYS		
	G{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, N, N, S, N, N, N}; }
		@Override
		public String majorName(){ return "G Major"; }
		@Override
		public String minorName(){ return "E Minor"; }
	},
	D{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{S, N, N, S, N, N, N}; }
		@Override
		public String majorName(){ return "D Major"; }
		@Override
		public String minorName(){ return "B Minor"; }
	},
	A{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{S, N, N, S, S, N, N}; }
		@Override
		public String majorName(){ return "A Major"; }
		@Override
		public String minorName(){ return "F Sharp Minor"; }
	},
	E{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{S, S, N, S, S, N, N}; }
		@Override
		public String majorName(){ return "E Major"; }
		@Override
		public String minorName(){ return "C Sharp Minor"; }
	},
	B{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{S, S, N, S, S, S, N}; }
		@Override
		public String majorName(){ return "B Major"; }
		@Override
		public String minorName(){ return "G Sharp Minor"; }
	},
	Fs{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{S, S, S, S, S, S, N}; }
		@Override
		public String majorName(){ return "F Sharp Major"; }
		@Override
		public String minorName(){ return "D Sharp Minor"; }
	},
	Cs{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{S, S, S, S, S, S, S}; }
		@Override
		public String majorName(){ return "C Sharp Major"; }
		@Override
		public String minorName(){ return "A Sharp Minor"; }
	},
	//FLAT KEYS
	F{ 
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, N, N, N, N, N, Fl}; }
		@Override
		public String majorName(){ return "F Major"; }
		@Override
		public String minorName(){ return "D Minor"; }
	},
	Bf{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, N, Fl, N, N, N, Fl}; }
		@Override
		public String majorName(){ return "B Flat Major"; }
		@Override
		public String minorName(){ return "G Minor"; }
	},
	Ef{ 
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, N, Fl, N, N, Fl, Fl}; }
		@Override
		public String majorName(){ return "E Flat Major"; }
		@Override
		public String minorName(){ return "C Minor"; }
	},
	Af{ 
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, Fl, Fl, N, N, Fl, Fl}; }
		@Override
		public String majorName(){ return "A Flat Major"; }
		@Override
		public String minorName(){ return "F Minor"; }
	},
	Df{
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{N, Fl, Fl, N, Fl, Fl, Fl}; }
		@Override
		public String majorName(){ return "D Flat Major"; }
		@Override
		public String minorName(){ return "B Flat Minor"; }
	},
	Gf{ 
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{Fl, Fl, Fl, N, Fl, Fl, Fl}; }
		@Override
		public String majorName(){ return "G Flat Major"; }
		@Override
		public String minorName(){ return "F Flat Minor"; }
	},
	Cf{ 
		@Override
		public Accidental[] getAccidentals() { return new Accidental[]{Fl, Fl, Fl, Fl, Fl, Fl, Fl}; }
		@Override
		public String majorName(){ return "C Flat Major"; }
		@Override
		public String minorName(){ return "A Flat Minor"; }
	};
	
	/* Shorter versions of the Accidentals to improve readability of above arrays */
	private static final Accidental S = Accidental.SHARP;
	private static final Accidental N = Accidental.NATURAL;
	private static final Accidental Fl = Accidental.FLAT;
	
	/**
	 * Get the Accidentals associated with each of the seven notes
	 * (octave minus one) for this KeySignature, formatted
	 * in the following order: { C, D, E, F, G, A, B }.
	 * @return The Accidentals for this KeySignature.
	 */
	public abstract Accidental[] getAccidentals();
	
	/**
	 * Get the major name for this KeySignature.
	 * @return The major name for this KeySignature,
	 * e.g. C Sharp Major.
	 * @author Jason Petersen
	 */
	public abstract String majorName();
	
	/**
	 * Get the minor name for this KeySignature.
	 * @return The minor name for this KeySignature,
	 * e.g. A Flat Minor.
	 * @author Jason Petersen
	 */
	public abstract String minorName();
	
	/**
	 * Get whether or not the KeySignature is sharp.
	 * @return True if KeySignature is sharp, otherwise false.
	 * @author Spring 2010
	 */
	public boolean isSharpKey() {
		return ordinal()<=7;
	}
	
	/**
	 * Get the number of Accidentals for this KeySignature.
	 * @return The number of Accidentals for this KeySignature.
	 * @author Spring 2010
	 */
	public int numAccidentals() {
		int num = ordinal();
		return (num>7) ? num-7 : num;
	}
	
	/**
	 * Get the KeySignature for the given String.
	 * @param name String representing the name of a KeySignature.
	 * @return The KeySignature associated with the given String.
	 * @throws BadInputException There is no KeySignature associated with
	 * the given String.
	 * @author Jason Petersen, Tim Macdonald
	 */
	public static KeySignature getKeySignatureFor(String name) throws BadInputException {
		if(name == null)
			throw new BadInputException("KeySignature name cannot be null");
		
		for(KeySignature s : KeySignature.values())
			if(name.equalsIgnoreCase(s.name())) return s;
		throw new BadInputException("Invalid KeySignature: " + name);
	}
		
}

