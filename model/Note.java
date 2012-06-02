/**
 * Note.java
 * 
 * This class contains all information specific to one note in a hymn. 
 * 
 * @author Spring 2010
 * @author Jason Petersen
 */

package model;

import io.BadInputException;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class Note {
	/** The pitch of this Note in  */
	private String pitch;
	
	/** The Accidental for this Note */
	private Accidental accidental = Accidental.NATURAL;
	
	/** A note's duration represented as the number of 
	 	SMALLESTNOTE increments it contains */
	private int durationDenom;
	
	/** Whether the Note is dotted */
	private boolean dotted = false;
	
	/** Whether the Note starts a tie */
	private boolean startsTie = false;
	
	/** This Note's involvement in a slur */
	private Slur slurStatus = Slur.NONE;
	
	/* NOTE: Under the project specifications, the shortest duration for a note allowed is a 16th note. */
	/* Further, this value must always be a power of 2 */
	/** The smallest supported note duration */
	public static final int SMALLESTNOTE = 16;

	/** Determine if this Note is a rest */
	private boolean rest;

	/**
	 * Constructor.
	 * Convenience method which sets default values for dotted,
	 * startsTie, slurStatus and rest to false and NONE.
	 * @param pitch String representing a pitch formatted in scientific pitch notation
	 * @param durationDenom Integer representing this Note's duration, e.g. 4 for quarter,
	 * 8 for eighth
	 * @throws BadInputException The pitch, durationDenom or dotting was invalid.
	 * @throws UnsupportedNoteException Nothing was invalid, it's just not supported. Yet.
	 * @author Jason Petersen
	 */
	public Note(String pitch, int durationDenom) throws BadInputException, UnsupportedNoteException {
		this(pitch, durationDenom, false, false, Slur.NONE, false);
	}
	
	/**
	 * Constructor.
	 * @param pitch String representing a pitch formatted in scientific pitch notation
	 * @param durationDenom Integer representing this Note's duration, e.g. 4 for quarter,
	 * 8 for eighth
	 * @param dotted Boolean representing whether or not this Note is dotted
	 * @param startsTie Boolean representing whether or not this Note starts a tie
	 * @param slurStatus Slur enum representing the involvement of this Note in a slur
	 * @throws BadInputException The pitch, durationDenom or dotting was invalid.
	 * @throws UnsupportedNoteException Nothing was invalid, it's just not supported. Yet.
	 * @author Spring 2010
	 * @author Jason Petersen
	 * @author Dr. Thomas VanDrunen
	 */
	public Note(String pitch, int durationDenom, boolean dotted, boolean startsTie, Slur slurStatus, boolean rest) throws BadInputException, UnsupportedNoteException {
		if(rest) {
			this.startsTie = false;
			this.slurStatus = Slur.NONE;
			this.dotted = false;
			this.pitch = "C4"; // Possibly fix this in the future
			if (durationDenomIsValid(durationDenom)) this.durationDenom = durationDenom;
			else throw new UnsupportedNoteException("DurationDenom \"" + durationDenom + "\" is not supported");
			this.rest = true;
			return;
		}

		this.rest = false;
		this.startsTie = startsTie;
		if(slurStatus != null) this.slurStatus = slurStatus;
		
		if (pitchIsValid(pitch)){
			this.pitch = pitch;
			
			/* This assumes only one accidental per note */
			if (pitch.length() == 2) accidental = Accidental.NATURAL;
			else
				try {
					accidental = Accidental.getAccidentalFor(pitch.charAt(1));
				} catch (BadInputException bie) {
					/* This should never be hit, as pitchIsValid will check the validity of the accidental as well */
					System.err.println("Note - " + bie.getMessage());
				}
		}
		else throw new UnsupportedNoteException("Pitch \"" + pitch + "\" is not supported");
		
		if (durationDenomIsValid(durationDenom)) this.durationDenom = durationDenom;
		else throw new UnsupportedNoteException("DurationDenom \"" + durationDenom + "\" is not supported");
		
		if (dotIsValid() || !dotted) this.dotted = dotted;
		else throw new UnsupportedNoteException("Dot not supported on note " + pitch + "with durationDenom \"" + durationDenom + "\"");
	}
	
	@Override
	public String toString() {
		String s = (rest) ? "(Rest) " : "";
		s += pitch + durationDenom;
		if(dotted) s += ".";
		s += " slur: " + slurStatus + " tie: " + startsTie;
		return s;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Note) {
			Note note = (Note) other;
			return	note.dotted == this.dotted &&
					note.rest == this.rest &&
					note.startsTie == this.startsTie &&
					note.durationDenom == this.durationDenom &&
					note.pitch.equals(this.pitch) &&
					note.accidental == this.accidental &&
					note.slurStatus == this.slurStatus;
		}
		return false;
	}

	/* ---------- Getter methods ---------- */
	
	/**
	 * Get an integer value of the absolute pitch for this Note.
	 * @return An integer value of the absolute pitch in order to make rendering
	 * easier.  The 'C' four octaves below middle C is 0.  For every octave add
	 * 7 (8 possible pitches, but zero-based).  Also add number of pitches above
	 * middle C in that octave.  Example: middle C = 28.
	 * @author Spring 2010
	 */
	public int getPitch() {
		return (((pitch.charAt(0) + 5) - 'A') % 7) + 
		    (7 * Integer.parseInt(pitch.substring(pitch.length() - 1)));
	}

	/**
	 * Get the pitch name (e.g. Af4) for this Note.
	 * @return The pitch name for this Note, a String
	 * formatted in scientific pitch notation.
	 * @author Spring 2010
	 */
	public String getPitchName() {
		return pitch;
	}
	
    /**
     * Get the Accidental for this Note.
     * @return The Accidental for this Note.  This method assumes that
     * Notes will have one accidental at a maximum.
     * @author Spring 2010
	 * @author Jason Petersen
     */
	public Accidental getAccidental() {
		return accidental;
	}

	/**
	 * Get the duration denominator for this Note.
	 * @return The duration denominator for this Note.
	 * @author Spring 2010
	 */
	public int getDurationDenom() {
		return durationDenom;
	}

	/**
	 * Get the duration for this Note relative to SMALLESTNOTE,
	 * e.g. if durationDenom is 8 and dotted, SMALLESTNOTE 16,
	 * return 3.
	 * @return The duration for this Note.
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	public int getDuration() {
		int duration = SMALLESTNOTE/durationDenom;
		if(dotted) duration += (SMALLESTNOTE/2)/durationDenom;
		return duration; 
	}
	
	/**
	 * Get whether or not the Note is dotted.
	 * @return True if it is dotted, false if it is not.
	 * @author Spring 2010
	 */
	public boolean isDotted() {
		return this.dotted;
	}
	
	/**
	 * Predicate for determining if the note is a rest.
	 * @author Tim Macdonald
	 */
	public boolean isRest() {
		return this.rest;
	}
	
	/**
	 * Get whether or not this Note starts a tie.
	 * @return True if the note starts a tie, otherwise false.
	 * @author Jason Petersen
	 */
	public boolean startsTie() {
		return this.startsTie;
	}

	/**
	 * Sets whether the note starts a tie or not.
	 *
	 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
	 */
	public void startsTie(boolean t) {
		this.startsTie = t;
	}

	/**
	 * Get the Slur for this Note.
	 * @return The Slur for this Note.
	 * @author Spring 2010
	 */
	public Slur slurStatus() {
		return this.slurStatus;
	}
	
	/* ---------- Helper methods ---------- */
	
	/**
	 * Test if a pitch formatted in scientific pitch notation is valid.
	 * @param pitch A String representing a pitch formatted in scientific pitch notation
	 * @return True if valid, otherwise false.
	 * @author Spring 2010
	 */
	public static boolean pitchIsValid(String pitch) {
		if(pitch == null) return false;
		
		try {
			Pattern regex = Pattern.compile("^[A-G][snf]?[2-5]$", Pattern.CASE_INSENSITIVE); 
			return regex.matcher(pitch).matches();
		} catch (PatternSyntaxException pse) {
			System.err.println("Note.pitchIsValid - bad regex pattern");
			return true;
		}
	}
	
	/**
	 * Helper method to determine if a durationDenom is valid.
	 * @param denom The duration denominator to check.
	 * @return False if denom is less than zero, not a power of 2,
	 * or greater than SMALLESTNOTE, otherwise true.
	 * @author Spring 2010
	 */
	private static boolean durationDenomIsValid(int denom) {
		if (denom <= 0 || (denom & (denom - 1)) != 0 || denom > SMALLESTNOTE) return false;
		else return true;
	}
	
	/**
	 * Helper method to test if the Note can actually be dotted.
	 * @return True if the durationDenom does not equal SMALLESTNOTE.
	 * If it did, there would be no way to represent the duration of
	 * this Note, since dotting increases duration by 50%.
	 * @author Spring 2010
	 */
	private boolean dotIsValid() {
		return durationDenom != SMALLESTNOTE;
	}
	
}
