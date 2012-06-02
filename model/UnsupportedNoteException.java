/**
 * UnsupportedNoteException.java
 * 
 * Exception class for Note.
 * 
 * @author Spring 2010
 */

package model;

public final class UnsupportedNoteException extends Exception {

	/** Useful if we were going to serialize this class, but this mostly just keeps Eclipse from complaining */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * @param message The error message
	 * @author Spring 2010
	 */
	public UnsupportedNoteException(String message) { 
		super(message); 
	}

}
