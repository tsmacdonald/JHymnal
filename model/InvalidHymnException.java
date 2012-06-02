/**
 * InvalidHymnException.java
 * 
 * Exception class for when an attempt to
 * create a Hymn goes awry.
 * 
 * @author Jason Petersen
 */

package model;

public class InvalidHymnException extends Exception {

	/** Useful if we were going to serialize this class, but this mostly just keeps Eclipse from complaining */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param message The error message
	 * @author Jason Petersen
	 */
	InvalidHymnException(String message) {
		super(message);
	}
}
