/**
 * Hymn.java
 * 
 * Proxy for a Hymn. This object allows
 * the GUI to remove/restore verses or
 * display choruses etc. without having
 * access to the internals to the rendering 
 * of the Text/Tune combination.
 * 
 * @author Jason Petersen
 */

package model;

import io.RenderEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class Hymn {
	
	/** Contains already-rendered hymns, which can be reused to aid performance. */
	private static HashMap<Hymn, ArrayList<File>> cache = new HashMap<Hymn, ArrayList<File>>();

	/** The Text for this Hymn */
	private Text text;
	
	/** The Tune for this Hymn */
	private Tune tune;
	
	/** Array determining if a verse should be rendered */
	private boolean[] display;
	
	/** The text for this Hymn should be doubled */
	private boolean doubleText;
		
	/** Determine whether or not to display the chorus for the Tune, if it exists */
	private boolean displayChorus;
	
	/**
	 * Constructor.
	 * @param text The Text for this Hymn.
	 * @param tune The Tune for this Hymn.
	 * @throws InvalidHymnException The given text and tune cannot
	 * be combined to make a hymn because the meters did not match.
	 * @author Jason Petersen
	 */
	public Hymn(Text text, Tune tune) throws InvalidHymnException {
		if(text == null || tune == null)
			throw new InvalidHymnException("Input for the Hymn cannot be null");
		
		if(canCombine(text, tune)){
			this.text = text;
			this.tune = tune;
			this.doubleText = textIsDouble(text, tune);
			this.displayChorus = tune.getChorus() != null;
		}
		else throw new InvalidHymnException("The given Text and Tune cannot be combined");
				
		this.display = new boolean[numberOfVerses()];
		for(int i=0; i<display.length; i++)
			display[i] = true;
	}
	
	/**
	 * Determine if a Text and Tune can be combined
	 * to create a Hymn.
	 * @param x Either a Text or a Tune.
	 * @param y Either a Text or a Tune.
	 * @return True if the meters match or the Text can
	 * be doubled to match the meter of the Tune. x and y
	 * must be of different types, i.e. x is a Text y is a Tune
	 * or y is a Text x is a Tune
	 * @author Jason Petersen
	 */
	public static boolean canCombine(HymnComponent x, HymnComponent y) {
		if(x == null || y == null) return false;
		
		if(x instanceof Text && y instanceof Tune) {
			return canCombine((Text)x, (Tune)y);
		}
		else if(y instanceof Text && x instanceof Tune) {
			return canCombine((Text)y, (Tune)x);
		}
		else {
			return false;
		}
	}

	/**
	 * Determine if a Text and Tune can be combined
	 * to create a Hymn.
	 * @param text The Text to test for combining.
	 * @param tune The Tune to test for combining.
	 * @return True if the meters match or the Text can
	 * be doubled to match the meter of the Tune
	 * @author Jason Petersen
	 */
	public static boolean canCombine(Text text, Tune tune) {
		if(text == null || tune == null) return false;
		if(!text.isValid() || !tune.isValid()) return false;
		
		Iterator<Integer> textMeter = text.getMeter().iterator();
		Iterator<Integer> tuneMeter = tune.getMeter().iterator();
			
		boolean equal = true;
		
		// Check if meters are equal
		while(textMeter.hasNext() && tuneMeter.hasNext())
			if(textMeter.next() != tuneMeter.next()) equal = false;
		if(tuneMeter.hasNext() || textMeter.hasNext()) equal = false;
		
		return equal || textIsDouble(text, tune);
	}
	
	/**
	 * Render the Hymn. The returned file will
	 * be the Hymn as an image.
	 * @return The rendered Hymn image.
	 * @throws IOException An error occurred when
	 * creating the file.
	 * @author Jason Petersen
	 * @author Tim Macdonald
	 */
	public ArrayList<File> render() throws IOException {
		// Fetch itself from the cache
		if(Hymn.cache.containsKey(this)) {
			ArrayList<File> files = Hymn.cache.get(this);
			boolean validFiles = true;
			for(File f : files) {
				if(!f.isFile()) {
					Hymn.cache.remove(this);
					validFiles = false;
					break;
				}
			}
			if(validFiles)
				return files;
		}

		// Search on the disk for existing images
		ArrayList<File> images = RenderEngine.fetch(this.getFilename());
		if(!images.isEmpty()) {
			Hymn.cache.put(this, images);
			return images;
		}
		
		// If all else fails, call lilypond to render the image
		images = RenderEngine.render(this);
		Hymn.cache.put(this, images);
		return images;
	}
	
	@Override
	public String toString() {
		return "Hymn (" + this.text.getName() + " Using " + this.tune.getName() + ")";
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Hymn) {
			Hymn hymn = (Hymn) other;
			if(hymn.text.equals(this.text) && hymn.tune.equals(this.tune)) {
				if(hymn.numberOfVerses() == this.numberOfVerses()) {
					for(int i=0; i<this.numberOfVerses(); i++) {
						if(hymn.display(i+1) != this.display(i+1)) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * To all future debuggers, code reviewers and curious investigators:
	 * We are sorry.
	 * This is a complete and total hack. We're hitting the final hours
	 * and while we really should change the cache to store the Hymn's
	 * filenames instead of the Hymn object itself as the key, we
	 * think leaving this here for you to find and scratch your heads
	 * over is much funnier in the long run.
	 * Much love.
	 * @return The completely crazed calculated hashcode integer.
	 * @author Jason Petersen
	 * @author Tim Macdonald
	 */
	@Override
	public int hashCode() {
		int hc = super.hashCode();
		int i = 1;
		for(boolean b : this.display) {
			if(b) {
				hc += Math.pow(5, i);
			}
			else {
				hc += Math.pow(17, i);
			}
			i++;
		}
		return hc;
	}
	
	/**
	 * Get the Tune for this Hymn.
	 * @return The Tune for this Hymn.
	 * @author Jason Petersen
	 */
	public Tune getTune() {
		return this.tune;
	}
	
	/**
	 * Get the Text for this Hymn.
	 * @return The Text for this Hymn.
	 * @author Jason Petersen
	 */
	public Text getText() {
		return this.text;
	}
	
	/**
	 * Get the filename for this Hymn.
	 * This should probably be drastically re-thought,
	 * for now it's a convenience.
	 * @return The filename for this Hymn.
	 * @author Jason Petersen
	 */
	public String getFilename() { 
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
		String name = text.getName() + "_Using_" + tune.getName();
		
		String filename = "";
		for(char c : name.toCharArray()){
			if(alphabet.contains(c + "")) filename += c;
		}
		
		for(int i=0; i<display.length; i++)
			if(display[i]) filename += "_" + (i+1);
		
		return filename;
	}
	
	/**
	 * Determine the number of verses in this Hymn.
	 * This should be called over text.numberOfVerses
	 * because the text may have been modified to fit
	 * a tune with a double meter.
	 * @return The number of verses for this Hymn.
	 * @author Jason Petersen
	 */
	public int numberOfVerses() {
		if(doubleText){
			return text.numberOfVerses()/2;
		}
		else return text.numberOfVerses();
	}
	
	/**
	 * Determine if the Hymn has a chorus.
	 * Should be used by the GUI to determine
	 * whether to add a chorus remover clicker.
	 * @return True if the Hymn has a chorus,
	 * otherwise false.
	 * @author Jason Petersen
	 */
	public boolean hasChorus() {
		return tune.hasChorus();
	}
	
	/**
	 * Restore a verse for this Hymn.
	 * @param number The verse to restore. If the
	 * verse was not found or has already been restored, 
	 * the input is ignored.
	 * @author Jason Petersen
	 */
	public void restoreVerse(int number) {
		if(validVerse(number)){
			display[number-1] = true;
		}
	}
	
	/**
	 * Remove a verse from this Hymn.
	 * @param number The verse to remove. If the
	 * verse was not found or is already removed,
	 * the input is ignored.
	 * @author Jason Petersen
	 */
	public void removeVerse(int number) {
		if(validVerse(number)){
			display[number-1] = false;
		}
	}
	
	/**
	 * Restore the chorus for the
	 * rendering of this Hymn, if a chorus exists.
	 * @author Jason Petersen
	 */
	public void restoreChorus() {
		displayChorus = true;
	}
	
	/**
	 * Remove the chorus from the
	 * rendering of this Hymn, if a chorus exists.
	 * @author Jason Petersen
	 */
	public void removeChorus() {
		displayChorus = false;
	}
	
	/**
	 * Determine if a verse should be displayed.
	 * @param verse The verse to check.
	 * @return True if the verse should be displayed, otherwise false.
	 * @author Jason Petersen
	 */
	public boolean display(int verse) {
		if(validVerse(verse))
			return display[verse-1];
		return false;
	}
	
	/**
	 * Get the number of verses to display for this Hymn.
	 * @return The number of verses to display for this Hymn.
	 * @author Jason Petersen
	 */
	public int numberOfVersesToDisplay() {
		int i = 0;
		for(boolean b : display)
			if(b) i++;
		return i;
	}
	
	/**
	 * Determine if a chorus should be displayed.
	 * @return False if the Hymn doesn't have a chorus
	 * or if the chorus has been removed, otherwise true.
	 * @author Jason Petersen
	 */
	public boolean displayChorus() {
		return (tune.getChorus() != null) && displayChorus; 
	}
	
	/**
	 * Determine if this Hymn's Text should be doubled.
	 * @return True if the Text should be doubled,
	 * otherwise false.
	 * @author Jason Petersen
	 */
	public boolean doubleText() {
		return doubleText;
	}
	
	/* ---------- Helper methods ---------- */
	
	/**
	 * Helper function to test if a verse number is valid.
	 * @param number The verse number to test.
	 * @return True if the number is greater than 0 and less than
	 * the number of verses, otherwise false.
	 * @author Jason Petersen
	 */
	private boolean validVerse(int number) {
		return number > 0 && number < display.length+1;
	}
	
	/**
	 * Determine if a Text is double that
	 * of the given Tune. 
	 * @param text The Text to test for doubling.
	 * @param tune The Tune to test for doubling.
	 * @return True the Text can be doubled to match 
	 * the meter of the Tune
	 * @author Jason Petersen
	 */
	private static boolean textIsDouble(Text text, Tune tune) {
		if(!text.isValid() || !tune.isValid()) return false;
		
		Iterator<Integer> textMeter = text.getMeter().iterator();
		Iterator<Integer> tuneMeter = tune.getMeter().iterator();
		
		// Advance the iterator
		while(textMeter.hasNext() && tuneMeter.hasNext()) { 
			textMeter.next();
			tuneMeter.next();
		}
		
		boolean dbleText = false;
		
		// Check if lyrics can be doubled
		if(tuneMeter.hasNext()){
			Iterator<Integer> dblText = text.getMeter().iterator();
			boolean dble = true;
			while(tuneMeter.hasNext() && dblText.hasNext())
				if(tuneMeter.next() != dblText.next()) dble = false;
			if(tuneMeter.hasNext() || dblText.hasNext()) dble = false;
			dbleText = dble;
		}
		
		if(text.numberOfVerses() < 2) dbleText = false;
		
		return dbleText;
	}

}
