/**
 * BadInputException.java
 * 
 * Exception class for invalid input.
 * 
 * @author Spring 2010
 * @author Jason Petersen
 */

package io;

import java.io.IOException;

public class BadInputException extends IOException {
	
	/** Useful if we were going to serialize this class, but this mostly just keeps Eclipse from complaining */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param message The error message
	 * @author Spring 2010
	 */
	public BadInputException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param line The line number where the error occurred.
	 * @param message The error message
	 * @author Spring 2010
	 * @author Jason Petersen
	 */
	public BadInputException(int line, String message) {
		super("Line: " + line + ", " + message);
	}
}
